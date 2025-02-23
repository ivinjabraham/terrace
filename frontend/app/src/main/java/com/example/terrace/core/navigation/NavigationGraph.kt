package com.example.terrace.core.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

import com.example.terrace.features.auth.screens.LoginScreen
import com.example.terrace.features.auth.screens.RegisterScreen
import com.example.terrace.features.leaderboard.LeaderboardScreen
import com.example.terrace.features.home.screens.HomeScreen
import com.example.terrace.features.stats.UsageScreen
import com.example.terrace.features.splash.LoaderScreen

// Define all screens
sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Register : Screen("register")
    object Home : Screen("home")
    object Usage : Screen("usage")
    object Loader : Screen("loader")
    object Leaderboard : Screen("leaderboard")
}

@Composable
fun NavigationGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Screen.Loader.route) {
        composable(Screen.Loader.route) {
            LoaderScreen(navController) // Show Loader First
        }
        composable(Screen.Login.route) { LoginScreen(navController) }
        composable(Screen.Register.route) { RegisterScreen(navController) }
        composable(Screen.Home.route) { HomeScreen(navController) }
        composable(Screen.Usage.route) { UsageScreen(navController.context) }
        composable(Screen.Leaderboard.route) {
            LeaderboardScreen(entries = emptyList(), navController = navController)
        }
    }
}
