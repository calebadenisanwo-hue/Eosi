package com.example.meridian.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Animated pixel companion pet ("Spark" / "Sol line") with floating bob motion and soft warm halo glow.
 */
@Composable
fun PixelCompanionPet(
    modifier: Modifier = Modifier,
    size: Dp = 48.dp,
    showAura: Boolean = true,
    isHappy: Boolean = false
) {
    val infiniteTransition = rememberInfiniteTransition(label = "pet_float")
    val bobOffset by infiniteTransition.animateFloat(
        initialValue = -3.5f,
        targetValue = 3.5f,
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = EaseInOutCubic),
            repeatMode = RepeatMode.Reverse
        ),
        label = "bob_offset"
    )

    val auraPulse by infiniteTransition.animateFloat(
        initialValue = 0.85f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(1800, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "aura_pulse"
    )

    Box(
        modifier = modifier
            .size(size)
            .offset(y = bobOffset.dp),
        contentAlignment = Alignment.Center
    ) {
        if (showAura) {
            // Soft warm celestial halo glow behind the companion
            Box(
                modifier = Modifier
                    .size(size * 1.5f * auraPulse)
                    .clip(CircleShape)
                    .background(
                        Brush.radialGradient(
                            colors = listOf(
                                Color(0xFFFFDAB9).copy(alpha = 0.45f),
                                Color(0xFFFDE8D0).copy(alpha = 0.20f),
                                Color.Transparent
                            )
                        )
                    )
            )
        }

        Canvas(modifier = Modifier.size(size)) {
            val pixelSize = this.size.width / 14f

            // Spark companion pixel bitmap layout (14x14 grid)
            // Color tokens
            val outline = Color(0xFF261C14) // Dark outline
            val body = Color(0xFFAED4E6)    // Soft sky cyan / cloud
            val highlight = Color(0xFFD8EEF8) // Light sparkle
            val eye = Color(0xFF1E1712)     // Eyes
            val blush = Color(0xFFF7A89A)   // Cute cheek blush
            val starCore = Color(0xFFFCEADE)

            val grid = listOf(
                "....XXXX....",
                "...X....X...",
                "..X.BBBB.X..",
                ".X.BBBBBB.X.",
                "X.BBHBBHBB.X",
                "XBB.E..E.BBX",
                "XBB..BB..BBX",
                "XBBPLMMLPBBX",
                "X.BBLLLLBB.X",
                ".X.BBBBBB.X.",
                "..X.BBBB.X..",
                "...X....X...",
                "....XXXX...."
            )

            // Draw friendly star companion
            for (row in grid.indices) {
                val line = grid[row]
                for (col in line.indices) {
                    val char = line[col]
                    val color = when (char) {
                        'X' -> outline
                        'B' -> body
                        'H' -> highlight
                        'E' -> eye
                        'P' -> blush
                        'L' -> if (isHappy) Color(0xFFE87A68) else outline
                        'M' -> if (isHappy) outline else body
                        else -> null
                    }
                    if (color != null) {
                        drawRect(
                            color = color,
                            topLeft = Offset(col * pixelSize, row * pixelSize),
                            size = Size(pixelSize + 0.5f, pixelSize + 0.5f)
                        )
                    }
                }
            }
        }
    }
}

/**
 * Pixel Heart Icon for navigation & touch reactions
 */
@Composable
fun PixelHeart(
    modifier: Modifier = Modifier,
    size: Dp = 24.dp,
    primaryColor: Color = Color(0xFFEF6A60),
    secondaryColor: Color = Color(0xFF8BBF9F)
) {
    Canvas(modifier = modifier.size(size)) {
        val pixelSize = this.size.width / 11f

        // Split two-tone or solid pixel heart
        val heartGrid = listOf(
            ".XX...XX.",
            "XXXX.XXXX",
            "XXXXXXXXX",
            "XXXXXXXXX",
            ".XXXXXXX.",
            "..XXXXX..",
            "...XXX...",
            "....X...."
        )

        for (row in heartGrid.indices) {
            val line = heartGrid[row]
            for (col in line.indices) {
                if (line[col] == 'X') {
                    // Left half secondary (green) / Right half primary (coral) like the app mockup
                    val color = if (col < 4) secondaryColor else primaryColor
                    drawRect(
                        color = color,
                        topLeft = Offset(col * pixelSize, row * pixelSize),
                        size = Size(pixelSize + 0.5f, pixelSize + 0.5f)
                    )
                }
            }
        }
    }
}
