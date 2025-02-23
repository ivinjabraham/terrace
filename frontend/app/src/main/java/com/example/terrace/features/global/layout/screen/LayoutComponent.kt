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
import com.example.terrace.features.home.viewmodel.LayoutViewModel
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.navigation.NavController
import com.example.terrace.features.home.viewmodel.NavigationAction

@Composable
fun LayoutComponent(navController: NavController, viewModel: LayoutViewModel) {
    val navigationEvent by viewModel.navigationEvent.observeAsState()

    // Observe navigation events
    LaunchedEffect(navigationEvent) {
        when (navigationEvent) {
            NavigationAction.Stats -> navController.navigate("stats")
            NavigationAction.Constellation -> navController.navigate("constellation")
            NavigationAction.Leaderboard -> navController.navigate("leaderboard")
            NavigationAction.Previous -> {} // Handle previous action
            NavigationAction.Next -> {} // Handle next action
            null -> {} // Do nothing
            else -> {}
        }
        viewModel.resetNavigationEvent() // Reset navigation event after handling
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color.Transparent,
                        Color.Transparent,
                        Color.Transparent,
                        Color.Transparent,
                        Color.Transparent,
                        Color.Black.copy(alpha = 0.8f)
                    )
                )
            )
    ) {
        BottomNavBar(viewModel)
    }
}

@Composable
fun BottomNavBar(viewModel: LayoutViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 18.dp),
        verticalArrangement = Arrangement.Bottom
    ) {
        BottomNavigation(
            backgroundColor = Color.Transparent,
            contentColor = Color.Transparent,
            elevation = 0.dp
        ) {
            BottomNavigationItem(
                icon = { Icon(painterResource(R.drawable.ic_arrow_left), contentDescription = "Previous", modifier = Modifier.size(26.dp), tint = Color.White) },
                label = { Text("Prev", fontSize = 8.sp, color = Color.White) },
                selected = false,
                onClick = { viewModel.onPrevConstellation() }
            )

            BottomNavigationItem(
                icon = { Icon(painterResource(R.drawable.ic_stats), contentDescription = "Stats", modifier = Modifier.size(26.dp), tint = Color.White ) },
                label = { Text("Stats", fontSize = 8.sp, color = Color.White) },
                selected = false,
                onClick = { viewModel.onStatsClick() }
            )

            BottomNavigationItem(
                icon = { Icon(painterResource(R.drawable.ic_constellation), contentDescription = "Constellation", modifier = Modifier.size(26.dp), tint = Color.White) },
                label = { Text("Constellation", fontSize = 8.sp, color = Color.White) },
                selected = false,
                onClick = { viewModel.onConstellationClick() }
            )

            BottomNavigationItem(
                icon = { Icon(painterResource(R.drawable.ic_leaderboard), contentDescription = "Leaderboard", modifier = Modifier.size(26.dp), tint = Color.White) },
                label = { Text("Leaderboard", fontSize = 8.sp, color = Color.White) },
                selected = false,
                onClick = { viewModel.onLeaderboardClick() }
            )

            BottomNavigationItem(
                icon = { Icon(painterResource(R.drawable.ic_arrow_right), contentDescription = "Next", modifier = Modifier.size(26.dp), tint = Color.White) },
                label = { Text("Next", fontSize = 8.sp, color = Color.White) },
                selected = false,
                onClick = { viewModel.onNextConstellation() }
            )
        }
    }
}
