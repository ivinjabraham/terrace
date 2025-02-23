package com.example.terrace.features.home.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.example.terrace.features.global.layout.screen.GlowingCommentBox
import com.example.terrace.R

@Composable
fun Libra(offsetX: Float, opacity: Float) {
    val starSize = 50.dp
    val halfStarSize = starSize / 2

    // Libra star positions
    val starPositions = listOf(
        100.dp to 200.dp,  // 0: Left end of balance beam
        200.dp to 200.dp,  // 1: Left-middle of beam
        110.dp to 260.dp,  // 2: Center of beam
        90.dp to 270.dp,   // 3: Right-middle of beam
        160.dp to 310.dp,  // 4: Right end of beam
        200.dp to 300.dp,  // 5: Bottom of left scale
        140.dp to 330.dp   // 6: Bottom of right scale
    )

    // Define connections for Libra shape - a horizontal line with two downward lines
    val connections = listOf(
        0 to 1,  // Left horizontal segment
        0 to 2,
        0 to 5,
        5 to 1,
        2 to 3,
        5 to 4,
        4 to 6
    )

    val description = "A zodiac constellation representing balanced scales, symbolizing justice, with dim stars and historical ties to Virgo and Scorpio in mythology."

    // State to track if the description box should be shown
    var showDescription by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .offset { IntOffset(offsetX.toInt(), 0) }
            .padding(16.dp)
            .graphicsLayer(alpha = opacity)
            .clickable { showDescription = !showDescription } // Toggle description visibility on click
    ) {
        // Draw dotted lines between nodes
        Canvas(modifier = Modifier.fillMaxSize()) {
            val stroke = Stroke(width = 2.dp.toPx())

            connections.forEach { (startIndex, endIndex) ->
                val (x1, y1) = starPositions[startIndex]
                val (x2, y2) = starPositions[endIndex]

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
        GlowingCommentBox(title = "Libra", description = description)
    }
}