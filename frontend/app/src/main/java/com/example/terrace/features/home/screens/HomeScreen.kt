package com.example.terrace.features.home.screens

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.terrace.features.home.components.LeaderboardComponent
import com.example.terrace.features.home.components.LeaderboardEntry

@Composable
fun HomeScreen(navController: NavController) {
    Column(modifier = Modifier.fillMaxSize()) {
        LeaderboardComponent(
            entries = listOf(
                LeaderboardEntry(rank = 1, name = "Jerry A.", points = 890),
                LeaderboardEntry(rank = 2, name = "Ananya K.", points = 812),
                LeaderboardEntry(rank = 3, name = "You", points = 560, isCurrentUser = true),
                LeaderboardEntry(rank = 4, name = "Krishna P.", points = 510),
                LeaderboardEntry(rank = 5, name = "Fida S.", points = 409)
            )
        )
    }
}
