package com.example.terrace.features.home.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp

import com.example.terrace.R

@Composable
fun Libra(offsetX: Float) {
    val starSize = 50.dp
    val halfStarSize = starSize / 2

    // Big Dipper star positions
    val starPositions = listOf(
        100.dp to 50.dp,   // 0: Top-left
        200.dp to 80.dp,   // 1: Slightly right
        300.dp to 150.dp,  // 2: Further right
        400.dp to 200.dp,  // 3: Center star
        350.dp to 300.dp,  // 4: Down-left
        250.dp to 400.dp,  // 5: Bottom-middle
        150.dp to 350.dp   // 6: Bottom-left
    )

    // Define connections for Big Dipper shape
    val connections = listOf(
        0 to 1,
        1 to 2,
        2 to 3,
        3 to 4,
        4 to 5,
        5 to 6
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .offset { IntOffset(offsetX.toInt(), 0) }
            .padding(16.dp)
    ) {
        // Draw dotted lines between nodes
        Canvas(modifier = Modifier.fillMaxSize()) {
            val stroke = Stroke(width = 2.dp.toPx(), pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f)))

            connections.forEach { (startIndex, endIndex) ->
                val (x1, y1) = starPositions[startIndex]
                val (x2, y2) = starPositions[endIndex]

                val startOffset = Offset(x1.toPx() + halfStarSize.toPx(), y1.toPx() + halfStarSize.toPx())
                val endOffset = Offset(x2.toPx() + halfStarSize.toPx(), y2.toPx() + halfStarSize.toPx())

                drawLine(
                    color = Color.White,
                    start = startOffset,
                    end = endOffset,
                    strokeWidth = 2.dp.toPx(),
                    pathEffect = stroke.pathEffect
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
}
