package com.example.terrace.features.global.layout.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.terrace.R
import com.example.terrace.features.global.layout.viewmodel.LayoutViewModel
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController
import com.example.terrace.features.global.layout.viewmodel.NavigationAction
import androidx.compose.ui.Alignment // Import this for Alignment.BottomCenter

@Composable
fun LayoutComponent(navController: NavController, viewModel: LayoutViewModel) {
    val navigationEvent by viewModel.navigationEvent.observeAsState()

    // Observe navigation events
    LaunchedEffect(navigationEvent) {
        when (navigationEvent) {
            NavigationAction.Stats -> navController.navigate("usage")
            NavigationAction.Constellation -> navController.navigate("constellation")
            NavigationAction.Leaderboard -> navController.navigate("leaderboard")
            NavigationAction.Previous -> {} // Handle previous action
            NavigationAction.Next -> {} // Handle next action
            null -> {} // Do nothing
            else -> {}
        }
        viewModel.resetNavigationEvent() // Reset navigation event after handling
    }
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.Transparent,
                                Color.Transparent,
                                Color.Transparent,
                                Color.Transparent,
                                Color.Black.copy(alpha = 0.8f)
                            )
                        )
                    )
            ) {
                // Bottom Navigation Bar - Anchored to the bottom
                Box(
                    modifier = Modifier.align(Alignment.BottomCenter) // Stick to bottom
                ) {
                    BottomNavBar(viewModel)
                }

                // Glowing Comment Box - Positioned just above BottomNavBar
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter) // Stick to bottom
                        .padding(bottom = 80.dp) // Move it up
                ) {
                    GlowingCommentBox(
                        title = "Leo",
                        description = "fqufof qof qow q woqwo idoqdjoqwi qowid qowi doiqqodiwj qowdiqodij"
                    )
                }
            }
}

@Composable
fun GlowingCommentBox(title: String, description: String) {
    Box(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(0.75f) // Reduce width (85% of screen width)
            .height(IntrinsicSize.Min) // Adjust height based on content
            .shadow(12.dp, RoundedCornerShape(16.dp)) // Softer shadow
            .drawBehind {
                drawIntoCanvas { canvas ->
                    val paint = Paint().apply {
                        color = Color(0xFFFF66B2) // Pink glow
                        asFrameworkPaint().setMaskFilter(
                            android.graphics.BlurMaskFilter(40f, android.graphics.BlurMaskFilter.Blur.NORMAL)
                        )
                    }
                    withTransform({ translate(-10f, -10f) }) {
                        canvas.drawRoundRect(
                            left = 0f,
                            top = 0f,
                            right = size.width,
                            bottom = size.height,
                            radiusX = 60f,
                            radiusY = 60f,
                            paint = paint
                        )
                    }
                }
            }
            .border(
                width = 2.dp,
                color = Color(0xFFFF66B2), // Pink glow border
                shape = RoundedCornerShape(16.dp)
            )
            .background(
                color = Color(0x55FF66B2), // More transparent pink
                shape = RoundedCornerShape(16.dp)
            )
            .padding(16.dp) // More padding for better spacing
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Title
            androidx.compose.material3.Text(
                text = title,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )

            // Description
            androidx.compose.material3.Text(
                text = description,
                fontSize = 15.sp,
                color = Color.White,
                textAlign = TextAlign.Justify,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun BottomNavBar(viewModel: LayoutViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 18.dp),
        verticalArrangement = Arrangement.Bottom
    ) {
        BottomNavigation(
            backgroundColor = Color.Transparent,
            contentColor = Color.Transparent,
            elevation = 0.dp
        ) {
            BottomNavigationItem(
                icon = { Icon(painterResource(R.drawable.ic_arrow_left), contentDescription = "Previous", modifier = Modifier.size(26.dp), tint = Color.White) },
                label = { Text("Prev", fontSize = 8.sp, color = Color.White) },
                selected = false,
                onClick = { viewModel.onPrevConstellation() }
            )

            BottomNavigationItem(
                icon = { Icon(painterResource(R.drawable.ic_stats), contentDescription = "Stats", modifier = Modifier.size(26.dp), tint = Color.White ) },
                label = { Text("Stats", fontSize = 8.sp, color = Color.White) },
                selected = false,
                onClick = { viewModel.onStatsClick() }
            )

            BottomNavigationItem(
                icon = { Icon(painterResource(R.drawable.ic_constellation), contentDescription = "Constellation", modifier = Modifier.size(26.dp), tint = Color.White) },
                label = { Text("Constellation", fontSize = 8.sp, color = Color.White) },
                selected = false,
                onClick = { viewModel.onConstellationClick() }
            )

            BottomNavigationItem(
                icon = { Icon(painterResource(R.drawable.ic_leaderboard), contentDescription = "Leaderboard", modifier = Modifier.size(26.dp), tint = Color.White) },
                label = { Text("Leaderboard", fontSize = 8.sp, color = Color.White) },
                selected = false,
                onClick = { viewModel.onLeaderboardClick() }
            )

            BottomNavigationItem(
                icon = { Icon(painterResource(R.drawable.ic_arrow_right), contentDescription = "Next", modifier = Modifier.size(26.dp), tint = Color.White) },
                label = { Text("Next", fontSize = 8.sp, color = Color.White) },
                selected = false,
                onClick = { viewModel.onNextConstellation() }
            )
        }
    }
}
