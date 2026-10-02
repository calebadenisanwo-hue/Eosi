package com.example.meridian.ui.screens.care

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Schedule
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PauseAndRepairDialog(
    partnerName: String,
    onDismiss: () -> Unit
) {
    var stage by remember { mutableIntStateOf(1) } // 1: Breathing cooldown, 2: 4 Prompts, 3: Park It

    // 4 Prompts answers
    var whatIHeard by remember { mutableStateOf("") }
    var whatIFelt by remember { mutableStateOf("") }
    var whatINeed by remember { mutableStateOf("") }
    var oneThingICanDo by remember { mutableStateOf("") }

    var isSubmitted by remember { mutableStateOf(false) }
    var isParkedConfirmed by remember { mutableStateOf(false) }

    // Breathing cycle animation: Inhale 4s, Hold 4s, Exhale 6s
    val infiniteTransition = rememberInfiniteTransition(label = "breathing")
    val breathScale by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 1.3f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 4000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "breathScale"
    )

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = MaterialTheme.colorScheme.surface,
        modifier = Modifier.testTag("pause_and_repair_sheet")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 6.dp)
                .verticalScroll(rememberScrollState())
                .padding(bottom = 36.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = when (stage) {
                        1 -> "20-Minute Cooldown"
                        2 -> "Grounding Prompts"
                        else -> "Park It"
                    },
                    fontFamily = FrauncesFontFamily,
                    fontSize = 20.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
                IconButton(onClick = onDismiss) {
                    Icon(Icons.Default.Close, contentDescription = "Close")
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            when (stage) {
                1 -> {
                    Text(
                        text = "A pause was called. When difficult words travel through text across time zones, tired minds mishear things. Take a quiet breath before responding.",
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        lineHeight = 18.sp
                    )

                    Spacer(modifier = Modifier.height(28.dp))

                    // Breathing visual
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size((120 * breathScale).dp)
                                .clip(CircleShape)
                                .background(MeridianApricot.copy(alpha = 0.2f))
                        )
                        Box(
                            modifier = Modifier
                                .size(90.dp)
                                .clip(CircleShape)
                                .background(MeridianDuskBlue.copy(alpha = 0.25f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Breathe",
                                fontFamily = FrauncesFontFamily,
                                fontSize = 16.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        text = "“It's not about winning the argument. It's about protecting the person.”",
                        fontSize = 13.sp,
                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedButton(
                            onClick = { stage = 3 },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Text("Park It for later")
                        }

                        Button(
                            onClick = { stage = 2 },
                            modifier = Modifier.weight(1.3f),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Text("Continue to Prompts →")
                        }
                    }
                }

                2 -> {
                    if (!isSubmitted) {
                        Text(
                            text = "Complete these 4 prompts privately. Neither sees the other's answer until both have answered. They auto-delete in 30 days.",
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        OutlinedTextField(
                            value = whatIHeard,
                            onValueChange = { whatIHeard = it },
                            label = { Text("1. What I heard you say") },
                            placeholder = { Text("Reflecting back what you understood...") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp),
                            maxLines = 3
                        )
                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedTextField(
                            value = whatIFelt,
                            onValueChange = { whatIFelt = it },
                            label = { Text("2. What I felt inside") },
                            placeholder = { Text("Honest emotion, without blame...") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp),
                            maxLines = 3
                        )
                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedTextField(
                            value = whatINeed,
                            onValueChange = { whatINeed = it },
                            label = { Text("3. What I need right now") },
                            placeholder = { Text("Reassurance, space, clarity, or a hug...") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp),
                            maxLines = 3
                        )
                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedTextField(
                            value = oneThingICanDo,
                            onValueChange = { oneThingICanDo = it },
                            label = { Text("4. One thing I can do to help") },
                            placeholder = { Text("A concrete small action I can take...") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp),
                            maxLines = 3
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = { isSubmitted = true },
                            enabled = whatIHeard.isNotBlank() && whatIFelt.isNotBlank(),
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Text("Submit Grounding Answers 🕊️")
                        }
                    } else {
                        // Submitted view
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(18.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f))
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = "Your responses are encrypted and sealed.",
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 14.sp
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "As soon as $partnerName submits their grounding answers, both will reveal together.",
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            OutlinedButton(
                                onClick = { stage = 3 },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(16.dp)
                            ) {
                                Text("Park It")
                            }

                            Button(
                                onClick = onDismiss,
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(16.dp)
                            ) {
                                Text("We're Okay 🤍")
                            }
                        }
                    }
                }

                3 -> {
                    // Park It view
                    Text(
                        text = "Never fight when one of you has to wake up in 4 hours. Park the discussion for the next shared awake window.",
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(containerColor = MeridianDuskBlue.copy(alpha = 0.12f))
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Schedule, contentDescription = null, tint = MeridianDuskBlue)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Suggested Window: Tomorrow 8:30pm",
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 14.sp,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "8:30pm for $partnerName · 1:00am for you (or Friday 10:00am both awake).",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            Text(
                                text = "Message sent to $partnerName:",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = MeridianDuskBlue
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "“Ayaan has parked something for tomorrow 8:30pm your time. It isn't a crisis. He just didn't want you to read it half-finished at 2am.”",
                                fontSize = 12.sp,
                                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                                color = MaterialTheme.colorScheme.onSurface,
                                lineHeight = 17.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(
                        onClick = {
                            isParkedConfirmed = true
                            onDismiss()
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text("Confirm Park It Schedule")
                    }
                }
            }
        }
    }
}
