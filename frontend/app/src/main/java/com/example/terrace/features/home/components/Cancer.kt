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
fun Cancer(offsetX: Float, opacity: Float) {
    val starSize = 50.dp // Size of the star image
    val halfStarSize = starSize / 2 // Half the size to center lines

    // Define star positions manually
    val starPositions = listOf(
        150.dp to 150.dp,
        170.dp to 230.dp,
        250.dp to 200.dp,
        40.dp to 40.dp,
        100.dp to 100.dp
    )

    // Define explicit node connections (index pairs)
    val connections = listOf(
        0 to 1,
        0 to 2,
        0 to 3,
        3 to 4
    )

    val description = "A faint zodiac constellation resembling a crab, home to the Beehive Cluster, best viewed in spring, symbolizing nurturing and protection."
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
                    color = Color.White.copy(alpha = 0.2f),
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
        GlowingCommentBox(title = "Cancer", description = description)
    }
}
