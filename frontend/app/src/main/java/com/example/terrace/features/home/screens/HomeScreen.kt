package com.example.terrace.features.auth.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.compose.ui.viewinterop.AndroidView
import com.example.terrace.BackgroundComponent
import com.example.terrace.core.navigation.Screen
import com.example.terrace.core.navigation.safeNavigate

@Composable
fun HomeScreen(navController: NavController) {
    AndroidView(
        factory = { context -> BackgroundComponent(context) },
        modifier = Modifier.fillMaxSize()
    )
}


