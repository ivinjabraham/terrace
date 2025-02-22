package com.example.terrace

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.example.terrace.core.navigation.NavigationGraph
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.terrace.features.auth.screens.HomeScreen
import com.example.terrace.features.auth.screens.LoginScreen
import android.graphics.Color
import android.os.Build
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.ViewCompat
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = Color.TRANSPARENT
            window.navigationBarColor = Color.TRANSPARENT
        }
        setContent {
            val navController = rememberNavController()
            NavigationGraph(navController)
        }
    }
}
