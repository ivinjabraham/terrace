package com.example.terrace.features.home.screens

import StarData
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.*
import androidx.navigation.NavController
import kotlin.math.sqrt
import kotlin.random.Random
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity

import com.example.terrace.R

import android.util.Log

import androidx.compose.material3.*
import androidx.compose.ui.zIndex
import android.media.MediaPlayer
import androidx.compose.foundation.background
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.terrace.features.home.components.BigDipper
import com.example.terrace.features.home.components.LittleDipper
import com.example.terrace.features.home.components.Orion
import com.example.terrace.features.global.layout.screen.LayoutComponent
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

import androidx.compose.ui.unit.dp


import com.example.terrace.features.home.components.Libra
import com.example.terrace.core.navigation.Screen
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.terrace.features.home.components.Cancer
import com.example.terrace.features.home.components.Leo
import com.example.terrace.features.home.model.HomeViewModel
import com.example.terrace.features.stats.model.UsageViewModel
import dagger.hilt.android.EntryPointAccessors

val constellationUnlocks = listOf(
    1388 to 5000,
    5555 to 20000,
    12500 to 60000,
    22222 to 150000,
    34722 to 350000,
    50000 to 800000
)

@Composable
fun getOpacity(level: Int, score: Int): Float {
    val (_, requiredScore) = constellationUnlocks.findLast { it.first <= level } ?: return 0f
    return (score.toFloat() / requiredScore).coerceIn(0f, 1f)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController, usageViewModel: UsageViewModel, isFriend: Boolean, sscore: Int) {
    val context = LocalContext.current
    val sessionManager = remember {
        EntryPointAccessors.fromApplication(
            context.applicationContext,
            SessionManagerEntryPoint::class.java
        ).sessionManager()
    }
    val homeViewModel: HomeViewModel = hiltViewModel()

    LaunchedEffect(Unit) {
        if (!isFriend) {
            homeViewModel.fetchScore()
        }
        if (sessionManager.getAuthToken() == null) {
            navController.navigate(Screen.Login.route) {
                popUpTo(Screen.Loader.route) { inclusive = true }
            }
        }
    }

    val scoreResponse = homeViewModel.score.collectAsState().value
    val score = if (isFriend) sscore else (scoreResponse?.score ?: 0)

    val baseOpacity = 0.05f


    val littleDipperOpacity = if (score >= 1388) 1f else baseOpacity
    val bigDipperOpacity = if (score >= 5555) 1f else baseOpacity
    val libraOpacity = if (score >= 12500) 1f else baseOpacity
    val leoOpacity = if (score >= 12500) 1f else baseOpacity
    val cancerOpacity = if (score >= 22222) 1f else baseOpacity
    val orionOpacity = if (score >= 50000) 1f else baseOpacity

    var screenSize by remember { mutableStateOf(IntSize.Zero) }
    val density = LocalDensity.current

    val starCount = when {
        score < 5000 -> 5
        score < 10000 -> 30
        score < 20000 -> 80
        score < 30000 -> 110
        score < 40000 -> 130
        else -> 150
    }

    val clusterCount = when {
        score < 5000 -> 0
        score < 10000 -> 2
        score < 20000 -> 6
        score < 30000 -> 10
        score < 40000 -> 10
        else -> 10
    }


    var offsetX by remember { mutableFloatStateOf(0f) }
    var direction by remember { mutableStateOf(1f) }
    // Auto-scroll effect


    LaunchedEffect(Unit) {
        Log.d("HomeScreen", "Calling fetchUsageStats")
        usageViewModel.fetchUsageStats(context, 7)
        while (true) {
            withFrameNanos {
                offsetX += direction * 2f // Adjust speed here
                if (offsetX >= screenSize.width * 2f || offsetX <= -screenSize.width) {
                    direction *= -1 // Reverse direction
                }
            }
        }
    }

    val starPositions = remember(screenSize) {
        mutableStateListOf<StarData>().apply {
            if (screenSize.width > 100 && screenSize.height > 100) {
                while (size < starCount) {
                    val newPosition = getRandomPosition(screenSize)
                    if (none { it.position.distanceTo(newPosition) < 100 }) {
                        val size = Random.nextInt(10, 60).dp
                        add(
                            StarData(
                                position = newPosition,
                                size = size,
                                rotation = Random.nextFloat() * 360f,
                                twinkleSpeed = Random.nextInt(200, 800),
                                drawableRes = if (Random.nextInt(3) == 0) R.drawable.star_2 else R.drawable.star_1,
                                sizeCategory = classifyStarBySize(size)
                            )
                        )
                    }
                }

                repeat(clusterCount) {
                    val clusterCenter = getRandomPosition(screenSize)
                    val clusterSize = Random.nextInt(5, 15)
                    repeat(clusterSize) {
                        val angle = Random.nextFloat() * 360f
                        val distance = Random.nextInt(90, 200).toFloat()
                        val coffsetX = (clusterCenter.x + distance * kotlin.math.cos(angle))
                            .coerceIn(-screenSize.width.toFloat(), screenSize.width.toFloat() * 2)
                        val coffsetY = (clusterCenter.y + distance * kotlin.math.sin(angle))
                            .coerceIn(0f, screenSize.height.toFloat())
                        add(
                            StarData(
                                position = Offset(coffsetX, coffsetY),
                                size = Random.nextInt(10, 40).dp,
                                rotation = Random.nextFloat() * 360f,
                                twinkleSpeed = Random.nextInt(200, 800),
                                drawableRes = if (Random.nextInt(3) == 0) R.drawable.star_2 else R.drawable.star_1,
                                sizeCategory = classifyStarBySize(Random.nextInt(10, 40).dp)
                            )
                        )
                    }
                }
            }
        }
    }

    // Play audio when entering HomeScreen

    DisposableEffect(context) {
        val mediaPlayer = MediaPlayer.create(context, R.raw.celestia)
        mediaPlayer.isLooping = true
        mediaPlayer.start()

        onDispose {
            mediaPlayer.stop()
            mediaPlayer.release()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Transparent Top Bar with status bar padding
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Transparent)
                .statusBarsPadding()
                .navigationBarsPadding()
                .zIndex(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TopAppBar(
                title = { },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    titleContentColor = Color.White
                )
            )
            LayoutComponent(viewModel = viewModel(), navController = navController)
        }
        // Starry Sky Content – padded so the gradient doesn't extend into the status bar area0x110088FF
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(WindowInsets.statusBars.asPaddingValues())
                .onGloballyPositioned { coordinates ->
                    screenSize = IntSize(coordinates.size.width, coordinates.size.height)
                    Log.d("HomeScreen", "Screen Size: $screenSize")
                }
                .pointerInput(Unit) {
                    detectTransformGestures { _, pan, _, _ ->
                        // Clamp offsetX within the range
                        offsetX = (offsetX + pan.x).coerceIn(
                            -screenSize.width.toFloat(),
                            screenSize.width * 2f
                        )
                        Log.d("HomeScreen", "Dragging: offsetX = $offsetX")
                    }
                }
        )  {
            // Background gradients (Starry Sky)
            Canvas(modifier = Modifier.fillMaxSize()) {
                drawRect(
                    brush = Brush.radialGradient(
                        colors = listOf(Color(0x0CA506EF), Color(0x1E000000), Color.Black),
                        center = Offset(
                            x = screenSize.width / 2f,
                            y = screenSize.height / 2f
                        ),
                        radius = size.minDimension * 5f
                    ),
                    size = size
                )

                drawRect(
                    brush = Brush.radialGradient(
                        colors = listOf(Color(0x6B002344), Color(0x1E2A000A), Color.Black),
                        center = Offset(
                            x = screenSize.width / 2f,
                            y = 0f
                        ),
                        radius = size.minDimension * 5f
                    ),
                    size = size
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .offset { IntOffset(offsetX.toInt() + screenSize.width * 2 - 300, -1000) }
                    // Move the entire StarryBox
            ) {
                LittleDipper(offsetX,littleDipperOpacity)
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .offset { IntOffset(offsetX.toInt() + screenSize.width * 2 - 1400, 500) }

            ) {
                BigDipper(offsetX,bigDipperOpacity)
            }
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .offset { IntOffset(offsetX.toInt() + 0, 0) } // Move the entire StarryBox
            ) {
                Libra(offsetX,libraOpacity)
            }
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .offset { IntOffset(offsetX.toInt() + -screenSize.width, 0) } // Move the entire StarryBox
            ) {
                Cancer(offsetX,cancerOpacity)
            }
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .offset { IntOffset(offsetX.toInt() + -screenSize.width - 2000, 0) } // Move the entire StarryBox
            ) {
                Leo(offsetX,leoOpacity)
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .offset { IntOffset(offsetX.toInt() + -screenSize.width - 1000, 500) } // Move the entire StarryBox
            ) {
                Orion(offsetX,orionOpacity)
            }


            // Different layers for parallax effect
            Box(modifier = Modifier.fillMaxSize()) {
                // Small Stars
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .offset { IntOffset((offsetX * 0.5).toInt(), 0) }
                ) {
                    starPositions.filter { it.sizeCategory == StarSizeCategory.SMALL }
                        .forEach { star ->
                            RenderStar(star, density, twinkleAlpha = Random.nextFloat())
                        }
                }

                // Medium Stars
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .offset { IntOffset((offsetX * 0.7).toInt(), 0) }
                ) {
                    starPositions.filter { it.sizeCategory == StarSizeCategory.MEDIUM }
                        .forEach { star ->
                            RenderStar(star, density, twinkleAlpha = Random.nextFloat())
                        }
                }

                // Large Stars
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .offset { IntOffset((offsetX * 0.9).toInt(), 0) }
                ) {
                    starPositions.filter { it.sizeCategory == StarSizeCategory.LARGE }
                        .forEach { star ->
                            RenderStar(star, density, twinkleAlpha = Random.nextFloat())
                        }
                }
            }
        }
    }
}

