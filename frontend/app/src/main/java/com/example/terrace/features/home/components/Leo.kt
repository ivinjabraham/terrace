package com.example.terrace.features.home.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
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
fun Leo(offsetX: Float, opacity: Float) {
    val starSize = 50.dp // Size of the star image
    val halfStarSize = starSize / 2 // Half the size to center lines

    // Define star positions manually
    val starPositions = listOf(
        150.dp to 200.dp,
        220.dp to 130.dp,
        250.dp to 178.dp,
        400.dp to 180.dp,
        390.dp to 135.dp,
        370.dp to 115.dp,
        360.dp to 85.dp,
        385.dp to 25.dp,
        420.dp to 45.dp,

    )

    // Define explicit node connections (index pairs)
    val connections = listOf(
        0 to 1,
        0 to 2,
        1 to 2,
        2 to 3,
        1 to 5,
        3 to 4,
        4 to 5,
        5 to 6,
        6 to 7,
        7 to 8
    )

    val description = "A majestic lion-shaped constellation, home to bright star Regulus, best seen in spring, linked to Greek mythology's Nemean Lion."
    var showDescription by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .offset { IntOffset(offsetX.toInt(), 0) }
            .padding(16.dp)
            .graphicsLayer( alpha = opacity)
            .clickable { showDescription = !showDescription } // Toggle description visibility on click
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
        GlowingCommentBox(title = "Leo", description = description)
    }
}
