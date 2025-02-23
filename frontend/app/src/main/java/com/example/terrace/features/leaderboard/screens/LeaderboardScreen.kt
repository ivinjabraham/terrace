package com.example.terrace.features.leaderboard.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.material3.Text
import androidx.navigation.NavController
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import kotlinx.coroutines.delay
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.terrace.features.leaderboard.model.LeaderboardViewModel
import javax.inject.Inject

import com.example.terrace.R
import com.example.terrace.features.leaderboard.LeaderboardEntry
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import android.util.Log
import com.example.terrace.core.auth.SessionManager
import com.example.terrace.core.navigation.Screen
import androidx.compose.ui.platform.LocalContext
import com.example.terrace.features.home.screens.LoadingDots


import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent

val Philosopher = FontFamily(
    Font(R.font.philosopher, FontWeight.Normal)
)

@EntryPoint
@InstallIn(SingletonComponent::class)
interface SessionManagerEntryPoint {
    fun sessionManager(): SessionManager
}

@Composable
fun LeaderboardScreen(navController: NavController, viewModel: LeaderboardViewModel = hiltViewModel()) {
    val context = LocalContext.current
    val sessionManager = remember {
        EntryPointAccessors.fromApplication(
            context.applicationContext,
            SessionManagerEntryPoint::class.java
        ).sessionManager()
    }

    LaunchedEffect(Unit) {
        if (sessionManager.getAuthToken() == null) {
            navController.navigate(Screen.Login.route) {
                popUpTo(Screen.Loader.route)
            }
        }
    }

    val entries by viewModel.entries
    val isLoading by viewModel.isLoading
    val error by viewModel.error

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF111121))
    ) {
        if (isLoading) {
            // Show loading indicator
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(16.dp))
                LoadingDots()
                Text(
                    text = "Loading Leaderboard...",
                    color = Color.White,
                    fontFamily = Philosopher
                )
            }
        } else if (error != null) {
            // Show error message
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Error: $error", color = Color.Red)
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = { /* Retry logic */ }) {
                    Text("Retry")
                }
            }
        } else {
            // Show actual content
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFF111121))
                    .padding(top = 36.dp)
            ) {
                Text(
                    text = "Leaderboards",
                    fontFamily = Philosopher,
                    fontWeight = FontWeight.Bold,
                    fontSize = 30.sp,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp)
                )

                // Scrollable LazyColumn for entries
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp)
                ) {
                    items(entries) { entry ->
                        LeaderboardRow(entry)
                    }
                }
            }
        }
    }
}

@Composable
private fun LeaderboardRow(entry: LeaderboardEntry) {
    val rowModifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 6.dp)

    if (entry.isCurrentUser) {
        Box(
            modifier = rowModifier
                .clip(RoundedCornerShape(12.dp))
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(Color(0xFF6A5ACD), Color(0xFF483D8B))
                    )
                )
                .padding(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = entry.rank.toString(),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color.White,
                    modifier = Modifier.width(32.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = entry.name,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = Philosopher,
                    fontSize = 24.sp,
                    color = Color.White,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = entry.points.toString(),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color.White,
                    textAlign = TextAlign.End
                )
            }
        }
    } else {
        Card(
            shape = RoundedCornerShape(12.dp),
            backgroundColor = Color(0xFF242447),
            modifier = rowModifier
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = entry.rank.toString(),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color.White,
                    modifier = Modifier.width(32.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = entry.name,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = Philosopher,
                    fontSize = 24.sp,
                    color = Color.White,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = entry.points.toString(),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color.White,
                    textAlign = TextAlign.End
                )
            }
        }
    }
}

