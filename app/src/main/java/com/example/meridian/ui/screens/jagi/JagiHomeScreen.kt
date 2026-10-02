package com.example.meridian.ui.screens.jagi

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
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.graphicsLayer
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
    onOpenMemories: () -> Unit,
    onOpenLists: () -> Unit,
    onOpenSettings: () -> Unit,
    onAnswerQuestion: () -> Unit,
    modifier: Modifier = Modifier
) {
    val coupleData by repo.coupleData.collectAsState()
    val scope = rememberCoroutineScope()

    var showTouchMenu by remember { mutableStateOf(false) }
    var showCreateMenu by remember { mutableStateOf(false) }
    var showSendTouchDialog by remember { mutableStateOf(false) }
    var touchMessageInput by remember { mutableStateOf("") }
    var showMoodDialog by remember { mutableStateOf(false) }
    var showFloatingToast by remember { mutableStateOf<String?>(null) }

    // Floating heart reaction animation trigger
    var heartBurstTrigger by remember { mutableIntStateOf(0) }

    // Close popups on backdrop tap
    val isAnyMenuOpen = showTouchMenu || showCreateMenu

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
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // TOP HEADER: Kola avatar | Companion level | Joy avatar
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Kola Profile Avatar (opens Settings)
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(CircleShape)
                            .border(1.5.dp, Color(0xFFE6DCCE), CircleShape)
                            .background(Color(0xFF4A3B32))
                            .bounceClick { onOpenSettings() }
                            .testTag("profile_avatar_kola"),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("🧑🏾", fontSize = 24.sp)
                    }

                    // Center Companion Pet Widget (opens Us / Bond detail)
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(Color(0xFFFFFDF8))
                            .border(1.dp, Color(0xFFF0E7DA), RoundedCornerShape(20.dp))
                            .padding(horizontal = 10.dp, vertical = 5.dp)
                            .bounceClick { onOpenUs() }
                            .testTag("companion_status_widget"),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        PixelCompanionPet(size = 28.dp, showAura = false)
                        Column {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(
                                    text = "LV ${coupleData.companionLevel}",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF6B5E55)
                                )
                                // Tiny XP progress bar
                                Box(
                                    modifier = Modifier
                                        .width(36.dp)
                                        .height(4.dp)
                                        .clip(RoundedCornerShape(2.dp))
                                        .background(Color(0xFFEFE8DD))
                                ) {
                                    val progressFraction = (coupleData.companionXp.toFloat() / coupleData.companionMaxXp.toFloat()).coerceIn(0f, 1f)
                                    Box(
                                        modifier = Modifier
                                            .fillMaxHeight()
                                            .fillMaxWidth(progressFraction)
                                            .background(Color(0xFFE27B58))
                                    )
                                }
                            }
                        }
                    }

                    // Joy Profile Avatar (opens Us)
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(CircleShape)
                            .border(1.5.dp, Color(0xFFE6DCCE), CircleShape)
                            .background(Color(0xFF385E50))
                            .bounceClick { onOpenUs() }
                            .testTag("profile_avatar_joy"),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("👩🏾", fontSize = 24.sp)
                    }
                }
            }

            // HERO CARD: "Nothing from Joy yet"
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
                        // Kicker label
                        Text(
                            text = "JOY'S TOUCH · NOT YET",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFA8998D),
                            letterSpacing = 1.2.sp
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // Large editorial headline with italics
                        Text(
                            text = buildAnnotatedString {
                                append("Nothing from Joy ")
                                withStyle(SpanStyle(fontStyle = FontStyle.Italic)) {
                                    append("yet.")
                                }
                            },
                            fontFamily = FrauncesFontFamily,
                            fontSize = 26.sp,
                            color = Color(0xFF2C221E),
                            lineHeight = 32.sp
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "It's ${coupleData.partnerTime} where Joy is. Probably asleep.",
                            fontSize = 13.sp,
                            color = Color(0xFF8E8076)
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Mood Status Row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            // User mood pill
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(16.dp))
                                    .border(1.dp, Color(0xFFDACFBF), RoundedCornerShape(16.dp))
                                    .background(Color(0xFFFBF8F2))
                                    .bounceClick { showMoodDialog = true }
                                    .padding(vertical = 12.dp, horizontal = 12.dp),
                                contentAlignment = Alignment.CenterStart
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = if (coupleData.userMood != null) "✨" else "+",
                                        fontSize = 14.sp,
                                        color = Color(0xFF8E8076),
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Column {
                                        Text(
                                            text = "YOU",
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFFA8998D),
                                            letterSpacing = 0.5.sp
                                        )
                                        Text(
                                            text = coupleData.userMood ?: "How are you today?",
                                            fontSize = 12.sp,
                                            color = if (coupleData.userMood != null) Color(0xFF2C221E) else Color(0xFF9E8E84),
                                            fontWeight = FontWeight.Medium
                                        )
                                    }
                                }
                            }

                            // Partner mood pill
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(16.dp))
                                    .border(1.dp, Color(0xFFDACFBF), RoundedCornerShape(16.dp))
                                    .background(Color(0xFFFBF8F2))
                                    .padding(vertical = 12.dp, horizontal = 12.dp),
                                contentAlignment = Alignment.CenterStart
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text("🌙", fontSize = 14.sp)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Column {
                                        Text(
                                            text = "JOY",
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFFA8998D),
                                            letterSpacing = 0.5.sp
                                        )
                                        Text(
                                            text = coupleData.partnerMood ?: "no mood yet",
                                            fontSize = 12.sp,
                                            color = Color(0xFF9E8E84)
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(18.dp))

                        // Dashed Time & Distance Line
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(horizontalAlignment = Alignment.Start) {
                                Text("YOU", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = Color(0xFFA8998D))
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text("🌙 ", fontSize = 12.sp)
                                    Text(
                                        text = coupleData.userTime,
                                        fontFamily = FrauncesFontFamily,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = Color(0xFF2C221E)
                                    )
                                }
                            }

                            // Center connecting dashed line with pixel heart
                            Row(
                                modifier = Modifier.weight(1f).padding(horizontal = 8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                DashedLine(modifier = Modifier.weight(1f))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("💖", fontSize = 14.sp)
                                Spacer(modifier = Modifier.width(6.dp))
                                DashedLine(modifier = Modifier.weight(1f))
                            }

                            Column(horizontalAlignment = Alignment.End) {
                                Text("JOY", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = Color(0xFFA8998D))
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = coupleData.partnerTime,
                                        fontFamily = FrauncesFontFamily,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = Color(0xFF2C221E)
                                    )
                                    Text(" 🌙", fontSize = 12.sp)
                                }
                            }
                        }
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
                                text = "TODAY'S QUESTION",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFA8998D),
                                letterSpacing = 1.2.sp
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = coupleData.todayQuestion,
                                fontFamily = FrauncesFontFamily,
                                fontSize = 19.sp,
                                color = Color(0xFF2C221E),
                                lineHeight = 25.sp
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = if (coupleData.isUserAnswerSealed) "Answer unsealed ✨" else "Answer today's question →",
                                fontSize = 14.sp,
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

            // 4 ACTION GRID (Play, Memories, Lists, Leaderboard)
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // 1: Play
                    ActionGridCard(
                        modifier = Modifier.weight(1f),
                        emoji = "🎯",
                        title = "Play",
                        subtitle = "2 GAMES",
                        testTag = "home_action_play",
                        onClick = onOpenPlay
                    )

                    // 2: Memories
                    ActionGridCard(
                        modifier = Modifier.weight(1f),
                        emoji = "🖼️",
                        title = "Memories",
                        subtitle = "ADD ONE",
                        testTag = "home_action_memories",
                        onClick = onOpenMemories
                    )

                    // 3: Lists
                    ActionGridCard(
                        modifier = Modifier.weight(1f),
                        emoji = "📋",
                        title = "Lists",
                        subtitle = "2 OPEN",
                        testTag = "home_action_lists",
                        onClick = onOpenLists
                    )

                    // 4: Leaderboard
                    ActionGridCard(
                        modifier = Modifier.weight(1f),
                        emoji = "🏆",
                        title = "Leaderboard",
                        subtitle = coupleData.leaderboardRankWeek,
                        testTag = "home_action_leaderboard",
                        onClick = {
                            showFloatingToast = "Bond #${coupleData.bondRank}: In the top 5% of long distance couples!"
                        }
                    )
                }
            }

            // "THE MOMENT IT HAPPENS" BANNER
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .bounceClick {
                            showFloatingToast = "Quiet notifications are on. You'll only feel a pulse when Joy touches."
                        },
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFDF8)),
                    shape = RoundedCornerShape(22.dp),
                    border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(Color(0xFFF0E7DA)))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "THE MOMENT IT HAPPENS",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFA8998D),
                                letterSpacing = 1.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = buildAnnotatedString {
                                    append("Know when ")
                                    withStyle(SpanStyle(fontStyle = FontStyle.Italic, color = Color(0xFFE27B58))) {
                                        append("a touch lands")
                                    }
                                },
                                fontFamily = FrauncesFontFamily,
                                fontSize = 17.sp,
                                color = Color(0xFF2C221E)
                            )
                            Text(
                                text = "quiet by default, only what matters",
                                fontSize = 12.sp,
                                color = Color(0xFF8E8076)
                            )
                        }

                        // Pixel heart bubble icon
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0xFFFAF2EB)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("💬", fontSize = 22.sp)
                        }
                    }
                }
            }

            // PLUS BANNER CARD (Chocolate brown)
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .bounceClick {
                            showFloatingToast = "Couple's Plus: Unlimited cloud memories & locked vault reserved for you."
                        },
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF2E1E14)),
                    shape = RoundedCornerShape(22.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                            Text("🎁", fontSize = 28.sp)
                            Spacer(modifier = Modifier.width(14.dp))
                            Column {
                                Text(
                                    text = "the couple's price, held for you",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color(0xFFFFF7F0)
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "₦16,300.00 · ENDS IN 01:54:51",
                                    fontSize = 11.sp,
                                    color = Color(0xFFD6C8BE),
                                    letterSpacing = 0.5.sp
                                )
                            }
                        }

                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(Color(0x33FFFFFF)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("→", color = Color.White, fontSize = 16.sp)
                        }
                    }
                }
            }
        }

        // DIMMED SCRIM WHEN MENUS OPEN
        if (isAnyMenuOpen) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.35f))
                    .clickable {
                        showTouchMenu = false
                        showCreateMenu = false
                    }
            )
        }

        // FLOATING ACTION MENU: TOUCH REACTIONS POPUP (Left Heart)
        AnimatedVisibility(
            visible = showTouchMenu,
            enter = fadeIn(spring(stiffness = Spring.StiffnessMediumLow)) +
                    slideInVertically(spring(dampingRatio = 0.75f)) { it / 2 } +
                    scaleIn(initialScale = 0.85f),
            exit = fadeOut(tween(150)) + slideOutVertically { it / 2 } + scaleOut(targetScale = 0.85f),
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = 24.dp, bottom = 96.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                TouchReactionPill(emoji = "🤗", label = "Big hug") {
                    repo.sendTouch("🤗 Big hug")
                    heartBurstTrigger++
                    showTouchMenu = false
                    showFloatingToast = "Sent a warm Big Hug to Joy! 🤗"
                }
                TouchReactionPill(emoji = "💭", label = "Miss you") {
                    repo.sendTouch("💭 Miss you")
                    heartBurstTrigger++
                    showTouchMenu = false
                    showFloatingToast = "Sent Miss You to Joy! 💭"
                }
                TouchReactionPill(emoji = "💖", label = "I love you") {
                    repo.sendTouch("💖 I love you")
                    heartBurstTrigger++
                    showTouchMenu = false
                    showFloatingToast = "Sent I Love You to Joy! 💖"
                }
                TouchReactionPill(emoji = "✨", label = "Create yours ✦ FOREVER") {
                    showTouchMenu = false
                    showSendTouchDialog = true
                }
            }
        }

        // FLOATING ACTION MENU: CREATE ITEMS POPUP (Right Plus)
        AnimatedVisibility(
            visible = showCreateMenu,
            enter = fadeIn(spring(stiffness = Spring.StiffnessMediumLow)) +
                    slideInVertically(spring(dampingRatio = 0.75f)) { it / 2 } +
                    scaleIn(initialScale = 0.85f),
            exit = fadeOut(tween(150)) + slideOutVertically { it / 2 } + scaleOut(targetScale = 0.85f),
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 24.dp, bottom = 96.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                horizontalAlignment = Alignment.End
            ) {
                CreateActionPill(emoji = "📸", title = "A memory", subtitle = "A PHOTO YOU KEEP") {
                    showCreateMenu = false
                    onOpenMemories()
                }
                CreateActionPill(emoji = "🎙️", title = "A voice note", subtitle = "TEN SECONDS OF YOU") {
                    showCreateMenu = false
                    repo.addMemory("Voice note from Kola", isVoice = true, duration = 10)
                    showFloatingToast = "Saved a 10s voice keepsake for Joy 🎙️"
                }
                CreateActionPill(emoji = "📝", title = "A list", subtitle = "THINGS TO DO TOGETHER") {
                    showCreateMenu = false
                    onOpenLists()
                }
                CreateActionPill(emoji = "🎟️", title = "A date", subtitle = "SOMETHING TO WAIT FOR") {
                    showCreateMenu = false
                    onOpenUs()
                }
            }
        }

        // FLOATING BOTTOM DOCK
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Left: Pixel Heart Button (Toggles Touch Reactions)
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFFFFDF8))
                    .border(1.dp, Color(0xFFEFE8DD), CircleShape)
                    .shadow(elevation = 6.dp, shape = CircleShape, spotColor = Color(0x26000000))
                    .bounceClick {
                        showCreateMenu = false
                        showTouchMenu = !showTouchMenu
                    }
                    .testTag("dock_pixel_heart_button"),
                contentAlignment = Alignment.Center
            ) {
                PixelHeart(size = 24.dp)
            }

            // Center: Big Peach Pill ("Send today's touch")
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp)
                    .clip(RoundedCornerShape(28.dp))
                    .background(Color(0xFFF7C5A0))
                    .shadow(elevation = 6.dp, shape = RoundedCornerShape(28.dp), spotColor = Color(0x33E27B58))
                    .bounceClick {
                        showTouchMenu = false
                        showCreateMenu = false
                        showSendTouchDialog = true
                    }
                    .testTag("dock_send_touch_button"),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Send today's touch",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF332014)
                    )
                    Text(
                        text = "A LITTLE MESSAGE FOR TODAY",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF7A4A28),
                        letterSpacing = 0.5.sp
                    )
                }
            }

            // Right: Plus Button (Rotates to X when open)
            val rotation by animateFloatAsState(
                targetValue = if (showCreateMenu) 45f else 0f,
                animationSpec = spring(dampingRatio = 0.7f, stiffness = 500f),
                label = "plus_rotate"
            )

            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFFFFDF8))
                    .border(1.dp, Color(0xFFEFE8DD), CircleShape)
                    .shadow(elevation = 6.dp, shape = CircleShape, spotColor = Color(0x26000000))
                    .bounceClick {
                        showTouchMenu = false
                        showCreateMenu = !showCreateMenu
                    }
                    .testTag("dock_plus_action_button"),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add",
                    tint = Color(0xFF2C221E),
                    modifier = Modifier
                        .size(24.dp)
                        .rotate(rotation)
                )
            }
        }

        // FLYING HEART PARTICLES ANIMATION
        if (heartBurstTrigger > 0) {
            FloatingHeartsOverlay(trigger = heartBurstTrigger)
        }

        // FLOATING SNACKBAR / TOAST
        AnimatedVisibility(
            visible = showFloatingToast != null,
            enter = fadeIn() + slideInVertically { -it },
            exit = fadeOut() + slideOutVertically { -it },
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 70.dp, start = 20.dp, end = 20.dp)
        ) {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFF2E1E14)),
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Text(
                    text = showFloatingToast ?: "",
                    color = Color(0xFFFFF7F0),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(horizontal = 18.dp, vertical = 12.dp)
                )
            }
            LaunchedEffect(showFloatingToast) {
                delay(3000)
                showFloatingToast = null
            }
        }

        // DIALOG: SEND CUSTOM TOUCH
        if (showSendTouchDialog) {
            AlertDialog(
                onDismissRequest = { showSendTouchDialog = false },
                containerColor = Color(0xFFFFFDF8),
                shape = RoundedCornerShape(24.dp),
                title = {
                    Text(
                        text = "Send today's touch",
                        fontFamily = FrauncesFontFamily,
                        fontSize = 20.sp,
                        color = Color(0xFF2C221E)
                    )
                },
                text = {
                    Column {
                        Text(
                            text = "A quiet, gentle note that lands on Joy's screen.",
                            fontSize = 13.sp,
                            color = Color(0xFF8E8076)
                        )
                        Spacer(modifier = Modifier.height(14.dp))
                        OutlinedTextField(
                            value = touchMessageInput,
                            onValueChange = { touchMessageInput = it },
                            placeholder = { Text("Thinking of you...") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp)
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            if (touchMessageInput.isNotBlank()) {
                                repo.sendTouch(touchMessageInput)
                                heartBurstTrigger++
                                showFloatingToast = "Sent touch to Joy! ✨"
                                touchMessageInput = ""
                            }
                            showSendTouchDialog = false
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF7C5A0)),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text("Send Touch", color = Color(0xFF332014), fontWeight = FontWeight.Bold)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showSendTouchDialog = false }) {
                        Text("Cancel", color = Color(0xFF8E8076))
                    }
                }
            )
        }

        // DIALOG: SET USER MOOD
        if (showMoodDialog) {
            AlertDialog(
                onDismissRequest = { showMoodDialog = false },
                containerColor = Color(0xFFFFFDF8),
                shape = RoundedCornerShape(24.dp),
                title = {
                    Text("How are you today?", fontFamily = FrauncesFontFamily, fontSize = 20.sp)
                },
                text = {
                    val moods = listOf(
                        "🥰 Loving & warm",
                        "☕ Cozy & focused",
                        "😴 A little tired",
                        "🌿 Peaceful",
                        "🏃 Busy day",
                        "💭 Missing you"
                    )
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        moods.forEach { mood ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .bounceClick {
                                        repo.setUserMood(mood)
                                        showMoodDialog = false
                                        showFloatingToast = "Mood updated for Joy to see!"
                                    },
                                colors = CardDefaults.cardColors(containerColor = Color(0xFFFBF8F2)),
                                shape = RoundedCornerShape(14.dp)
                            ) {
                                Text(
                                    text = mood,
                                    fontSize = 14.sp,
                                    color = Color(0xFF2C221E),
                                    modifier = Modifier.padding(14.dp)
                                )
                            }
                        }
                    }
                },
                confirmButton = {}
            )
        }
    }
}

