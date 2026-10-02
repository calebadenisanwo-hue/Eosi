package com.example.meridian.ui.screens.jagi

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.meridian.data.jagi.JagiRepo
import com.example.meridian.ui.components.PixelCompanionPet
import com.example.meridian.ui.components.PixelHeart
import com.example.meridian.ui.components.bounceClick
import com.example.ui.theme.FrauncesFontFamily
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun JagiHomeScreen(
    repo: JagiRepo,
    onOpenUs: () -> Unit,
    onOpenPlay: () -> Unit,
    onOpenArena: () -> Unit,
    onOpenMemories: () -> Unit,
    onOpenLists: () -> Unit,
    onOpenSettings: () -> Unit,
    onAnswerQuestion: () -> Unit,
    onEditProfile: () -> Unit,
    modifier: Modifier = Modifier
) {
    val coupleData by repo.coupleData.collectAsState()
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    var showTouchMenu by remember { mutableStateOf(false) }
    var showCreateMenu by remember { mutableStateOf(false) }
    var showSendTouchDialog by remember { mutableStateOf(false) }
    var touchMessageInput by remember { mutableStateOf("") }

    // Dense Feature Modal Sheets
    var showRepairSheet by remember { mutableStateOf(false) }
    var showVaultSheet by remember { mutableStateOf(false) }
    var showPressPlaySheet by remember { mutableStateOf(false) }
    var showAmbientSheet by remember { mutableStateOf(false) }

    // Reaction Animation Trigger
    var activeReaction by remember { mutableStateOf<String?>(null) }
    var reactionCount by remember { mutableIntStateOf(0) }

    val reactionScale by animateFloatAsState(
        targetValue = if (activeReaction != null) 1.4f else 0.8f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow),
        label = "reactionScale"
    )

    fun sendReaction(emoji: String) {
        activeReaction = emoji
        reactionCount++
        repo.sendTouch("Sent $emoji")
        triggerHaptic(context, 70)
        scope.launch {
            delay(1200)
            activeReaction = null
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFFAF5EE))
            .testTag("jagi_home_screen")
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(top = 16.dp, bottom = 120.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // TOP HEADER: Couple Names, Live Distance, Time & Settings
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier
                            .bounceClick { onEditProfile() }
                            .testTag("home_header_profile")
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "${coupleData.userName} & ${coupleData.partnerName}",
                                fontFamily = FrauncesFontFamily,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF2C221E)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Edit Names",
                                tint = Color(0xFFA8998D),
                                modifier = Modifier.size(16.dp)
                            )
                        }

                        Text(
                            text = "${coupleData.userCity} ⇄ ${coupleData.partnerCity} · ${coupleData.distanceKm} ${coupleData.distanceUnit}",
                            fontSize = 12.sp,
                            color = Color(0xFF8E8076)
                        )
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Settings Gear
                        IconButton(
                            onClick = onOpenSettings,
                            modifier = Modifier
                                .size(40.dp)
                                .bounceClick()
                                .testTag("home_settings_button")
                        ) {
                            Text("⚙️", fontSize = 18.sp)
                        }

                        // Partner Avatar (opens Us)
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .border(1.5.dp, Color(0xFFE6DCCE), CircleShape)
                                .background(Color(0xFF385E50))
                                .bounceClick { onOpenUs() }
                                .testTag("profile_avatar_partner"),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("👩🏾", fontSize = 22.sp)
                        }
                    }
                }
            }

            // HERO CARD: Partner Status, Touch, and Distance
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(elevation = 2.dp, shape = RoundedCornerShape(24.dp), spotColor = Color(0x1A000000))
                        .testTag("hero_touch_card"),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFDF8)),
                    shape = RoundedCornerShape(24.dp),
                    border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(Color(0xFFF0E7DA)))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "${coupleData.partnerName.uppercase()}'S STATUS · CONNECTED",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFA8998D),
                                letterSpacing = 1.2.sp
                            )
                            Text(
                                text = "${coupleData.daysTogether} Days Together",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFE27B58)
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = buildAnnotatedString {
                                append("Thinking of you from ")
                                withStyle(SpanStyle(fontStyle = FontStyle.Italic)) {
                                    append(coupleData.partnerCity)
                                }
                            },
                            fontFamily = FrauncesFontFamily,
                            fontSize = 24.sp,
                            color = Color(0xFF2C221E),
                            lineHeight = 30.sp
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "“${coupleData.partnerMood}” · Battery: ${coupleData.partnerBatteryPercent}%",
                            fontSize = 13.sp,
                            color = Color(0xFF8E8076)
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Touch Button & Fast Reaction
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Button(
                                onClick = { sendReaction("💖") },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(46.dp)
                                    .bounceClick(),
                                shape = RoundedCornerShape(14.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2C221E))
                            ) {
                                Text("Send Touch 💓", color = Color(0xFFFFFDF8), fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                            }

                            Button(
                                onClick = { showSendTouchDialog = true },
                                modifier = Modifier
                                    .height(46.dp)
                                    .bounceClick(),
                                shape = RoundedCornerShape(14.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF7F0E6))
                            ) {
                                Text("Whisper Note 💌", color = Color(0xFF2C221E), fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                            }
                        }
                    }
                }
            }

            // 2-PLAYER FULLSCREEN GAME ARENA HERO SHORTCUT
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .bounceClick { onOpenArena() }
                        .testTag("home_arena_shortcut"),
                    shape = RoundedCornerShape(22.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF131822))
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
                                    .size(46.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFFF8A65)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("🎮", fontSize = 22.sp)
                            }

                            Spacer(modifier = Modifier.width(14.dp))

                            Column {
                                Text(
                                    text = "2-Player Game Arena",
                                    fontFamily = FrauncesFontFamily,
                                    fontSize = 17.sp,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Play simultaneous games on one screen",
                                    fontSize = 12.sp,
                                    color = Color.White.copy(alpha = 0.7f)
                                )
                            }
                        }

                        Text("PLAY →", color = Color(0xFFFF9E80), fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                }
            }

            // TODAY'S QUESTION CARD
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .bounceClick { onAnswerQuestion() }
                        .testTag("today_question_card"),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFDF8)),
                    shape = RoundedCornerShape(24.dp),
                    border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(Color(0xFFF0E7DA)))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Column(modifier = Modifier.weight(1f).padding(end = 12.dp)) {
                            Text(
                                text = "TODAY'S DAILY QUESTION",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFA8998D),
                                letterSpacing = 1.2.sp
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = coupleData.todayQuestion,
                                fontFamily = FrauncesFontFamily,
                                fontSize = 18.sp,
                                color = Color(0xFF2C221E),
                                lineHeight = 24.sp
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = if (coupleData.isUserAnswerSealed) "Answer sealed · Tap to review ✨" else "Tap to answer & seal for partner →",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFFE27B58)
                            )
                        }

                        // Streak flame badge
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .clip(RoundedCornerShape(16.dp))
                                .background(Color(0xFFFDF4EB))
                                .padding(horizontal = 12.dp, vertical = 8.dp)
                        ) {
                            Text("🔥", fontSize = 20.sp)
                            Text(
                                text = "${coupleData.questionStreakDays}",
                                fontFamily = FrauncesFontFamily,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF2C221E)
                            )
                            Text("DAYS", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = Color(0xFFA8998D))
                        }
                    }
                }
            }

            // DENSE COUPLE TOOLS ROW (Pause & Repair, Vault, Watch Sync, Soundscapes)
            item {
                Text(
                    text = "LONG-DISTANCE TOOLS",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFA8998D),
                    letterSpacing = 1.2.sp
                )
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Tool 1: Pause & Repair
                    ToolCard(
                        modifier = Modifier.weight(1f),
                        emoji = "🕊️",
                        title = "Repair",
                        subtitle = "DE-ESCALATE",
                        onClick = { showRepairSheet = true }
                    )

                    // Tool 2: Encrypted Vault
                    ToolCard(
                        modifier = Modifier.weight(1f),
                        emoji = "🔐",
                        title = "Vault",
                        subtitle = "SECRET NOTES",
                        onClick = { showVaultSheet = true }
                    )

                    // Tool 3: Press Play Watch Sync
                    ToolCard(
                        modifier = Modifier.weight(1f),
                        emoji = "🎬",
                        title = "Watch Sync",
                        subtitle = "3-2-1 PLAY",
                        onClick = { showPressPlaySheet = true }
                    )

                    // Tool 4: Shared Soundscapes
                    ToolCard(
                        modifier = Modifier.weight(1f),
                        emoji = "🎧",
                        title = "Ambience",
                        subtitle = "RAIN & CAFE",
                        onClick = { showAmbientSheet = true }
                    )
                }
            }

            // 4 NAVIGATION CARDS (Play, Memories, Lists, Us Profile)
            item {
                Text(
                    text = "BOND & SPACES",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFA8998D),
                    letterSpacing = 1.2.sp
                )
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    ActionGridCard(
                        modifier = Modifier.weight(1f),
                        emoji = "🎯",
                        title = "Play",
                        subtitle = "5 MODES",
                        testTag = "home_action_play",
                        onClick = onOpenPlay
                    )

                    ActionGridCard(
                        modifier = Modifier.weight(1f),
                        emoji = "🖼️",
                        title = "Memories",
                        subtitle = "ALBUM",
                        testTag = "home_action_memories",
                        onClick = onOpenMemories
                    )

                    ActionGridCard(
                        modifier = Modifier.weight(1f),
                        emoji = "📋",
                        title = "Lists",
                        subtitle = "BUCKET LIST",
                        testTag = "home_action_lists",
                        onClick = onOpenLists
                    )

                    ActionGridCard(
                        modifier = Modifier.weight(1f),
                        emoji = "🌿",
                        title = "Us & Pet",
                        subtitle = "LVL ${coupleData.companionLevel}",
                        testTag = "home_action_us",
                        onClick = onOpenUs
                    )
                }
            }
        }

        // FLOATING ACTION DOCK
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 24.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier
                    .clip(RoundedCornerShape(32.dp))
                    .background(Color(0xFF2C221E))
                    .padding(horizontal = 14.dp, vertical = 8.dp)
            ) {
                // Reaction Chips
                listOf("💖", "💋", "🫂", "✨", "☕").forEach { emoji ->
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .bounceClick { sendReaction(emoji) },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(emoji, fontSize = 20.sp)
                    }
                }

                // Add / Menu Button
                IconButton(
                    onClick = { showCreateMenu = true },
                    modifier = Modifier
                        .size(38.dp)
                        .background(Color.White.copy(alpha = 0.15f), CircleShape)
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add", tint = Color.White)
                }
            }
        }

        // FLOATING ANIMATED REACTION BURST
        if (activeReaction != null) {
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .scale(reactionScale)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.9f))
                    .padding(24.dp)
            ) {
                Text(activeReaction ?: "💖", fontSize = 56.sp)
            }
        }

        // CREATE MENU DIALOG
        if (showCreateMenu) {
            AlertDialog(
                onDismissRequest = { showCreateMenu = false },
                title = { Text("Couple Actions", fontWeight = FontWeight.Bold) },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        ListItemButton("💌 Whisper a Love Note", "Send to partner's home") {
                            showCreateMenu = false
                            showSendTouchDialog = true
                        }
                        ListItemButton("🕊️ Pause & Repair Protocol", "De-escalate with care") {
                            showCreateMenu = false
                            showRepairSheet = true
                        }
                        ListItemButton("🎬 Press Play Sync", "Watch movies in sync") {
                            showCreateMenu = false
                            showPressPlaySheet = true
                        }
                        ListItemButton("✏️ Customize Couple Names & Cities", "Change profile details") {
                            showCreateMenu = false
                            onEditProfile()
                        }
                    }
                },
                confirmButton = {
                    TextButton(onClick = { showCreateMenu = false }) { Text("Close") }
                }
            )
        }

        // SEND TOUCH DIALOG
        if (showSendTouchDialog) {
            AlertDialog(
                onDismissRequest = { showSendTouchDialog = false },
                title = { Text("Whisper Note to ${coupleData.partnerName}") },
                text = {
                    OutlinedTextField(
                        value = touchMessageInput,
                        onValueChange = { touchMessageInput = it },
                        placeholder = { Text("e.g. Thinking of you while making tea...") },
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                confirmButton = {
                    Button(
                        onClick = {
                            if (touchMessageInput.isNotBlank()) {
                                repo.sendTouch(touchMessageInput)
                                touchMessageInput = ""
                                triggerHaptic(context, 100)
                            }
                            showSendTouchDialog = false
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2C221E))
                    ) {
                        Text("Send Note 💌", color = Color.White)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showSendTouchDialog = false }) { Text("Cancel") }
                }
            )
        }

        // Dense Modal Sheets
        if (showRepairSheet) {
            JagiRepairModalSheet(repo = repo, onDismiss = { showRepairSheet = false })
        }

        if (showVaultSheet) {
            JagiVaultModalSheet(repo = repo, onDismiss = { showVaultSheet = false })
        }

        if (showPressPlaySheet) {
            JagiPressPlayModalSheet(onDismiss = { showPressPlaySheet = false })
        }

        if (showAmbientSheet) {
            JagiAmbientModalSheet(repo = repo, onDismiss = { showAmbientSheet = false })
        }
    }
}

