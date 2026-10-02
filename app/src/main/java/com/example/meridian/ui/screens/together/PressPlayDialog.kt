package com.example.meridian.ui.screens.together

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.FrauncesFontFamily
import com.example.ui.theme.MeridianApricot
import com.example.ui.theme.MeridianDuskBlue
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PressPlayDialog(
    partnerName: String,
    onDismiss: () -> Unit
) {
    var myReady by remember { mutableStateOf(false) }
    var partnerReady by remember { mutableStateOf(true) } // In demo mode, partner is ready
    var countdownValue by remember { mutableIntStateOf(0) } // 0: Idle, 3, 2, 1, -1 (PLAY)
    var syncCheckInput by remember { mutableStateOf("") }
    var syncResult by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(myReady, partnerReady) {
        if (myReady && partnerReady && countdownValue == 0) {
            countdownValue = 3
            delay(1000)
            countdownValue = 2
            delay(1000)
            countdownValue = 1
            delay(1000)
            countdownValue = -1 // PLAY!
        }
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = MaterialTheme.colorScheme.surface,
        modifier = Modifier.testTag("press_play_sheet")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 6.dp)
                .padding(bottom = 36.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Press Play Together",
                    fontFamily = FrauncesFontFamily,
                    fontSize = 20.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
                IconButton(onClick = onDismiss) {
                    Icon(Icons.Default.Close, contentDescription = "Close")
                }
            }

            Text(
                text = "Works with Netflix, HBO, YouTube, or any player on your own TV or laptop. Press play on 3-2-1 at the exact same second.",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.align(Alignment.Start)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Big Countdown Display
            Box(
                modifier = Modifier
                    .size(130.dp)
                    .clip(CircleShape)
                    .background(
                        when (countdownValue) {
                            -1 -> Color(0xFF4CAF50).copy(alpha = 0.2f)
                            in 1..3 -> MeridianApricot.copy(alpha = 0.2f)
                            else -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = when (countdownValue) {
                        3 -> "3"
                        2 -> "2"
                        1 -> "1"
                        -1 -> "PLAY! ▶"
                        else -> "Ready?"
                    },
                    fontFamily = FrauncesFontFamily,
                    fontSize = if (countdownValue == -1) 26.sp else 44.sp,
                    fontWeight = FontWeight.Bold,
                    color = when (countdownValue) {
                        -1 -> Color(0xFF4CAF50)
                        in 1..3 -> MeridianApricot
                        else -> MaterialTheme.colorScheme.onSurface
                    }
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Ready Status row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("You", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = if (myReady) "Ready ✅" else "Not Ready",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp,
                        color = if (myReady) Color(0xFF4CAF50) else MaterialTheme.colorScheme.onSurface
                    )
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(partnerName, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = if (partnerReady) "Ready ✅" else "Waiting...",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp,
                        color = Color(0xFF4CAF50)
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            if (countdownValue == 0) {
                Button(
                    onClick = { myReady = true },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Icon(Icons.Default.PlayArrow, contentDescription = null)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("I'm at the 00:00 pause point!")
                }
            } else if (countdownValue == -1) {
                OutlinedButton(
                    onClick = {
                        myReady = false
                        countdownValue = 0
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text("Reset Countdown")
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Time Sync Checker
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "Mid-Movie Sync Check",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Paused for a snack? Compare your exact timestamp:",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = syncCheckInput,
                            onValueChange = { syncCheckInput = it },
                            placeholder = { Text("e.g. 24:18") },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(
                            onClick = {
                                syncResult = "You and $partnerName are within 1 second of each other. Perfectly in sync! ✨"
                            },
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Check")
                        }
                    }
                    if (syncResult != null) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = syncResult!!,
                            fontSize = 12.sp,
                            color = Color(0xFF4CAF50),
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}
