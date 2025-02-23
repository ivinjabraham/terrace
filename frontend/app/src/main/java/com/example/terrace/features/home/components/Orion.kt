package com.example.terrace.features.home.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.example.terrace.R
import com.example.terrace.features.global.layout.screen.GlowingCommentBox

@Composable
fun Orion(offsetX: Float,opacity: Float) {
    val starSize = 50.dp // Size of the star image
    val halfStarSize = starSize / 2 // Half the size to center lines

    // Define star positions manually
    val starPositions = listOf(
        50.dp to 50.dp,   // 0: Top-left
        150.dp to 80.dp,  // 1: Slightly right
        100.dp to 200.dp, // 2: Middle-left
        215.dp to 265.dp, // 3: Center
        220.dp to 250.dp, // 4: Top-right
        200.dp to 280.dp, // 5: Bottom-middle
        200.dp to 400.dp,
        330.dp to 320.dp // 6: Bottom-left
    )

    // Define explicit node connections (index pairs)
    val connections = listOf(
        0 to 1,
        1 to 4,
        3 to 4,
        3 to 5,
        5 to 6,
        2 to 5,
        2 to 0,
        6 to 7,
        7 to 4
    )

    val description = "A prominent winter constellation, featuring Betelgeuse and Rigel, with a distinct belt of three aligned stars forming Orion the Hunter."
    var showDescription by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .offset { IntOffset(offsetX.toInt(), 0) }
            .padding(16.dp)
            .graphicsLayer( alpha = opacity)
            .clickable(
                interactionSource = remember {  MutableInteractionSource() },
                indication = null
            ) { showDescription = !showDescription }

    ) {
        // Draw dotted lines between specific nodes
        Canvas(modifier = Modifier.fillMaxSize()) {
            val stroke = Stroke(width = 2.dp.toPx())

            connections.forEach { (startIndex, endIndex) ->
                val (x1, y1) = starPositions[startIndex]
                val (x2, y2) = starPositions[endIndex]

                // Adjust the positions to the **center** of each star
                val startOffset = Offset(x1.toPx() + halfStarSize.toPx(), y1.toPx() + halfStarSize.toPx())
                val endOffset = Offset(x2.toPx() + halfStarSize.toPx(), y2.toPx() + halfStarSize.toPx())

                drawLine(
                    color = Color.White.copy(alpha = .2f),
                    start = startOffset,
                    end = endOffset,
                    strokeWidth = 2.dp.toPx(),
                )
            }
        }

        // Place each star at the given positions
        starPositions.forEach { (x, y) ->
            Image(
                painter = painterResource(id = R.drawable.star_1),
                contentDescription = "Star at ($x, $y)",
                modifier = Modifier
                    .size(starSize)
                    .absoluteOffset(x = x, y = y)
            )
        }
    }
    if (showDescription) {
        GlowingCommentBox(title = "Orion", description = description)
    }
}
