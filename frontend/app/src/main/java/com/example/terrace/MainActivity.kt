package com.example.terrace

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import androidx.navigation.compose.rememberNavController
import androidx.compose.foundation.layout.*
import com.example.terrace.core.navigation.NavigationGraph
import com.example.terrace.features.home.components.LeaderboardComponent
import com.example.terrace.features.home.components.LeaderboardEntry
import androidx.compose.ui.Modifier

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            val navController = rememberNavController()
            Column(modifier = Modifier.fillMaxSize()) {
                NavigationGraph(navController)

                LeaderboardComponent(
                    weeklyEntries = listOf(
                        LeaderboardEntry(rank = 1, name = "Jerry A.", points = 890),
                        LeaderboardEntry(rank = 2, name = "Ananya K.", points = 812),
                        LeaderboardEntry(rank = 3, name = "You", points = 560, isCurrentUser = true),
                        LeaderboardEntry(rank = 4, name = "Krishna P.", points = 510),
                        LeaderboardEntry(rank = 5, name = "Fida S.", points = 409)
                    ),
                    monthlyEntries = emptyList(),
                    allTimeEntries = emptyList()
                ).Display()
            }
        }
    }
}