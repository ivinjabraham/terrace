package com.example.terrace.features.home.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.*
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.*
import androidx.navigation.NavController
import kotlin.math.sqrt
import kotlin.random.Random
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import com.example.terrace.R

import android.util.Log

import androidx.compose.material3.*
import androidx.compose.ui.zIndex

@OptIn(ExperimentalMaterial3Api::class)


@Composable
fun HomeScreen(navController: NavController) {
    var screenSize by remember { mutableStateOf(IntSize.Zero) }
    val density = LocalDensity.current
    val starCount = 100
    val clusterCount = 10

    var transformState by remember {
        mutableStateOf(TransformState(
            offsetX = 0f,
            scale = 1f
        ))
    }

    val minScale = 1.0f
    val maxScale = 1.8f

    val starPositions = remember(screenSize) {
        mutableStateListOf<StarData>().apply {
            if (screenSize.width > 100 && screenSize.height > 100) {
                while (size < starCount) {
                    val newPosition = getRandomPosition(screenSize)
                    if (none { it.position.distanceTo(newPosition) < 100 }) {
                        add(
                            StarData(
                                position = newPosition,
                                size = Random.nextInt(10, 60).dp,
                                rotation = Random.nextFloat() * 360f,
                                twinkleSpeed = Random.nextInt(200, 800),
                                drawableRes = if (Random.nextInt(3) == 0) R.drawable.star_2 else R.drawable.star_1
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
                        val offsetX = (clusterCenter.x + distance * kotlin.math.cos(angle)).coerceIn(-screenSize.width.toFloat(), screenSize.width.toFloat() * 2)
                        val offsetY = (clusterCenter.y + distance * kotlin.math.sin(angle)).coerceIn(0f, screenSize.height.toFloat())
                        add(
                            StarData(
                                position = Offset(offsetX, offsetY),
                                size = Random.nextInt(10, 40).dp,
                                rotation = Random.nextFloat() * 360f,
                                twinkleSpeed = Random.nextInt(200, 800),
                                drawableRes = if (Random.nextInt(3) == 0) R.drawable.star_2 else R.drawable.star_1
                            )
                        )
                    }
                }
            }
        }
    }

    val minStarX = remember(starPositions) { starPositions.minOfOrNull { it.position.x } ?: 0f }
    val maxStarX = remember(starPositions) { starPositions.maxOfOrNull { it.position.x } ?: screenSize.width.toFloat() }

    val minOffset = (minStarX - 50).coerceAtMost(0f)
    val maxOffset = (maxStarX - screenSize.width + 50).coerceAtLeast(0f)

    val draggableState = rememberDraggableState { delta ->
        transformState = transformState.copy(
            offsetX = (transformState.offsetX + delta)
                .coerceIn(minOffset * transformState.scale, maxOffset * transformState.scale)
        )
        Log.d("HomeScreen", "Dragging: offsetX = ${transformState.offsetX}")
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Transparent Top Bar
        Column(modifier = Modifier.fillMaxWidth()) {
            TopAppBar(
                title = {
                    Text("Starry Sky")
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.hsv(0f, 0f, 0f, 0.2f),  // Transparent background
                    titleContentColor = Color.White // Title color
                ),
                modifier = Modifier.zIndex(1f) // Ensure TopAppBar stays above other content
            )
        }

        // Starry Sky Content
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 56.dp) // Adjust padding to prevent overlap with TopAppBar
                .onGloballyPositioned { coordinates ->
                    screenSize = IntSize(coordinates.size.width, coordinates.size.height)
                    Log.d("HomeScreen", "Screen Size: $screenSize")
                }
                .pointerInput(Unit) {
                    detectTransformGestures { centroid, pan, zoom, _ ->
                        val oldScale = transformState.scale
                        val newScale = (oldScale * zoom).coerceIn(minScale, maxScale)

                        val contentPoint = (centroid - Offset(transformState.offsetX, 0f)) / oldScale
                        val deltaContentPoint = contentPoint * (newScale - oldScale)

                        val newOffsetX = (transformState.offsetX - deltaContentPoint.x + pan.x)
                            .coerceIn(minOffset * newScale, maxOffset * newScale)

                        Log.d("HomeScreen", "New OffsetX: $newOffsetX, Previous OffsetX: ${transformState.offsetX}, Scale: $newScale")

                        transformState = transformState.copy(
                            scale = newScale,
                            offsetX = newOffsetX
                        )

                        Log.d("HomeScreen", "Updated Transform: offsetX = $newOffsetX, scale = $newScale")
                    }
                }

                .draggable(state = draggableState, orientation = Orientation.Horizontal)
        ) {
            // Background gradients (Starry Sky)
            Canvas(modifier = Modifier.fillMaxSize()) {
                drawRect(
                    brush = Brush.radialGradient(
                        colors = listOf(Color(0x0CA506EF), Color(0x1E000000), Color.Black),
                        center = Offset(
                            x = (transformState.offsetX - size.width)
                                .coerceIn(-size.width.toFloat(), size.width.toFloat()),
                            y = size.height
                        ),
                        radius = size.minDimension * 5f
                    ),
                    size = size
                )

                drawRect(
                    brush = Brush.radialGradient(
                        colors = listOf(Color(0x1DFF0000), Color(0x1E050001), Color.Black),
                        center = Offset(
                            x = (size.width * 2 + transformState.offsetX)
                                .coerceIn(-size.width.toFloat(), size.width.toFloat() * 2),
                            y = 0f
                        ),
                        radius = size.minDimension * 5f
                    ),
                    size = size
                )
            }

            // Star layer
            Box(
                modifier = Modifier
                    .width(with(density) { (maxStarX - minStarX + screenSize.width).toDp() })
                    .fillMaxHeight()
                    .graphicsLayer {
                        scaleX = transformState.scale
                        scaleY = transformState.scale
                        translationX = transformState.offsetX
                    }
            ) {
                starPositions.forEach { star ->
                    val starImage: Painter = painterResource(id = star.drawableRes)

                    val twinkleAlpha by animateFloatAsState(
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
                                    alpha = twinkleAlpha * 0.6f
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
                                    alpha = twinkleAlpha
                                },
                            contentScale = ContentScale.Fit
                        )
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

fun Offset.distanceTo(other: Offset) = sqrt((x - other.x) * (x - other.x) + (y - other.y) * (y - other.y))

data class StarData(
    val position: Offset,
    val size: Dp,
    val rotation: Float,
    val twinkleSpeed: Int,
    val drawableRes: Int
)

private data class TransformState(
    val offsetX: Float,
    val scale: Float
)
