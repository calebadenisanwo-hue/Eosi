package com.example.meridian.ui.screens.jagi

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.meridian.data.jagi.JagiRepo
import com.example.meridian.ui.components.bounceClick
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

enum class ArenaGameMode {
    SYNC_PULSE,
    REACTION_DUEL,
    TIC_TAC_TOE,
    WOULD_YOU_RATHER,
    QUESTION_ROULETTE
}

@Composable
fun JagiFullscreenGameArena(
    repo: JagiRepo,
    onClose: () -> Unit
) {
    val coupleData by repo.coupleData.collectAsState()
    var selectedMode by remember { mutableStateOf(ArenaGameMode.SYNC_PULSE) }
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0C0F14))
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Top Bar with Exit and Game Mode Tabs
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(
                    onClick = onClose,
                    modifier = Modifier
                        .size(42.dp)
                        .background(Color.White.copy(alpha = 0.1f), CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Exit Arena",
                        tint = Color.White
                    )
                }

                Text(
                    text = "Couple Arena",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )

                // Quick Mode Selector Icon
                IconButton(
                    onClick = {
                        val all = ArenaGameMode.entries
                        val nextIdx = (all.indexOf(selectedMode) + 1) % all.size
                        selectedMode = all[nextIdx]
                    },
                    modifier = Modifier
                        .size(42.dp)
                        .background(Color.White.copy(alpha = 0.1f), CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.SportsEsports,
                        contentDescription = "Switch Game",
                        tint = Color(0xFFFF9E80)
                    )
                }
            }

            // Mode Selector Chips
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                GameModeChip("💓 Sync", selectedMode == ArenaGameMode.SYNC_PULSE) { selectedMode = ArenaGameMode.SYNC_PULSE }
                GameModeChip("⚡ Duel", selectedMode == ArenaGameMode.REACTION_DUEL) { selectedMode = ArenaGameMode.REACTION_DUEL }
                GameModeChip("⭕ Board", selectedMode == ArenaGameMode.TIC_TAC_TOE) { selectedMode = ArenaGameMode.TIC_TAC_TOE }
                GameModeChip("💬 Pick", selectedMode == ArenaGameMode.WOULD_YOU_RATHER) { selectedMode = ArenaGameMode.WOULD_YOU_RATHER }
                GameModeChip("🎴 Cards", selectedMode == ArenaGameMode.QUESTION_ROULETTE) { selectedMode = ArenaGameMode.QUESTION_ROULETTE }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Game Viewport
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                when (selectedMode) {
                    ArenaGameMode.SYNC_PULSE -> SyncPulseGame(
                        user1 = coupleData.userName,
                        user2 = coupleData.partnerName,
                        context = context
                    )
                    ArenaGameMode.REACTION_DUEL -> ReactionDuelGame(
                        user1 = coupleData.userName,
                        user2 = coupleData.partnerName,
                        repo = repo,
                        context = context
                    )
                    ArenaGameMode.TIC_TAC_TOE -> CoupleTicTacToeGame(
                        user1 = coupleData.userName,
                        user2 = coupleData.partnerName
                    )
                    ArenaGameMode.WOULD_YOU_RATHER -> WouldYouRatherGame(
                        user1 = coupleData.userName,
                        user2 = coupleData.partnerName
                    )
                    ArenaGameMode.QUESTION_ROULETTE -> DeepQuestionRouletteGame()
                }
            }
        }
    }
}

@Composable
private fun GameModeChip(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .bounceClick()
            .clip(RoundedCornerShape(16.dp))
            .background(if (isSelected) Color(0xFFFF8A65) else Color.White.copy(alpha = 0.08f))
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 7.dp)
    ) {
        Text(
            text = label,
            fontSize = 12.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            color = if (isSelected) Color(0xFF1E0E08) else Color.White.copy(alpha = 0.8f)
        )
    }
}

