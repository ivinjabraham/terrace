package com.example.terrace.features.home.screens

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.terrace.features.home.components.LeaderboardComponent
import com.example.terrace.features.home.components.LeaderboardEntry

@Composable
fun HomeScreen() {
    LeaderboardComponent(
        weeklyEntries = listOf(
            LeaderboardEntry(rank = 4, name = "Jerry A.", points = 890),
            LeaderboardEntry(rank = 5, name = "Ananya K.", points = 812),
            LeaderboardEntry(rank = 6, name = "You", points = 560, isCurrentUser = true),
            LeaderboardEntry(rank = 7, name = "Krishna P.", points = 510),
            LeaderboardEntry(rank = 9, name = "Fida S.", points = 409)
        ),
        monthlyEntries = emptyList(),
        allTimeEntries = emptyList()
    )
}


