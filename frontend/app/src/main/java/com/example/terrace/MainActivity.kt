package com.example.terrace

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.example.terrace.core.navigation.NavigationGraph
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.terrace.features.stats.model.UsageViewModel
import dagger.hilt.android.AndroidEntryPoint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.getValue
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.terrace.core.navigation.Screen
import com.example.terrace.features.global.layout.screen.LayoutComponent

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        window.statusBarColor = Color.TRANSPARENT
        window.navigationBarColor = Color.TRANSPARENT

        setContent {
            val navController = rememberNavController()
            val viewModel: UsageViewModel = hiltViewModel()
            
            // Get current destination
            val currentDestination by navController.currentBackStackEntryAsState()
            val isLoaderScreen = currentDestination?.destination?.route == Screen.Loader.route

            Box(modifier = Modifier.fillMaxSize()) {
                NavigationGraph(navController = navController, usageViewModel = viewModel)
                
                // Show bottom nav only when not on loader screen
                if (!isLoaderScreen) {
                    LayoutComponent(
                        viewModel = hiltViewModel(),
                        navController = navController,
                    )
                }
            }
        }
    }
}