// -------------------------------------------------------------
// 1. SYNC PULSE GAME (Simultaneous Dual Touch)
// -------------------------------------------------------------
@Composable
fun SyncPulseGame(user1: String, user2: String, context: Context) {
    var p1Holding by remember { mutableStateOf(false) }
    var p2Holding by remember { mutableStateOf(false) }
    var syncDurationMillis by remember { mutableLongStateOf(0L) }

    val isInSync = p1Holding && p2Holding

    LaunchedEffect(isInSync) {
        if (isInSync) {
            val startTime = System.currentTimeMillis()
            triggerHapticPulse(context, 100)
            while (true) {
                delay(100)
                syncDurationMillis = System.currentTimeMillis() - startTime
                if (syncDurationMillis % 500 < 100) {
                    triggerHapticPulse(context, 40)
                }
            }
        } else {
            syncDurationMillis = 0L
        }
    }

    val pulseScale by animateFloatAsState(
        targetValue = if (isInSync) 1.25f else 1.0f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "pulseScale"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Player 2 (Top - oriented for facing partner)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .rotate(180f)
                .clip(RoundedCornerShape(24.dp))
                .background(if (p2Holding) Color(0xFF64B5F6).copy(alpha = 0.25f) else Color.White.copy(alpha = 0.04f))
                .border(2.dp, if (p2Holding) Color(0xFF64B5F6) else Color.White.copy(alpha = 0.15f), RoundedCornerShape(24.dp))
                .pointerInput(Unit) {
                    detectTapGestures(
                        onPress = {
                            p2Holding = true
                            tryAwaitRelease()
                            p2Holding = false
                        }
                    )
                },
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .scale(if (p2Holding) pulseScale else 1f)
                        .background(if (p2Holding) Color(0xFF64B5F6) else Color.White.copy(alpha = 0.15f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(if (p2Holding) "💙" else "👆", fontSize = 28.sp)
                }
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "$user2's Touch Pad",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = if (p2Holding) "Holding touch..." else "Press and hold here",
                    color = Color.White.copy(alpha = 0.6f),
                    fontSize = 12.sp
                )
            }
        }

        // Center Connection & Resonance Display
        Box(
            modifier = Modifier
                .padding(vertical = 16.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(
                    if (isInSync) Brush.horizontalGradient(listOf(Color(0xFFFF8A65), Color(0xFF64B5F6)))
                    else Brush.horizontalGradient(listOf(Color.White.copy(alpha = 0.08f), Color.White.copy(alpha = 0.08f)))
                )
                .padding(horizontal = 24.dp, vertical = 12.dp)
        ) {
            if (isInSync) {
                val syncSec = (syncDurationMillis / 1000.0)
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "✨ HEARTS IN RESONANCE ✨",
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = String.format("%.1fs Connected", syncSec),
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Black
                    )
                }
            } else {
                Text(
                    text = "Both touch and hold at the same time",
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 13.sp
                )
            }
        }

        // Player 1 (Bottom)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .clip(RoundedCornerShape(24.dp))
                .background(if (p1Holding) Color(0xFFFF8A65).copy(alpha = 0.25f) else Color.White.copy(alpha = 0.04f))
                .border(2.dp, if (p1Holding) Color(0xFFFF8A65) else Color.White.copy(alpha = 0.15f), RoundedCornerShape(24.dp))
                .pointerInput(Unit) {
                    detectTapGestures(
                        onPress = {
                            p1Holding = true
                            tryAwaitRelease()
                            p1Holding = false
                        }
                    )
                },
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .scale(if (p1Holding) pulseScale else 1f)
                        .background(if (p1Holding) Color(0xFFFF8A65) else Color.White.copy(alpha = 0.15f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(if (p1Holding) "🧡" else "👆", fontSize = 28.sp)
                }
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "$user1's Touch Pad",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = if (p1Holding) "Holding touch..." else "Press and hold here",
                    color = Color.White.copy(alpha = 0.6f),
                    fontSize = 12.sp
                )
            }
        }
    }
}