@Composable
private fun ActionGridCard(
    modifier: Modifier = Modifier,
    emoji: String,
    title: String,
    subtitle: String,
    testTag: String,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .bounceClick { onClick() }
            .testTag(testTag),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFDF8)),
        shape = RoundedCornerShape(18.dp),
        border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(Color(0xFFF0E7DA)))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(emoji, fontSize = 24.sp)
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = title,
                fontFamily = FrauncesFontFamily,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF2C221E)
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = subtitle,
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFA8998D),
                letterSpacing = 0.5.sp
            )
        }
    }
}

@Composable
private fun ToolCard(
    modifier: Modifier = Modifier,
    emoji: String,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .bounceClick { onClick() },
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFDF8)),
        shape = RoundedCornerShape(18.dp),
        border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(Color(0xFFF0E7DA)))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(emoji, fontSize = 22.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = title,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2C221E)
            )
            Text(
                text = subtitle,
                fontSize = 8.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFA8998D)
            )
        }
    }
}

@Composable
private fun ListItemButton(
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFFF7F0E6))
            .clickable(onClick = onClick)
            .padding(12.dp)
    ) {
        Column {
            Text(title, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color(0xFF2C221E))
            Text(subtitle, fontSize = 11.sp, color = Color(0xFF8E8076))
        }
    }
}

private fun triggerHaptic(context: Context, durationMs: Long) {
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
