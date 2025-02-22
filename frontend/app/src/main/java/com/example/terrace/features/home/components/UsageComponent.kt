package com.example.terrace

import android.app.AppOpsManager
import android.app.usage.UsageEvents
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import java.util.Calendar

@Composable
fun UsageComponent(context: Context) {
    var hasPermission by remember { mutableStateOf(NoUsageComponent(context)) }
    var screenTime by remember { mutableStateOf<Long?>(null) }
    var selectedDays by remember { mutableStateOf(1) } // Default: 1 Day

    // Keep checking for permission every second
    LaunchedEffect(Unit) {
        while (!hasPermission) {
            hasPermission = NoUsageComponent(context)
            delay(1000) // Check every second
        }
        // Once permission is granted, fetch screen time
        screenTime = YesUsageComponent(context, selectedDays)
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (hasPermission) {
            Text("Select time range:", fontSize = 18.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Row {
                Button(onClick = { selectedDays = 1; screenTime = YesUsageComponent(context, selectedDays) }) { Text("1 Day") }
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = { selectedDays = 7; screenTime = YesUsageComponent(context, selectedDays) }) { Text("7 Days") }
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = { selectedDays = 30; screenTime = YesUsageComponent(context, selectedDays) }) { Text("30 Days") }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = formatScreenTime(screenTime ?: 0), fontSize = 18.sp)
        } else {
            Text("Permission required to access screen time", fontSize = 18.sp)
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { requestUsageAccess(context) }) {
                Text("Grant Permission")
            }
        }
    }
}

// Check if the app has Usage Access permission
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

// Request the user to enable Usage Access permission
fun requestUsageAccess(context: Context) {
    val intent = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
    context.startActivity(intent)
}

// Get screen-on time usage
fun YesUsageComponent(context: Context, days: Int): Long {
    val usageStatsManager = context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
    val calendar = Calendar.getInstance()
    val endTime = calendar.timeInMillis
    calendar.add(Calendar.DAY_OF_MONTH, -days)
    val startTime = calendar.timeInMillis

    val eventStats = usageStatsManager.queryEvents(startTime, endTime)

    var screenOnTime: Long = 0
    var lastScreenOn: Long? = null
    val event = UsageEvents.Event()

    while (eventStats.hasNextEvent()) {
        eventStats.getNextEvent(event)
        if (event.eventType == UsageEvents.Event.SCREEN_INTERACTIVE) {
            lastScreenOn = event.timeStamp
        } else if (event.eventType == UsageEvents.Event.SCREEN_NON_INTERACTIVE && lastScreenOn != null) {
            screenOnTime += event.timeStamp - lastScreenOn!!
            lastScreenOn = null
        }
    }
    return screenOnTime // Returns screen-on time in milliseconds
}

// Convert milliseconds to hours and minutes
fun formatScreenTime(milliseconds: Long): String {
    val minutes = (milliseconds / 1000) / 60
    val hours = minutes / 60
    val minutesInHour = minutes % 60
    return "Total screen time: $hours hours and $minutesInHour minutes"
}