// -------------------------------------------------------------
// 2. REACTION DUEL GAME
// -------------------------------------------------------------
@Composable
fun ReactionDuelGame(user1: String, user2: String, repo: JagiRepo, context: Context) {
    val scores by repo.duelScore.collectAsState()
    var state by remember { mutableStateOf("WAIT") } // "WAIT", "READY", "TAP", "RESULT"
    var winner by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(state) {
        if (state == "READY") {
            val waitMs = Random.nextLong(2000, 5000)
            delay(waitMs)
            state = "TAP"
            triggerHapticPulse(context, 80)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Player 2 Zone (Top - Inverted)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .rotate(180f)
                .clip(RoundedCornerShape(20.dp))
                .background(
                    when (state) {
                        "TAP" -> Color(0xFF00E676)
                        "READY" -> Color(0xFFD50000).copy(alpha = 0.6f)
                        else -> Color(0xFF1E2430)
                    }
                )
                .clickable {
                    if (state == "TAP" && winner == null) {
                        winner = user2
                        repo.recordDuelWin(2)
                        state = "RESULT"
                        triggerHapticPulse(context, 150)
                    } else if (state == "READY") {
                        winner = "$user1 (False start!)"
                        repo.recordDuelWin(1)
                        state = "RESULT"
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = if (state == "TAP") "TAP NOW!" else if (state == "READY") "WAIT..." else "$user2 (${scores.second})",
                    fontSize = if (state == "TAP") 36.sp else 22.sp,
                    fontWeight = FontWeight.Black,
                    color = Color.White
                )
            }
        }

        // Center Control Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${scores.first} - ${scores.second}",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )

            Button(
                onClick = {
                    winner = null
                    state = "READY"
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF8A65)),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(if (state == "WAIT" || state == "RESULT") "Start Duel" else "Round Active", color = Color(0xFF1E0E08), fontWeight = FontWeight.Bold)
            }

            IconButton(onClick = { repo.resetDuelScore() }) {
                Icon(Icons.Default.Refresh, contentDescription = "Reset", tint = Color.White.copy(alpha = 0.5f))
            }
        }

        // Player 1 Zone (Bottom)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .clip(RoundedCornerShape(20.dp))
                .background(
                    when (state) {
                        "TAP" -> Color(0xFF00E676)
                        "READY" -> Color(0xFFD50000).copy(alpha = 0.6f)
                        else -> Color(0xFF1E2430)
                    }
                )
                .clickable {
                    if (state == "TAP" && winner == null) {
                        winner = user1
                        repo.recordDuelWin(1)
                        state = "RESULT"
                        triggerHapticPulse(context, 150)
                    } else if (state == "READY") {
                        winner = "$user2 (False start!)"
                        repo.recordDuelWin(2)
                        state = "RESULT"
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = if (state == "TAP") "TAP NOW!" else if (state == "READY") "WAIT..." else "$user1 (${scores.first})",
                    fontSize = if (state == "TAP") 36.sp else 22.sp,
                    fontWeight = FontWeight.Black,
                    color = Color.White
                )
            }
        }
    }
}

