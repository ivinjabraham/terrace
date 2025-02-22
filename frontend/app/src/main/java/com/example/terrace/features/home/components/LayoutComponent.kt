package com.example.terrace.features.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.terrace.R

@Composable
fun LayoutComponent(
    onStatsClick: () -> Unit = {},
    onConstellationClick: () -> Unit = {},
    onLeaderboardClick: () -> Unit = {},
    onPrevConstellation: () -> Unit = {},
    onNextConstellation: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color.Black.copy(alpha = 0.8f), // Dark at the top
                        Color.Transparent,               // Fully transparent in the center
                        Color.Transparent,
                        Color.Transparent,
                        Color.Transparent,
                        Color.Transparent,
                        Color.Black.copy(alpha = 0.8f)  // Dark at the bottom
                    )
                )
            )
    ) {
        BottomNavBar(
            onStatsClick = onStatsClick,
            onConstellationClick = onConstellationClick,
            onLeaderboardClick = onLeaderboardClick,
            onPrevConstellation = onPrevConstellation,
            onNextConstellation = onNextConstellation
        )
    }
}

@Composable
fun BottomNavBar(
    onStatsClick: () -> Unit = {},
    onConstellationClick: () -> Unit = {},
    onLeaderboardClick: () -> Unit = {},
    onPrevConstellation: () -> Unit = {},
    onNextConstellation: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 18.dp), // Add padding to the navigation bar
        verticalArrangement = Arrangement.Bottom
    ) {
        BottomNavigation(
            backgroundColor = Color.Transparent, // Keep it fully transparent
            contentColor = Color.Transparent,
            elevation = 0.dp
        ) {
            BottomNavigationItem(
                icon = { Icon(painterResource(R.drawable.ic_arrow_left), contentDescription = "Previous", modifier = Modifier.size(26.dp), tint = Color.White) },
                label = { Text("Prev", fontSize = 8.sp, color = Color.White) },
                selected = false,
                onClick = onPrevConstellation
            )

            BottomNavigationItem(
                icon = { Icon(painterResource(R.drawable.ic_stats), contentDescription = "Stats", modifier = Modifier.size(26.dp), tint = Color.White ) },
                label = { Text("Stats", fontSize = 8.sp, color = Color.White) },
                selected = false,
                onClick = onStatsClick
            )

            BottomNavigationItem(
                icon = { Icon(painterResource(R.drawable.ic_constellation), contentDescription = "Constellation", modifier = Modifier.size(26.dp), tint = Color.White) },
                label = { Text("Constellation", fontSize = 8.sp, color = Color.White) },
                selected = false,
                onClick = onConstellationClick
            )

            BottomNavigationItem(
                icon = { Icon(painterResource(R.drawable.ic_leaderboard), contentDescription = "Leaderboard", modifier = Modifier.size(26.dp), tint = Color.White) },
                label = { Text("Leaderboard", fontSize = 8.sp, color = Color.White) },
                selected = false,
                onClick = onLeaderboardClick
            )

            BottomNavigationItem(
                icon = { Icon(painterResource(R.drawable.ic_arrow_right), contentDescription = "Next", modifier = Modifier.size(26.dp), tint = Color.White) },
                label = { Text("Next", fontSize = 8.sp, color = Color.White) },
                selected = false,
                onClick = onNextConstellation
            )
        }
    }
}
