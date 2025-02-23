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
fun BigDipper(offsetX: Float, opacity: Float) {
    val starSize = 50.dp
    val halfStarSize = starSize / 2

    // Updated Big Dipper star positions to better match the actual constellation
    val starPositions = listOf(
        150.dp to 100.dp,  // 0: Dubhe (Alpha UMa)
        250 .dp to 120.dp,  // 1: Merak (Beta UMa)
        300.dp to 160.dp,  // 2: Phecda (Gamma UMa)
        350.dp to 170.dp,  // 3: Megrez (Delta UMa)
        355.dp to 210.dp,  // 4: Alioth (Epsilon UMa)
        420.dp to 220.dp,  // 5: Mizar (Zeta UMa)
        440.dp to 175.dp   // 6: Alkaid (Eta UMa)
    )

    // Connections remain the same as they follow the traditional pattern
    val connections = listOf(
        0 to 1,  // Dubhe to Merak
        1 to 2,  // Merak to Phecda
        2 to 3,  // Phecda to Megrez
        3 to 4,  // Megrez to Alioth
        4 to 5,  // Alioth to Mizar
        5 to 6,
        6 to 3// Mizar to Alkaid
    )

    val description = "A bright, spoon-shaped asterism in Ursa Major, often used for navigation, pointing to Polaris, the North Star."
    var showDescription by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .offset { IntOffset(offsetX.toInt(), 0) }
            .padding(16.dp)
            .graphicsLayer( alpha = opacity)
            .clickable { showDescription = !showDescription } // Toggle description visibility on click
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
    if (showDescription) {
        GlowingCommentBox(title = "Big Dipper", description = description)
    }
}