package com.example.terrace.features.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * LeaderboardComponent as a class
 */
class LeaderboardComponent(
    private val weeklyEntries: List<LeaderboardEntry>,
    private val monthlyEntries: List<LeaderboardEntry>,
    private val allTimeEntries: List<LeaderboardEntry>
) {
    @Composable
    fun Display() {
        val tabs = listOf("Weekly", "Monthly", "All Time")
        var selectedTabIndex by remember { mutableStateOf(0) }

        Column(modifier = Modifier.fillMaxSize()) {
            // Top Tab Row
            TabRow(
                selectedTabIndex = selectedTabIndex,
                backgroundColor = Color(0xFF1B1B2F),
                contentColor = Color.White
            ) {
                tabs.forEachIndexed { index, text ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        text = { Text(text = text, fontWeight = FontWeight.SemiBold) }
                    )
                }
            }

            // Decide which list of entries to display
            val currentEntries = when (selectedTabIndex) {
                0 -> weeklyEntries
                1 -> monthlyEntries
                else -> allTimeEntries
            }

            // Leaderboard List
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFF111121))
                    .padding(16.dp)
            ) {
                items(currentEntries) { entry ->
                    LeaderboardRow(entry)
                }
            }
        }
    }
}

@Composable
private fun LeaderboardRow(entry: LeaderboardEntry) {
    val rowModifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 4.dp)

    if (entry.isCurrentUser) {
        Box(
            modifier = rowModifier
                .clip(RoundedCornerShape(12.dp))
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(Color(0xFF6A5ACD), Color(0xFF483D8B))
                    )
                )
                .padding(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = entry.rank.toString(),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color.White,
                    modifier = Modifier.width(32.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = entry.name,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    color = Color.White,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = entry.points.toString(),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color.White,
                    textAlign = TextAlign.End
                )
            }
        }
    } else {
        Card(
            shape = RoundedCornerShape(12.dp),
            backgroundColor = Color(0xFF242447),
            modifier = rowModifier
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = entry.rank.toString(),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color.White,
                    modifier = Modifier.width(32.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = entry.name,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    color = Color.White,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = entry.points.toString(),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color.White,
                    textAlign = TextAlign.End
                )
            }
        }
    }
}