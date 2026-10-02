package com.example.meridian.ui.screens.jagi

import androidx.activity.compose.BackHandler
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.meridian.data.jagi.JagiRepo
import com.example.meridian.data.jagi.PlayDeck
import com.example.meridian.ui.components.PixelCompanionPet
import com.example.meridian.ui.components.bounceClick
import com.example.ui.theme.FrauncesFontFamily

@Composable
fun JagiPlayScreen(
    repo: JagiRepo,
    onBack: () -> Unit,
    onOpenArena: () -> Unit,
    onAnswerQuestion: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { onBack() }
    val coupleData by repo.coupleData.collectAsState()

    val games = listOf(
        PlayDeck("sync_pulse", "Sync Pulse", "Touch at the exact same second", "💓", 99, "Realtime"),
        PlayDeck("reaction_duel", "Reaction Duel", "Fastest tap wins point by point", "⚡", 5, "Duel"),
        PlayDeck("tic_tac_toe", "Hearts & Stars", "Couple turn-based board duel", "⭕", 9, "Board"),
        PlayDeck("would_you_rather", "Would You Rather?", "Simultaneous blind voting match", "💬", 25, "Match"),
        PlayDeck("question_roulette", "Deep Conversation Roulette", "Soul-deep & spicy flip cards", "🎴", 40, "Cards")
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFFAF5EE))
            .testTag("jagi_play_screen")
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(top = 16.dp, bottom = 64.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // TOP BAR
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier.size(40.dp).testTag("play_back_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color(0xFF2C221E)
                        )
                    }

                    // Level indicator
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        PixelCompanionPet(size = 20.dp, showAura = false)
                        Text(
                            text = "LVL ${coupleData.companionLevel}",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF8E8076)
                        )
                    }

                    Text(
                        text = "PLAY",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFA8998D),
                        letterSpacing = 2.sp
                    )
                }
            }

            // FULLSCREEN 2-PLAYER ARENA HERO BANNER
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .bounceClick { onOpenArena() }
                        .testTag("play_hero_arena_card"),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF131822))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Brush.verticalGradient(
                                    listOf(Color(0xFF1E2838), Color(0xFF10141D))
                                )
                            )
                            .padding(22.dp)
                    ) {
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text("🎮", fontSize = 28.sp)
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column {
                                        Text(
                                            text = "2-PLAYER FULLSCREEN ARENA",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFFFF9E80),
                                            letterSpacing = 1.2.sp
                                        )
                                        Text(
                                            text = "Play Live Together",
                                            fontFamily = FrauncesFontFamily,
                                            fontSize = 24.sp,
                                            color = Color.White,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }

                                Box(
                                    modifier = Modifier
                                        .size(46.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFFFF8A65)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.PlayArrow,
                                        contentDescription = "Start",
                                        tint = Color(0xFF1E0E08),
                                        modifier = Modifier.size(28.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            Text(
                                text = "Split-screen simultaneous games built specifically for couples: Heartbeat resonance sync, reaction showdowns, couple tic-tac-toe, and blind voting.",
                                color = Color.White.copy(alpha = 0.8f),
                                fontSize = 13.sp,
                                lineHeight = 19.sp
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                ArenaTag("💓 Dual Touch")
                                ArenaTag("⚡ Reflex Duel")
                                ArenaTag("⭕ Tic-Tac-Toe")
                                ArenaTag("💬 Blind Pick")
                            }
                        }
                    }
                }
            }

            // SECTION TITLE
            item {
                Text(
                    text = "GAMES & CONVERSATIONS",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFA8998D),
                    letterSpacing = 1.2.sp
                )
            }

            // GAME CARDS LIST
            items(games) { deck ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .bounceClick { onOpenArena() }
                        .testTag("play_deck_${deck.id}"),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFDF8)),
                    shape = RoundedCornerShape(20.dp),
                    border = CardDefaults.outlinedCardBorder().copy(
                        brush = androidx.compose.ui.graphics.SolidColor(Color(0xFFF0E7DA))
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(Color(0xFFF7F0E6)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(deck.emoji, fontSize = 24.sp)
                            }

                            Spacer(modifier = Modifier.width(14.dp))

                            Column {
                                Text(
                                    text = deck.title,
                                    fontFamily = FrauncesFontFamily,
                                    fontSize = 17.sp,
                                    color = Color(0xFF2C221E),
                                    fontWeight = FontWeight.SemiBold
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = deck.subtitle,
                                    fontSize = 12.sp,
                                    color = Color(0xFF8E8076)
                                )
                            }
                        }

                        Icon(
                            imageVector = Icons.Default.ChevronRight,
                            contentDescription = "Open",
                            tint = Color(0xFFA8998D)
                        )
                    }
                }
            }

            // DAILY QUESTION PROMPT CARD
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .bounceClick { onAnswerQuestion() },
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFDF8)),
                    border = CardDefaults.outlinedCardBorder().copy(
                        brush = androidx.compose.ui.graphics.SolidColor(Color(0xFFF0E7DA))
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("💌", fontSize = 28.sp)
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "Today's Daily Question",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp,
                                    color = Color(0xFF2C221E)
                                )
                                Text(
                                    text = if (coupleData.isUserAnswerSealed) "Answer sealed · Tap to review" else "Answer sealed for partner",
                                    fontSize = 12.sp,
                                    color = Color(0xFFE27B58)
                                )
                            }
                        }
                        Icon(
                            imageVector = Icons.Default.ChevronRight,
                            contentDescription = null,
                            tint = Color(0xFFA8998D)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ArenaTag(text: String) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(Color.White.copy(alpha = 0.1f))
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(text, color = Color.White.copy(alpha = 0.9f), fontSize = 11.sp, fontWeight = FontWeight.Medium)
    }
}
