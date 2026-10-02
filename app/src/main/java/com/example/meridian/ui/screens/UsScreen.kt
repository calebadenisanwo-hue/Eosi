package com.example.meridian.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Flight
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.meridian.core.time.MeridianTime
import com.example.meridian.data.model.*
import com.example.meridian.data.repository.MeridianRepo
import com.example.meridian.ui.components.TwoWindows
import com.example.meridian.ui.screens.dailyupdate.DailyUpdateSheet
import com.example.meridian.ui.screens.letters.WriteLetterSheet
import com.example.ui.theme.FrauncesFontFamily
import com.example.ui.theme.MeridianApricot
import com.example.ui.theme.MeridianDuskBlue
import java.time.Instant

@Composable
fun UsScreen(
    repo: MeridianRepo,
    modifier: Modifier = Modifier
) {
    val activeUser by repo.activeUser.collectAsState()
    val partnerUser by repo.partnerUser.collectAsState()
    val myPresence by repo.myPresence.collectAsState()
    val partnerPresence by repo.partnerPresence.collectAsState()
    val updates by repo.updates.collectAsState()
    val visits by repo.visits.collectAsState()
    val occasions by repo.occasions.collectAsState()
    val quietReactions by repo.quietReactions.collectAsState()

    var showDailyUpdateSheet by remember { mutableStateOf(false) }
    var showWriteLetterSheet by remember { mutableStateOf(false) }

    val currentInstant = MeridianTime.now()

    // Detect DST shift
    val dstShift = remember(activeUser.timezone, partnerUser.timezone, currentInstant) {
        MeridianTime.nextDstShift(activeUser.timezone, partnerUser.timezone, currentInstant)
    }

    // Overlap calculation
    val bothAwake = remember(activeUser, partnerUser, currentInstant) {
        MeridianTime.areBothAwakeNow(
            activeUser.timezone,
            MeridianTime.AwakeHours(activeUser.awakeStart, activeUser.awakeEnd),
            partnerUser.timezone,
            MeridianTime.AwakeHours(partnerUser.awakeStart, partnerUser.awakeEnd),
            currentInstant
        )
    }

    val daysInPerson = visits.filter { it.isPast }.sumOf { it.durationDays }
    val nextVisit = visits.firstOrNull { !it.isPast }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("us_screen_scroll"),
        contentPadding = PaddingValues(top = 8.dp, bottom = 96.dp)
    ) {
        // DST Heads-up Banner if within 14 days
        if (dstShift != null && dstShift.daysUntil <= 14) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 6.dp)
                        .testTag("dst_banner"),
                    colors = CardDefaults.cardColors(containerColor = MeridianDuskBlue.copy(alpha = 0.12f)),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.Info,
                            contentDescription = "Time zone heads-up",
                            tint = MeridianDuskBlue,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = dstShift.headsUpMessage,
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurface,
                            lineHeight = 17.sp
                        )
                    }
                }
            }
        }

        // Two Windows Hero
        item {
            TwoWindows(
                myUser = activeUser,
                myPresence = myPresence,
                partnerUser = partnerUser,
                partnerPresence = partnerPresence,
                currentInstant = currentInstant,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }

        // Overlap Finder
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (bothAwake) Color(0xFF5B9E78).copy(alpha = 0.12f)
                    else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                )
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = if (bothAwake) "☀️" else "🌙", fontSize = 18.sp)
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = if (bothAwake) "You're both awake right now"
                            else "Both awake from 6:30pm to 11:00pm your time",
                            fontWeight = FontWeight.Medium,
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = if (bothAwake) "Perfect window for a call" else "Next shared window in 3h 20m",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        // Birthday Heads-up banner (12 days away for Linnea)
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MeridianApricot.copy(alpha = 0.12f))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "🎂 ${partnerUser.displayName}'s birthday is in 12 days",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Unseals at 00:00 in her timezone to wake up to.",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Button(
                        onClick = { showWriteLetterSheet = true },
                        shape = RoundedCornerShape(12.dp),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Text("Write ✉️", fontSize = 11.sp)
                    }
                }
            }
        }

        // Thinking of You & Arrived Safe Actions
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(
                    text = "Thinking of ${partnerUser.displayName}",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    QuickPingButton("🤍 Tap", onClick = { repo.sendPing("tap") }, modifier = Modifier.weight(1f))
                    QuickPingButton("🤗 Hug", onClick = { repo.sendPing("hug") }, modifier = Modifier.weight(1f))
                    QuickPingButton("💋 Kiss", onClick = { repo.sendPing("kiss") }, modifier = Modifier.weight(1f))
                    QuickPingButton("🥺 Miss you", onClick = { repo.sendPing("miss_you") }, modifier = Modifier.weight(1.2f))
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Single Deliberate "Arrived Safe" button
                OutlinedButton(
                    onClick = { repo.sendArrivedSafe() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("arrived_safe_button"),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Icon(Icons.Default.Flight, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Arrived safe (single ping, no location)",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        // Next Visit Countdown
        if (nextVisit != null) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Next reunion in ${nextVisit.place}",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "41 days",
                            fontFamily = FrauncesFontFamily,
                            fontSize = 38.sp,
                            fontWeight = FontWeight.Medium,
                            color = MeridianApricot
                        )
                        Text(
                            text = "14 days planned together · Old Pune & Monsoons",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }

        // Our Numbers (Compact Card)
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = "Our Numbers",
                        fontFamily = FrauncesFontFamily,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        StatItem(label = "Days together in person", value = "$daysInPerson days", highlight = true)
                        StatItem(label = "Days as a couple", value = "426 days")
                        StatItem(label = "Between you", value = "6,480 km")
                    }
                }
            }
        }

        // Daily Update Card: Tap to open full sheet
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .clickable { showDailyUpdateSheet = true },
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Today, in two taps",
                            fontFamily = FrauncesFontFamily,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Tap to update →",
                            fontSize = 11.sp,
                            color = MeridianApricot,
                            fontWeight = FontWeight.Medium
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))

                    val myUp = updates[activeUser.uid]
                    val partUp = updates[partnerUser.uid]

                    if (partUp != null && partUp.feelings.isNotEmpty()) {
                        Text(
                            text = "${partnerUser.displayName}: ${partUp.feelings.joinToString(" ")} · ${partUp.actions.joinToString(" ")}",
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    if (myUp != null && myUp.feelings.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "You: ${myUp.feelings.joinToString(" ")} · ${myUp.actions.joinToString(" ")}",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    } else {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Share what you're up to and feeling today.",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }

    if (showDailyUpdateSheet) {
        DailyUpdateSheet(
            myProfile = activeUser,
            partnerProfile = partnerUser,
            myUpdate = updates[activeUser.uid],
            partnerUpdate = updates[partnerUser.uid],
            quietReactions = quietReactions[partnerUser.uid] ?: emptyList(),
            onSaveUpdate = { f, fn, a, an -> repo.saveDailyUpdate(f, fn, a, an) },
            onSendReaction = { repo.sendQuietReaction(partnerUser.uid, it) },
            onOpenLetterComposer = { showWriteLetterSheet = true },
            onSendTap = { repo.sendPing("tap") },
            onDismiss = { showDailyUpdateSheet = false }
        )
    }

    if (showWriteLetterSheet) {
        WriteLetterSheet(
            partnerName = partnerUser.displayName,
            onSaveLetter = { trig, tit, bod, uns -> repo.addLetter(trig, tit, bod, uns) },
            onDismiss = { showWriteLetterSheet = false }
        )
    }
}

@Composable
private fun QuickPingButton(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    FilledTonalButton(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(14.dp),
        contentPadding = PaddingValues(horizontal = 6.dp, vertical = 8.dp)
    ) {
        Text(text = label, fontSize = 12.sp)
    }
}

@Composable
private fun StatItem(label: String, value: String, highlight: Boolean = false) {
    Column {
        Text(
            text = value,
            fontFamily = FrauncesFontFamily,
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            color = if (highlight) MeridianApricot else MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = label,
            fontSize = 10.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
