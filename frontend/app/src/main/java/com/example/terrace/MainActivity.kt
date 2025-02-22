package com.example.terrace


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.terrace.features.auth.screens.LoginScreen
import com.example.terrace.core.navigation.NavigationGraph
import android.graphics.Color
import androidx.core.view.WindowCompat
import com.example.terrace.features.home.screens.HomeScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        window.statusBarColor = Color.TRANSPARENT
        window.navigationBarColor = Color.TRANSPARENT
        setContent {
            val navController = rememberNavController()
            NavHost(navController, startDestination = "home") {
                composable("login") { LoginScreen(navController) }
                composable("home") { HomeScreen(navController) }
            }
        }
    }
}