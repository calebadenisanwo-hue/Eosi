package com.example.meridian.ui.screens.together

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.FrauncesFontFamily
import com.example.ui.theme.MeridianApricot
import com.example.ui.theme.MeridianDuskBlue

@Composable
fun AmbientSoundCard(
    modifier: Modifier = Modifier
) {
    val soundscapes = listOf(
        "Monsoon in Pune 🌧️",
        "Gothenburg Breeze 🌊",
        "Quiet Swedish Fika ☕",
        "Night Train 🚂"
    )

    var activeTrack by remember { mutableStateOf<String?>(null) }
    var volume by remember { mutableFloatStateOf(0.7f) }

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Text(
                text = "AMBIENT SOUNDSCAPES",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = MeridianDuskBlue
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Listen to each other's atmosphere while working or falling asleep.",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(14.dp))

            soundscapes.forEach { track ->
                val isPlaying = activeTrack == track
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = track,
                        fontSize = 13.sp,
                        fontWeight = if (isPlaying) FontWeight.Bold else FontWeight.Normal,
                        color = if (isPlaying) MeridianApricot else MaterialTheme.colorScheme.onSurface
                    )
                    IconButton(
                        onClick = {
                            activeTrack = if (isPlaying) null else track
                        }
                    ) {
                        Icon(
                            if (isPlaying) Icons.Default.Stop else Icons.Default.PlayArrow,
                            contentDescription = if (isPlaying) "Stop" else "Play",
                            tint = if (isPlaying) MeridianApricot else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            if (activeTrack != null) {
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Volume", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Spacer(modifier = Modifier.width(10.dp))
                    Slider(
                        value = volume,
                        onValueChange = { volume = it },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}
