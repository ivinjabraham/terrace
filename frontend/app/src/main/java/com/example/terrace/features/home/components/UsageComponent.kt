package com.example.terrace

import android.app.AppOpsManager
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import java.util.Calendar

@Composable
fun UsageComponent(context: Context) {
    var hasPermission by remember { mutableStateOf(NoUsageComponent(context)) }
    var screenTime by remember { mutableStateOf(0L) }
    var selectedDays by remember { mutableStateOf(1) }
    var appUsageStats by remember { mutableStateOf<Map<String, Long>>(emptyMap()) }

    LaunchedEffect(Unit) {
        while (!hasPermission) {
            hasPermission = NoUsageComponent(context)
            delay(100)
        }
        screenTime = YesUsageComponent(context, selectedDays)
        appUsageStats = getAppUsageStats(context, selectedDays)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (hasPermission) {
            Spacer(modifier = Modifier.height(16.dp))
            Text("Usage Stats", fontSize = 22.sp, color = Color.Black)
            Spacer(modifier = Modifier.height(12.dp))
            Text("Select Time Range:", fontSize = 18.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Row {
                listOf(1, 7, 10).forEach { days ->
                    Button(
                        onClick = {
                            selectedDays = days
                            screenTime = YesUsageComponent(context, days)
                            appUsageStats = getAppUsageStats(context, days)
                        },
                        modifier = Modifier.padding(horizontal = 4.dp)
                    ) {
                        Text("$days Day${if (days > 1) "s" else ""}")
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Total Screen Time:", fontSize = 18.sp, color = Color.Black)
                    Text(formatScreenTime(screenTime), fontSize = 16.sp, color = Color.Gray)
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            AppUsagePieChart(context, appUsageStats)
        } else {
            Text("Permission required to access screen time", fontSize = 18.sp)
            Button(onClick = { requestUsageAccess(context) }) {
                Text("Grant Permission", fontSize = 16.sp)
            }
        }
    }
}

@Composable
fun AppUsagePieChart(context: Context, appUsageStats: Map<String, Long>) {
    val sortedEntries = appUsageStats.entries
        .sortedByDescending { it.value }
        .take(7)

    val totalUsage = sortedEntries.sumOf { it.value }
    val sliceColors = listOf(
        Color(0xFF4285F4), // Blue
        Color(0xFFDB4437), // Red
        Color(0xFFF4B400), // Yellow
        Color(0xFF0F9D58), // Green
        Color(0xFF7B1FA2), // Purple
        Color(0xFF0097A7), // Cyan
        Color(0xFFE65100)  // Orange
    )

    Canvas(modifier = Modifier.size(200.dp)) {
        var startAngle = 0f
        sortedEntries.forEachIndexed { index, entry ->
            val sweepAngle = if (totalUsage > 0) (entry.value.toFloat() / totalUsage) * 360f else 0f
            drawArc(
                color = sliceColors[index % sliceColors.size],
                startAngle = startAngle,
                sweepAngle = sweepAngle,
                useCenter = true
            )
            startAngle += sweepAngle
        }
    }

    Spacer(modifier = Modifier.height(12.dp))

    Column {
        sortedEntries.forEachIndexed { index, entry ->
            val percentage = if (totalUsage > 0)
                (entry.value.toFloat() / totalUsage * 100).toInt()
            else 0

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 6.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(16.dp)
                        .background(sliceColors[index % sliceColors.size],
                            shape = RoundedCornerShape(4.dp))
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "${entry.key}: ${formatScreenTime(entry.value)} ($percentage%)",
                    fontSize = 16.sp,
                    color = Color.Black,
                    textAlign = TextAlign.Start
                )
            }
        }
    }
}

fun getAppName(context: Context, packageName: String): String {
    return try {
        val packageManager = context.packageManager
        val appInfo = packageManager.getApplicationInfo(packageName, 0)
        packageManager.getApplicationLabel(appInfo).toString()
    } catch (e: Exception) {
        val parts = packageName.split(".")
        if (parts.size >= 2) parts[1].replaceFirstChar { it.uppercaseChar() } else packageName
    }
}

fun NoUsageComponent(context: Context): Boolean {
    val appOps = context.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
    val mode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        appOps.unsafeCheckOpNoThrow(
            AppOpsManager.OPSTR_GET_USAGE_STATS,
            android.os.Process.myUid(),
            context.packageName
        )
    } else {
        appOps.checkOpNoThrow(
            AppOpsManager.OPSTR_GET_USAGE_STATS,
            android.os.Process.myUid(),
            context.packageName
        )
    }
    return mode == AppOpsManager.MODE_ALLOWED
}

fun requestUsageAccess(context: Context) {
    val intent = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
    context.startActivity(intent)
}

fun YesUsageComponent(context: Context, days: Int): Long {
    val usageStatsManager = context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
    val calendar = Calendar.getInstance()
    val endTime = calendar.timeInMillis
    calendar.add(Calendar.DAY_OF_MONTH, -days)
    val startTime = calendar.timeInMillis

    val stats = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, startTime, endTime)
    return stats.sumOf { it.totalTimeInForeground }
}

fun getAppUsageStats(context: Context, days: Int): Map<String, Long> {
    val usageStatsManager = context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
    val calendar = Calendar.getInstance()
    val endTime = calendar.timeInMillis
    calendar.add(Calendar.DAY_OF_MONTH, -days)
    val startTime = calendar.timeInMillis

    val stats = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, startTime, endTime)

    return stats
        .filter { it.totalTimeInForeground > 0 }
        .groupBy { getAppName(context, it.packageName) }
        .mapValues { (_, usageStats) -> usageStats.sumOf { it.totalTimeInForeground } }
        .toSortedMap()
}

fun formatScreenTime(milliseconds: Long): String {
    val totalMinutes = (milliseconds / 1000) / 60
    val hours = totalMinutes / 60
    val minutes = totalMinutes % 60
    return if (hours > 0) "$hours hours, $minutes minutes" else "$minutes minutes"
}