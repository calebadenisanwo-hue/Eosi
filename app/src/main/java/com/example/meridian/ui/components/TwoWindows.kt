package com.example.meridian.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.meridian.core.time.MeridianTime
import com.example.meridian.data.model.UserProfile
import com.example.meridian.data.model.UserPresence
import com.example.ui.theme.FrauncesFontFamily
import com.example.ui.theme.MeridianApricot
import com.example.ui.theme.MeridianSkyPalettes
import java.time.Instant
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun TwoWindows(
    myUser: UserProfile,
    myPresence: UserPresence,
    partnerUser: UserProfile,
    partnerPresence: UserPresence,
    currentInstant: Instant = MeridianTime.now(),
    modifier: Modifier = Modifier
) {
    val myTime = MeridianTime.formatInZone(currentInstant, myUser.timezone)
    val partnerTime = MeridianTime.formatInZone(currentInstant, partnerUser.timezone)
    val diff = MeridianTime.offsetDifference(myUser.timezone, partnerUser.timezone, currentInstant)
    val diffLabel = MeridianTime.formatDifference(diff)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .testTag("two_windows_container")
            .padding(horizontal = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            // My Window (Left Arch)
            ArchWindowItem(
                user = myUser,
                presence = myPresence,
                timeInfo = myTime,
                isViewer = true,
                modifier = Modifier.weight(1f)
            )

            // Center Separator Pill
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(horizontal = 8.dp, vertical = 36.dp)
            ) {
                Box(
                    modifier = Modifier
                        .height(1.dp)
                        .width(28.dp)
                        .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f))
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = diffLabel,
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(4.dp))
                Box(
                    modifier = Modifier
                        .height(1.dp)
                        .width(28.dp)
                        .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f))
                )
            }

            // Partner Window (Right Arch)
            ArchWindowItem(
                user = partnerUser,
                presence = partnerPresence,
                timeInfo = partnerTime,
                isViewer = false,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun ArchWindowItem(
    user: UserProfile,
    presence: UserPresence,
    timeInfo: MeridianTime.FormattedTime,
    isViewer: Boolean,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(horizontal = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Arch-shaped sky viewport
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(130.dp)
                .clip(RoundedCornerShape(topStart = 64.dp, topEnd = 64.dp, bottomStart = 16.dp, bottomEnd = 16.dp))
        ) {
            SkyCanvas(
                hour24 = timeInfo.hour24,
                minute = timeInfo.minute,
                modifier = Modifier.fillMaxSize()
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Name & city
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${user.avatarEmoji} ${user.displayName}",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
            if (isViewer) {
                Text(
                    text = " (You)",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // Big Serif Local Time
        Row(
            verticalAlignment = Alignment.Bottom
        ) {
            Text(
                text = timeInfo.timeStr,
                fontFamily = FrauncesFontFamily,
                fontSize = 32.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.width(3.dp))
            Text(
                text = timeInfo.period,
                fontFamily = FrauncesFontFamily,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        // Date
        Text(
            text = timeInfo.dateStr,
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(6.dp))

        // Presence Pill
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .background(
                    if (presence.state == "Around") Color(0xFF5B9E78).copy(alpha = 0.15f)
                    else MaterialTheme.colorScheme.surfaceVariant
                )
                .padding(horizontal = 10.dp, vertical = 4.dp)
        ) {
            Text(
                text = presence.state,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                color = if (presence.state == "Around") Color(0xFF388E3C) else MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun SkyCanvas(
    hour24: Int,
    minute: Int,
    modifier: Modifier = Modifier
) {
    val colors = MeridianSkyPalettes.getSkyGradient(hour24)
    val isNight = hour24 in 21..23 || hour24 in 0..4

    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height

        // Background Sky Gradient
        drawRect(
            brush = Brush.verticalGradient(
                colors = colors,
                startY = 0f,
                endY = h
            )
        )

        // Celestial trajectory: sun or moon along an arc
        val normalizedTime = (hour24 * 60 + minute).toFloat() / (24 * 60)
        val angle = Math.PI * (1.0 - (normalizedTime % 1.0))
        val cx = (w * 0.5f) + (w * 0.38f * cos(angle)).toFloat()
        val cy = (h * 0.85f) - (h * 0.65f * sin(angle)).toFloat()

        if (isNight) {
            // Pale stars
            drawCircle(Color.White.copy(alpha = 0.7f), radius = 1.5.dp.toPx(), center = Offset(w * 0.25f, h * 0.25f))
            drawCircle(Color.White.copy(alpha = 0.5f), radius = 1.2.dp.toPx(), center = Offset(w * 0.7f, h * 0.2f))
            drawCircle(Color.White.copy(alpha = 0.8f), radius = 1.8.dp.toPx(), center = Offset(w * 0.45f, h * 0.35f))
            drawCircle(Color.White.copy(alpha = 0.6f), radius = 1.0.dp.toPx(), center = Offset(w * 0.8f, h * 0.45f))

            // Crescent Moon
            drawCircle(
                color = Color(0xFFEDE9E3),
                radius = 10.dp.toPx(),
                center = Offset(cx, cy)
            )
            // Moon shadow crescent mask
            drawCircle(
                color = colors[0],
                radius = 8.5.dp.toPx(),
                center = Offset(cx + 3.dp.toPx(), cy - 2.dp.toPx())
            )
        } else {
            // Warm Glowing Sun
            drawCircle(
                color = MeridianApricot.copy(alpha = 0.25f),
                radius = 18.dp.toPx(),
                center = Offset(cx, cy)
            )
            drawCircle(
                color = Color(0xFFFFF7D6),
                radius = 10.dp.toPx(),
                center = Offset(cx, cy)
            )
        }
    }
}
