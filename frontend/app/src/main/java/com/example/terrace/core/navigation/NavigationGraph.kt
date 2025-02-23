package com.example.terrace.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.terrace.features.stats.model.UsageViewModel
import com.example.terrace.features.auth.screens.LoginScreen
import com.example.terrace.features.auth.screens.RegisterScreen
import com.example.terrace.features.leaderboard.screens.LeaderboardScreen
import com.example.terrace.features.home.screens.HomeScreen
import com.example.terrace.features.stats.screen.UsageScreen
import com.example.terrace.features.splash.LoaderScreen

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Register : Screen("register")
    object Home : Screen("home")
    object Usage : Screen("usage")
    object Loader : Screen("loader")
    object Leaderboard : Screen("leaderboard")
}

@Composable
fun NavigationGraph(navController: NavHostController, usageViewModel: UsageViewModel) {
    val context = LocalContext.current // Get Context

    NavHost(navController = navController, startDestination = Screen.Loader.route) {
        composable(Screen.Loader.route) {
            LoaderScreen(navController)
        }
        composable(Screen.Login.route) { LoginScreen(navController) }
        composable(Screen.Register.route) { RegisterScreen(navController) }
        composable(Screen.Home.route) { HomeScreen(navController, usageViewModel = usageViewModel) } // Pass Context to HomeScreen
        composable(Screen.Usage.route) { UsageScreen(context, viewModel = usageViewModel) }
        composable(Screen.Leaderboard.route) {
            LeaderboardScreen(entries = emptyList(), navController = navController)
        }
    }
}