@Composable
private fun ActionGridCard(
    emoji: String,
    title: String,
    subtitle: String,
    testTag: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .bounceClick { onClick() }
            .testTag(testTag),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFDF8)),
        shape = RoundedCornerShape(20.dp),
        border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(Color(0xFFF0E7DA)))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp, horizontal = 6.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(emoji, fontSize = 24.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = title,
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
                letterSpacing = 0.5.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun TouchReactionPill(
    emoji: String,
    label: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .bounceClick { onClick() }
            .clip(RoundedCornerShape(22.dp))
            .background(Color(0xFFFFFDF8))
            .border(1.dp, Color(0xFFEFE8DD), RoundedCornerShape(22.dp))
            .shadow(4.dp, RoundedCornerShape(22.dp), spotColor = Color(0x22000000))
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(emoji, fontSize = 20.sp)
        Text(
            text = label,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF2C221E)
        )
    }
}

@Composable
private fun CreateActionPill(
    emoji: String,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(220.dp)
            .bounceClick { onClick() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFDF8)),
        border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(Color(0xFFEFE8DD))),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFFFAF2EB)),
                contentAlignment = Alignment.Center
            ) {
                Text(emoji, fontSize = 18.sp)
            }
            Column {
                Text(
                    text = title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF2C221E)
                )
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
}

@Composable
private fun DashedLine(
    modifier: Modifier = Modifier,
    color: Color = Color(0xFFDACFBF)
) {
    Canvas(modifier = modifier.height(1.dp)) {
        drawLine(
            color = color,
            start = Offset(0f, 0f),
            end = Offset(size.width, 0f),
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f),
            strokeWidth = 2f
        )
    }
}

@Composable
private fun FloatingHeartsOverlay(trigger: Int) {
    val heartAnim = remember(trigger) { Animatable(0f) }
    LaunchedEffect(trigger) {
        heartAnim.snapTo(0f)
        heartAnim.animateTo(
            targetValue = 1f,
            animationSpec = tween(1200, easing = EaseOutCubic)
        )
    }

    val alpha = (1f - heartAnim.value).coerceIn(0f, 1f)
    val offsetY = (-240f * heartAnim.value).dp

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .offset(y = offsetY)
                .graphicsLayer { this.alpha = alpha }
        ) {
            Text("💖", fontSize = 28.sp)
            Text("✨", fontSize = 22.sp)
            Text("🥰", fontSize = 32.sp)
            Text("✨", fontSize = 20.sp)
            Text("💖", fontSize = 26.sp)
        }
    }
}