fun getRandomPosition(screenSize: IntSize): Offset {
    if (screenSize.width < 100 || screenSize.height < 100) return Offset.Zero
    return Offset(
        x = Random.nextInt(-screenSize.width, screenSize.width * 2).toFloat(),
        y = Random.nextInt(50, screenSize.height - 50).toFloat()
    )
}

fun Offset.distanceTo(other: Offset) =
    sqrt((x - other.x) * (x - other.x) + (y - other.y) * (y - other.y))

// Classification based on size
enum class StarSizeCategory {
    SMALL, MEDIUM, LARGE
}

fun classifyStarBySize(size: Dp): StarSizeCategory {
    return when {
        size.value < 20 -> StarSizeCategory.SMALL
        size.value in 20f..40f -> StarSizeCategory.MEDIUM
        else -> StarSizeCategory.LARGE
    }
}

@Composable
fun RenderStar(star: StarData, density: Density, twinkleAlpha: Float) {
    val starImage: Painter = painterResource(id = star.drawableRes)

    val twinkleAlphaAnimation by animateFloatAsState(
        targetValue = if (Random.nextFloat() < 0.5f) 0.5f else 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(star.twinkleSpeed, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    Box(
        modifier = Modifier
            .offset(
                with(density) { star.position.x.toDp() },
                with(density) { star.position.y.toDp() }
            )
            .size(star.size)
    ) {
        Image(
            painter = starImage,
            contentDescription = null,
            modifier = Modifier
                .size(star.size)
                .graphicsLayer {
                    rotationZ = star.rotation
                    alpha = twinkleAlphaAnimation * 0.6f
                    shadowElevation = 15f
                    scaleX = 1.3f
                    scaleY = 1.3f
                },
            contentScale = ContentScale.Fit
        )

        Image(
            painter = starImage,
            contentDescription = "Twinkling Star",
            modifier = Modifier
                .size(star.size)
                .graphicsLayer {
                    rotationZ = star.rotation
                    alpha = twinkleAlphaAnimation
                },
            contentScale = ContentScale.Fit
        )
    }
}