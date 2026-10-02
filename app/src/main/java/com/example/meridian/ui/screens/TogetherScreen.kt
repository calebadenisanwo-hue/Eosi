package com.example.meridian.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.meridian.data.repository.MeridianRepo
import com.example.meridian.ui.screens.together.AmbientSoundCard
import com.example.meridian.ui.screens.together.AsyncGamesSheet
import com.example.meridian.ui.screens.together.DateGeneratorSheet
import com.example.meridian.ui.screens.together.PressPlayDialog
import com.example.ui.theme.FrauncesFontFamily
import com.example.ui.theme.MeridianApricot
import com.example.ui.theme.MeridianDuskBlue

@Composable
fun TogetherScreen(
    repo: MeridianRepo,
    modifier: Modifier = Modifier
) {
    val activeUser by repo.activeUser.collectAsState()
    val partnerUser by repo.partnerUser.collectAsState()

    var showPressPlay by remember { mutableStateOf(false) }
    var showDateGenerator by remember { mutableStateOf(false) }
    var showAsyncGames by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("together_screen_scroll"),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 100.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "Together",
                fontFamily = FrauncesFontFamily,
                fontSize = 28.sp,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = "Activities, games, and synced moments across 6,480 kilometers.",
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        // Press Play Together Hero Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showPressPlay = true }
                    .testTag("press_play_card"),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f))
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "PRESS PLAY TOGETHER",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = MeridianApricot
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.PlayArrow, contentDescription = null, tint = MeridianApricot, modifier = Modifier.size(16.dp))
                            Text("Ready →", fontSize = 11.sp, color = MeridianApricot, fontWeight = FontWeight.Bold)
                        }
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Synchronized 3-2-1 Movie & Show Countdown",
                        fontFamily = FrauncesFontFamily,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Watch simultaneously on Netflix, HBO, or YouTube. Both tap ready, countdown fires at the exact same millisecond, and mid-movie sync check keeps you aligned.",
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        lineHeight = 18.sp
                    )
                }
            }
        }

        // Date Generator Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showDateGenerator = true }
                    .testTag("date_generator_card"),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f))
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Date Generator",
                            fontFamily = FrauncesFontFamily,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text("40 Ideas →", fontSize = 11.sp, color = MeridianDuskBlue, fontWeight = FontWeight.Medium)
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Filter by energy, cost, and time required. From 15-minute blind portrait drawing to cooking the same recipe on speaker.",
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        // Async Turn-Based Games Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showAsyncGames = true }
                    .testTag("async_games_card"),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f))
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Async Turn-Based Games",
                            fontFamily = FrauncesFontFamily,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text("Play →", fontSize = 11.sp, color = MeridianApricot, fontWeight = FontWeight.Medium)
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Two Truths & A Lie, Rapid-Fire This or That, and One-Sentence Story Builder. Play on your own time across offsets.",
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        // Ambient Soundscapes Player
        item {
            AmbientSoundCard()
        }
    }

    if (showPressPlay) {
        PressPlayDialog(
            partnerName = partnerUser.displayName,
            onDismiss = { showPressPlay = false }
        )
    }

    if (showDateGenerator) {
        DateGeneratorSheet(
            onLogToTimeline = { dateTitle ->
                // Logs date activity to timeline
            },
            onDismiss = { showDateGenerator = false }
        )
    }

    if (showAsyncGames) {
        AsyncGamesSheet(
            partnerName = partnerUser.displayName,
            onDismiss = { showAsyncGames = false }
        )
    }
}