// -------------------------------------------------------------
// 3. COUPLE TIC-TAC-TOE (Hearts vs Stars)
// -------------------------------------------------------------
@Composable
fun CoupleTicTacToeGame(user1: String, user2: String) {
    var board by remember { mutableStateOf(List(9) { "" }) }
    var isHeartTurn by remember { mutableStateOf(true) }
    var winner by remember { mutableStateOf<String?>(null) }

    fun checkWin(b: List<String>): String? {
        val lines = listOf(
            listOf(0, 1, 2), listOf(3, 4, 5), listOf(6, 7, 8),
            listOf(0, 3, 6), listOf(1, 4, 7), listOf(2, 5, 8),
            listOf(0, 4, 8), listOf(2, 4, 6)
        )
        for (line in lines) {
            val (a, bIdx, c) = line
            if (b[a].isNotEmpty() && b[a] == b[bIdx] && b[a] == b[c]) return b[a]
        }
        if (b.all { it.isNotEmpty() }) return "Tie"
        return null
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Status Card
        Text(
            text = when (winner) {
                "💖" -> "$user1 (Hearts) Wins! 🎉"
                "⭐" -> "$user2 (Stars) Wins! 🎉"
                "Tie" -> "It's a Tie! 🤝"
                else -> if (isHeartTurn) "$user1's Turn (💖)" else "$user2's Turn (⭐)"
            },
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(24.dp))

        // 3x3 Grid
        Column(
            modifier = Modifier
                .size(310.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(Color.White.copy(alpha = 0.05f))
                .padding(12.dp),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            for (row in 0..2) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    for (col in 0..2) {
                        val index = row * 3 + col
                        Box(
                            modifier = Modifier
                                .size(84.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(Color.White.copy(alpha = 0.08f))
                                .clickable {
                                    if (board[index].isEmpty() && winner == null) {
                                        val newBoard = board.toMutableList().apply {
                                            this[index] = if (isHeartTurn) "💖" else "⭐"
                                        }
                                        board = newBoard
                                        isHeartTurn = !isHeartTurn
                                        winner = checkWin(newBoard)
                                    }
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = board[index],
                                fontSize = 36.sp
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        Button(
            onClick = {
                board = List(9) { "" }
                winner = null
                isHeartTurn = true
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color.White.copy(alpha = 0.15f)),
            shape = RoundedCornerShape(16.dp)
        ) {
            Icon(Icons.Default.Refresh, contentDescription = null, tint = Color.White)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Reset Board", color = Color.White)
        }
    }
}

// -------------------------------------------------------------
// 4. WOULD YOU RATHER (Simultaneous Blind Pick)
// -------------------------------------------------------------
@Composable
fun WouldYouRatherGame(user1: String, user2: String) {
    val prompts = listOf(
        Pair("1-month spontaneous road trip living in a van", "1-week ultra luxury resort in Bali"),
        Pair("Never have time zones and live together in a cozy studio", "Keep distance for 1 more year but get your dream house"),
        Pair("Cook an elaborate gourmet dinner together every night", "Have your own personal private chef cook for you both"),
        Pair("Teleport to each other for 10 minutes every day", "Spend 1 full month together every 6 months"),
        Pair("Know the exact future of our next 10 years", "Be completely surprised by our adventures together")
    )
    var promptIdx by remember { mutableIntStateOf(0) }
    var p1Pick by remember { mutableStateOf<Int?>(null) }
    var p2Pick by remember { mutableStateOf<Int?>(null) }

    val currentPrompt = prompts[promptIdx % prompts.size]
    val bothVoted = p1Pick != null && p2Pick != null

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Player 2 Side (Top Inverted)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .rotate(180f)
                .clip(RoundedCornerShape(20.dp))
                .background(Color.White.copy(alpha = 0.05f))
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Text("$user2's Vote", color = Color.White.copy(alpha = 0.6f), fontSize = 12.sp)
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                VoteCard(
                    text = currentPrompt.first,
                    isSelected = p2Pick == 1,
                    isRevealed = bothVoted,
                    modifier = Modifier.weight(1f)
                ) { if (!bothVoted) p2Pick = 1 }
                VoteCard(
                    text = currentPrompt.second,
                    isSelected = p2Pick == 2,
                    isRevealed = bothVoted,
                    modifier = Modifier.weight(1f)
                ) { if (!bothVoted) p2Pick = 2 }
            }
        }

        // Center Status / Next
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (bothVoted) {
                val matched = p1Pick == p2Pick
                Text(
                    text = if (matched) "🎉 100% MATCH! You chose the same!" else "💫 OPPOSITES ATTRACT!",
                    color = if (matched) Color(0xFF00E676) else Color(0xFFFF9E80),
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(6.dp))
                Button(
                    onClick = {
                        p1Pick = null
                        p2Pick = null
                        promptIdx++
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF8A65)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Next Dilemma →", color = Color(0xFF1E0E08), fontWeight = FontWeight.Bold)
                }
            } else {
                Text(
                    text = "Both players pick an option secretly",
                    color = Color.White.copy(alpha = 0.5f),
                    fontSize = 12.sp
                )
            }
        }

        // Player 1 Side (Bottom)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .clip(RoundedCornerShape(20.dp))
                .background(Color.White.copy(alpha = 0.05f))
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Text("$user1's Vote", color = Color.White.copy(alpha = 0.6f), fontSize = 12.sp)
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                VoteCard(
                    text = currentPrompt.first,
                    isSelected = p1Pick == 1,
                    isRevealed = bothVoted,
                    modifier = Modifier.weight(1f)
                ) { if (!bothVoted) p1Pick = 1 }
                VoteCard(
                    text = currentPrompt.second,
                    isSelected = p1Pick == 2,
                    isRevealed = bothVoted,
                    modifier = Modifier.weight(1f)
                ) { if (!bothVoted) p1Pick = 2 }
            }
        }
    }
}

