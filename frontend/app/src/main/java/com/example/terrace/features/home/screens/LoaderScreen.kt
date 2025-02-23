package com.example.terrace.features.home.screens

import android.annotation.SuppressLint
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
import com.example.terrace.core.auth.SessionManager
import com.example.terrace.core.navigation.Screen
import androidx.compose.runtime.LaunchedEffect
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import android.content.Context
import androidx.compose.ui.platform.LocalContext

@EntryPoint
@InstallIn(SingletonComponent::class)
interface SessionManagerEntryPoint {
    fun sessionManager(): SessionManager
}

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun LoaderScreen(navController: NavController) {
    val context = LocalContext.current
    val sessionManager = remember {
        EntryPointAccessors.fromApplication(
            context.applicationContext,
            SessionManagerEntryPoint::class.java
        ).sessionManager()
    }

    var isVisible by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        delay(2000)
        val destination = if (sessionManager.getAuthToken() != null) {
            "home/0/false"
        } else {
            "home/0/false"
        }
        
        navController.navigate(destination) {
            popUpTo(Screen.Loader.route) { inclusive = true }
        }
        isVisible = false
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF111121)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.mipmap.boy),
                contentDescription = "App Logo",
                modifier = Modifier
                    .size(100.dp)
                    .padding(bottom = 32.dp)
            )
            
            Text(
                text = "Terrace",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            
            LoadingDots()
        }

        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 64.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            val constellationFacts = remember {
                listOf(
                    "There are 88 officially recognized constellations",
                    "The largest constellation is Hydra, covering 3.16% of the night sky",
                    "The Big Dipper is not a constellation but an asterism",
                    "Constellations slowly change position over thousands of years",
                    "The zodiac constellations follow the ecliptic path of the sun"
                )
            }
            Text(
                text = "Did you know?\n${constellationFacts.random()}",
                color = Color(0xFF6D8BFF),
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(horizontal = 32.dp)
                    .alpha(0.8f)
            )
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
