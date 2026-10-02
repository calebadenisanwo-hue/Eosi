package com.example.meridian.ui.screens.jagi

import androidx.activity.compose.BackHandler
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ChevronRight
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
import com.example.meridian.data.jagi.JagiRepo
import com.example.meridian.ui.components.PixelCompanionPet
import com.example.meridian.ui.components.bounceClick
import com.example.ui.theme.FrauncesFontFamily

@Composable
fun JagiPlayScreen(
    repo: JagiRepo,
    onBack: () -> Unit,
    onAnswerQuestion: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { onBack() }
    val coupleData by repo.coupleData.collectAsState()

    var activeGameTitle by remember { mutableStateOf<String?>(null) }
    var activeGameQuestion by remember { mutableStateOf("") }
    var activeGameOptions by remember { mutableStateOf<List<String>>(emptyList()) }
    var selectedOption by remember { mutableStateOf<String?>(null) }

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
                            text = "LV.${coupleData.companionLevel}",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFA8998D)
                        )
                    }
                }
            }

            // HEADLINE: "PLAYING WITH JOY" / "Play"
            item {
                Column {
                    Text(
                        text = "PLAYING WITH ${coupleData.partnerName.uppercase()}",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFA8998D),
                        letterSpacing = 1.2.sp
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Play",
                        fontFamily = FrauncesFontFamily,
                        fontSize = 32.sp,
                        color = Color(0xFF2C221E)
                    )
                }
            }

            // TODAY'S QUESTION CARD
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFDF8)),
                    shape = RoundedCornerShape(24.dp),
                    border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(Color(0xFFF0E7DA)))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                    ) {
                        Text(
                            text = "TODAY'S QUESTION",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFA8998D),
                            letterSpacing = 1.2.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = coupleData.todayQuestion,
                            fontFamily = FrauncesFontFamily,
                            fontSize = 20.sp,
                            color = Color(0xFF2C221E),
                            lineHeight = 26.sp
                        )
                        Spacer(modifier = Modifier.height(16.dp))

                        // Inner bar with Answer button
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(20.dp))
                                .background(Color(0xFFFAF2EB))
                                .padding(horizontal = 14.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("✉️", fontSize = 18.sp)
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = if (coupleData.isUserAnswerSealed) "Answer sealed" else "Nobody's answered yet",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = Color(0xFF2C221E)
                                    )
                                    Text(
                                        text = if (coupleData.isUserAnswerSealed) "Waiting for Joy" else "Both of you still to answer.",
                                        fontSize = 11.sp,
                                        color = Color(0xFF8E8076)
                                    )
                                }
                            }

                            Button(
                                onClick = onAnswerQuestion,
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF7C5A0)),
                                shape = RoundedCornerShape(16.dp),
                                contentPadding = PaddingValues(horizontal = 18.dp, vertical = 8.dp)
                            ) {
                                Text(
                                    text = if (coupleData.isUserAnswerSealed) "View" else "Answer",
                                    color = Color(0xFF332014),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                )
                            }
                        }
                    }
                }
            }

            // FIVE WAYS TO PLAY
            item {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "FIVE WAYS TO PLAY",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFA8998D),
                        letterSpacing = 1.2.sp
                    )
                    Text(
                        text = "🌐 PLAYED TODAY · BACK IN 1H 39M PLAY TONIGHT",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFA8998D),
                        letterSpacing = 0.5.sp
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    // 2x2 Grid of Decks
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        PlayDeckCard(
                            modifier = Modifier.weight(1f),
                            emoji = "🎯",
                            title = "Guess her answer",
                            subtitle = "How well you know her",
                            decks = "13 DECKS",
                            onClick = {
                                activeGameTitle = "Guess her answer 🎯"
                                activeGameQuestion = "What is Joy's go-to comfort meal when she's stressed?"
                                activeGameOptions = listOf("Ramen noodles 🍜", "Warm soup & bread 🍲", "Chocolate croissant 🥐", "Spicy takeout 🌶️")
                            }
                        )

                        PlayDeckCard(
                            modifier = Modifier.weight(1f),
                            emoji = "🔗",
                            title = "In sync",
                            subtitle = "You both answer",
                            decks = "13 DECKS",
                            onClick = {
                                activeGameTitle = "In sync 🔗"
                                activeGameQuestion = "Pick the perfect Saturday morning together:"
                                activeGameOptions = listOf("Sleep in till 11 AM 😴", "Farmers market stroll 🍓", "Coffee & vinyl in bed ☕", "Scenic road trip 🚗")
                            }
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        PlayDeckCard(
                            modifier = Modifier.weight(1f),
                            emoji = "🎲",
                            title = "Who's more likely?",
                            subtitle = "Agree and score",
                            decks = "11 DECKS",
                            onClick = {
                                activeGameTitle = "Who's more likely? 🎲"
                                activeGameQuestion = "Who is more likely to book spontaneous flights at 2 AM?"
                                activeGameOptions = listOf("Kola 🙋🏾‍♂️", "Joy 🙋🏾‍♀️", "Both equally! 💕")
                            }
                        )

                        PlayDeckCard(
                            modifier = Modifier.weight(1f),
                            emoji = "⚡",
                            title = "This or That",
                            subtitle = "Quick picks",
                            decks = "14 DECKS",
                            onClick = {
                                activeGameTitle = "This or That ⚡"
                                activeGameQuestion = "Long FaceTime call with dinner OR falling asleep on audio call?"
                                activeGameOptions = listOf("FaceTime with dinner 🍽️", "Falling asleep on audio 🌙")
                            }
                        )
                    }

                    // Deep Dive Card
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .bounceClick {
                                activeGameTitle = "Deep dive 🌊"
                                activeGameQuestion = "What was the exact moment you realized you were in love with them?"
                                activeGameOptions = listOf("Type a heartfelt note ✍️")
                            },
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFDF8)),
                        shape = RoundedCornerShape(22.dp),
                        border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(Color(0xFFF0E7DA)))
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(18.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(42.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(Color(0xFFFAF2EB)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text("🌊", fontSize = 22.sp)
                                }
                                Spacer(modifier = Modifier.width(14.dp))
                                Column {
                                    Text(
                                        text = "Deep dive",
                                        fontFamily = FrauncesFontFamily,
                                        fontSize = 17.sp,
                                        color = Color(0xFF2C221E)
                                    )
                                    Text(
                                        text = "Three questions, real answers",
                                        fontSize = 12.sp,
                                        color = Color(0xFF8E8076)
                                    )
                                }
                            }
                            Text(
                                text = "6 DECKS",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFA8998D)
                            )
                        }
                    }

                    // The Journal Card
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .bounceClick { /* Opens Journal */ },
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFDF8)),
                        shape = RoundedCornerShape(22.dp),
                        border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(Color(0xFFF0E7DA)))
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(18.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(
                                    text = "The journal",
                                    fontFamily = FrauncesFontFamily,
                                    fontSize = 16.sp,
                                    color = Color(0xFF2C221E)
                                )
                                Text(
                                    text = "Every answer you've unsealed together.",
                                    fontSize = 12.sp,
                                    color = Color(0xFF8E8076)
                                )
                            }
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = if (coupleData.isUserAnswerSealed) "1" else "0",
                                    fontFamily = FrauncesFontFamily,
                                    fontSize = 18.sp,
                                    color = Color(0xFFD77A61)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Icon(Icons.Default.ChevronRight, contentDescription = null, tint = Color(0xFFC0B3A6))
                            }
                        }
                    }
                }
            }
        }

        // PLAY GAME MODAL DIALOG
        if (activeGameTitle != null) {
            AlertDialog(
                onDismissRequest = {
                    activeGameTitle = null
                    selectedOption = null
                },
                containerColor = Color(0xFFFFFDF8),
                shape = RoundedCornerShape(24.dp),
                title = {
                    Text(
                        text = activeGameTitle ?: "",
                        fontFamily = FrauncesFontFamily,
                        fontSize = 20.sp,
                        color = Color(0xFF2C221E)
                    )
                },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text(
                            text = activeGameQuestion,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF2C221E),
                            lineHeight = 22.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        activeGameOptions.forEach { opt ->
                            val isSelected = selectedOption == opt
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .bounceClick { selectedOption = opt },
                                shape = RoundedCornerShape(14.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = if (isSelected) Color(0xFFF7C5A0) else Color(0xFFFBF8F2)
                                ),
                                border = CardDefaults.outlinedCardBorder().copy(
                                    brush = androidx.compose.ui.graphics.SolidColor(
                                        if (isSelected) Color(0xFFE27B58) else Color(0xFFF0E7DA)
                                    )
                                )
                            ) {
                                Text(
                                    text = opt,
                                    fontSize = 14.sp,
                                    color = Color(0xFF2C221E),
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    modifier = Modifier.padding(14.dp)
                                )
                            }
                        }
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            repo.addMemory("Played $activeGameTitle", isVoice = false)
                            activeGameTitle = null
                            selectedOption = null
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF7C5A0)),
                        shape = RoundedCornerShape(16.dp),
                        enabled = selectedOption != null || activeGameOptions.size == 1
                    ) {
                        Text("Submit Answer (+5 XP)", color = Color(0xFF332014), fontWeight = FontWeight.Bold)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { activeGameTitle = null }) {
                        Text("Close", color = Color(0xFF8E8076))
                    }
                }
            )
        }
    }
}

@Composable
private fun PlayDeckCard(
    emoji: String,
    title: String,
    subtitle: String,
    decks: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .bounceClick { onClick() },
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFDF8)),
        shape = RoundedCornerShape(22.dp),
        border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(Color(0xFFF0E7DA)))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFFAF2EB)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(emoji, fontSize = 20.sp)
                }

                Text(
                    text = decks,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFA8998D),
                    letterSpacing = 0.5.sp
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = title,
                fontFamily = FrauncesFontFamily,
                fontSize = 15.sp,
                color = Color(0xFF2C221E),
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = subtitle,
                fontSize = 11.sp,
                color = Color(0xFF8E8076)
            )
        }
    }
}