@Composable
private fun VoteCard(
    text: String,
    isSelected: Boolean,
    isRevealed: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxHeight()
            .clip(RoundedCornerShape(16.dp))
            .background(
                if (isSelected && isRevealed) Color(0xFFFF8A65)
                else if (isSelected) Color.White.copy(alpha = 0.2f)
                else Color.White.copy(alpha = 0.08f)
            )
            .clickable(onClick = onClick)
            .padding(12.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = if (isRevealed) text else if (isSelected) "✓ Voted" else text,
            color = if (isSelected && isRevealed) Color(0xFF1E0E08) else Color.White,
            fontSize = 13.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            textAlign = TextAlign.Center
        )
    }
}

// -------------------------------------------------------------
// 5. DEEP QUESTION ROULETTE
// -------------------------------------------------------------
@Composable
fun DeepQuestionRouletteGame() {
    val cards = listOf(
        Pair("Soul Deep", "What is something you’re currently struggling with that you haven’t told anyone else?"),
        Pair("Intimate & Soft", "When do you feel most safely held and truly understood by me?"),
        Pair("Sweet Nostalgia", "What was your very first thought the morning after we first started talking?"),
        Pair("Future Dreams", "What is one quiet morning routine you envision for us when we finally close the distance?"),
        Pair("Spicy Spark", "What is your secret favorite memory of us being alone together?")
    )
    var cardIndex by remember { mutableIntStateOf(0) }
    val currentCard = cards[cardIndex % cards.size]

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Category Badge
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(20.dp))
                .background(Color(0xFFFF8A65).copy(alpha = 0.2f))
                .padding(horizontal = 16.dp, vertical = 6.dp)
        ) {
            Text(currentCard.first, color = Color(0xFFFF8A65), fontWeight = FontWeight.Bold, fontSize = 13.sp)
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Big Question Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(260.dp),
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF181E28))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(28.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "“${currentCard.second}”",
                    color = Color.White,
                    fontSize = 20.sp,
                    lineHeight = 28.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = { cardIndex++ },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF8A65)),
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier.height(54.dp)
        ) {
            Icon(Icons.Default.Casino, contentDescription = null, tint = Color(0xFF1E0E08))
            Spacer(modifier = Modifier.width(8.dp))
            Text("Draw Next Question", color = Color(0xFF1E0E08), fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }
    }
}

private fun triggerHapticPulse(context: Context, durationMs: Long) {
    try {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
            vibratorManager?.defaultVibrator?.vibrate(
                VibrationEffect.createOneShot(durationMs, VibrationEffect.DEFAULT_AMPLITUDE)
            )
        } else {
            @Suppress("DEPRECATION")
            val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
            vibrator?.vibrate(
                VibrationEffect.createOneShot(durationMs, VibrationEffect.DEFAULT_AMPLITUDE)
            )
        }
    } catch (_: Exception) {}
}
