package com.example.terrace.features.splash

import android.os.Handler
import android.os.Looper
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.delay
import com.example.terrace.R

@Composable
fun LoaderScreen(navController: NavController) {
    var isVisible by remember { mutableStateOf(true) }

    // Fade-out animation
    val alpha by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0f,
        animationSpec = tween(durationMillis = 1000, easing = LinearEasing)
    )

    // Navigate after 3 seconds
    LaunchedEffect(Unit) {
        delay(3000)
        isVisible = false
        delay(1000) // Wait for fade-out
        navController.navigate("home") {
            popUpTo("loader") { inclusive = true } // Remove LoaderScreen from stack
        }
    }

    if (isVisible) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Gray)
                .alpha(alpha), // Apply fade-out animation
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                // App Logo
                Image(
                    painter = painterResource(id = R.mipmap.boy), // Your PNG logo
                    contentDescription = "App Logo",
                    modifier = Modifier
                        .size(100.dp)
                        .clip(RoundedCornerShape(percent = 50))
                )
                Spacer(modifier = Modifier.height(8.dp))

                // App Name
                Text(
                    text = "Terrace",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Loading Animation (Dots)
                LoadingDots()
            }
        }
    }
}

@Composable
fun LoadingDots() {
    val dotCount = 3
    val infiniteTransition = rememberInfiniteTransition()

    val dotsAlpha = List(dotCount) { index ->
        infiniteTransition.animateFloat(
            initialValue = 0.3f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(1000, easing = LinearEasing, delayMillis = index * 300),
                repeatMode = RepeatMode.Reverse
            )
        )
    }

    Row {
        repeat(dotCount) { index ->
            Text(
                text = ".",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White.copy(alpha = dotsAlpha[index].value),
                modifier = Modifier.padding(horizontal = 4.dp)
            )
        }
    }
}
