package com.example.terrace.features.auth.screens

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.compose.ui.viewinterop.AndroidView
import com.example.terrace.BackgroundComponent

@Composable
fun HomeScreen(navController: NavController) {
    AndroidView(
        factory = { context -> BackgroundComponent(context) },
        modifier = Modifier.fillMaxSize()
    )
}


