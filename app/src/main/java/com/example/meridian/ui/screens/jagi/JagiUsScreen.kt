package com.example.meridian.ui.screens.jagi

import androidx.activity.compose.BackHandler
import androidx.compose.animation.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
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
import com.example.meridian.ui.components.bounceClick
import com.example.ui.theme.FrauncesFontFamily

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JagiUsScreen(
    repo: JagiRepo,
    onBack: () -> Unit,
    onOpenMemories: () -> Unit,
    onOpenLists: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { onBack() }
    val coupleData by repo.coupleData.collectAsState()

    var showUsSettingsSheet by remember { mutableStateOf(false) }
    var showPlanReunionDialog by remember { mutableStateOf(false) }
    var reunionInput by remember { mutableStateOf("Christmas in Paris ✈️") }
    var reunionPlan by remember { mutableStateOf<String?>(null) }
    var showAddDateDialog by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFFAF5EE))
            .testTag("jagi_us_screen")
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
                        modifier = Modifier.size(40.dp).testTag("us_back_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color(0xFF2C221E)
                        )
                    }

                    Text(
                        text = "US",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFA8998D),
                        letterSpacing = 2.sp
                    )

                    IconButton(
                        onClick = { showUsSettingsSheet = true },
                        modifier = Modifier.size(40.dp).testTag("us_options_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.MoreHoriz,
                            contentDescription = "Settings",
                            tint = Color(0xFF2C221E)
                        )
                    }
                }
            }

            // HEADER: OVERLAPPING AVATARS + DAYS TOGETHER
            item {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Overlapping avatars with center heart
                    Box(
                        modifier = Modifier.height(64.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(54.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFF4A3B32))
                                    .border(2.dp, Color(0xFFFFFDF8), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("🧑🏾", fontSize = 28.sp)
                            }
                            Spacer(modifier = Modifier.width((-12).dp))
                            Box(
                                modifier = Modifier
                                    .size(54.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFF385E50))
                                    .border(2.dp, Color(0xFFFFFDF8), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("👩🏾", fontSize = 28.sp)
                            }
                        }

                        // Center tiny heart badge
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFFFFDF8))
                                .border(1.dp, Color(0xFFF0E7DA), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("💖", fontSize = 12.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "${coupleData.userName} & ${coupleData.partnerName}",
                        fontFamily = FrauncesFontFamily,
                        fontSize = 26.sp,
                        color = Color(0xFF2C221E)
                    )

                    Spacer(modifier = Modifier.height(2.dp))

                    // Huge days together count
                    Text(
                        text = "${coupleData.daysTogether}",
                        fontFamily = FrauncesFontFamily,
                        fontSize = 62.sp,
                        color = Color(0xFFD77A61),
                        lineHeight = 66.sp
                    )

                    Text(
                        text = "DAYS TOGETHER · SINCE 2 MAY 2026",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFA8998D),
                        letterSpacing = 1.2.sp
                    )
                }
            }

            // SECTION: YOUR BOND · DAY ONE
            item {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "YOUR BOND · DAY ONE",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFA8998D),
                        letterSpacing = 1.2.sp
                    )

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFDF8)),
                        shape = RoundedCornerShape(24.dp),
                        border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(Color(0xFFF0E7DA)))
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "SPARK · HATCHED ${coupleData.hatchedDate.uppercase()}",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFA8998D),
                                letterSpacing = 1.sp
                            )

                            Spacer(modifier = Modifier.height(14.dp))

                            PixelCompanionPet(size = 56.dp, showAura = true)

                            Spacer(modifier = Modifier.height(14.dp))

                            // Dots indicator
                            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                repeat(5) { i ->
                                    Box(
                                        modifier = Modifier
                                            .size(width = 8.dp, height = 4.dp)
                                            .clip(RoundedCornerShape(2.dp))
                                            .background(if (i == 0) Color(0xFFD77A61) else Color(0xFFEFE8DD))
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "LV.${coupleData.companionLevel} · ${coupleData.companionSpecies.uppercase()}",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF5A4D45),
                                    letterSpacing = 0.5.sp
                                )
                                Text(
                                    text = "${coupleData.companionXp} / ${coupleData.companionMaxXp} XP",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF5A4D45)
                                )
                            }

                            Spacer(modifier = Modifier.height(6.dp))

                            // XP Progress bar
                            val progress = (coupleData.companionXp.toFloat() / coupleData.companionMaxXp.toFloat()).coerceIn(0f, 1f)
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(6.dp)
                                    .clip(RoundedCornerShape(3.dp))
                                    .background(Color(0xFFF4EDE4))
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxHeight()
                                        .fillMaxWidth(progress)
                                        .background(Color(0xFFD77A61))
                                )
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            Text(
                                text = buildAnnotatedString {
                                    append("Just hatched. ")
                                    withStyle(SpanStyle(fontStyle = FontStyle.Italic)) {
                                        append("Your first days will shape it.")
                                    }
                                    append(" Every touch, answer and game feeds it.")
                                },
                                fontSize = 12.sp,
                                color = Color(0xFF8E8076),
                                textAlign = TextAlign.Center,
                                lineHeight = 18.sp
                            )
                        }
                    }
                }
            }

            // SECTION: LONG DISTANCE
            item {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "LONG DISTANCE",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFA8998D),
                        letterSpacing = 1.2.sp
                    )

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFDF8)),
                        shape = RoundedCornerShape(24.dp),
                        border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(Color(0xFFF0E7DA)))
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(18.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column(horizontalAlignment = Alignment.Start) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = coupleData.userTime,
                                            fontFamily = FrauncesFontFamily,
                                            fontSize = 17.sp,
                                            fontWeight = FontWeight.Medium,
                                            color = Color(0xFF2C221E)
                                        )
                                        Text(" 🌙", fontSize = 13.sp)
                                    }
                                    Text("You", fontSize = 11.sp, color = Color(0xFF8E8076))
                                }

                                Row(
                                    modifier = Modifier.weight(1f).padding(horizontal = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    DashedUsLine(modifier = Modifier.weight(1f))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("💖", fontSize = 13.sp)
                                    Spacer(modifier = Modifier.width(6.dp))
                                    DashedUsLine(modifier = Modifier.weight(1f))
                                }

                                Column(horizontalAlignment = Alignment.End) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = coupleData.partnerTime,
                                            fontFamily = FrauncesFontFamily,
                                            fontSize = 17.sp,
                                            fontWeight = FontWeight.Medium,
                                            color = Color(0xFF2C221E)
                                        )
                                        Text(" 🌙", fontSize = 13.sp)
                                    }
                                    Text("Her", fontSize = 11.sp, color = Color(0xFF8E8076))
                                }
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            // Dashed inner card: Reunion plan
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(16.dp))
                                    .border(
                                        width = 1.dp,
                                        color = Color(0xFFDACFBF),
                                        shape = RoundedCornerShape(16.dp)
                                    )
                                    .background(Color(0xFFFAF5EE))
                                    .bounceClick { showPlanReunionDialog = true }
                                    .padding(vertical = 16.dp, horizontal = 16.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        text = reunionPlan ?: "No reunion planned yet",
                                        fontFamily = FrauncesFontFamily,
                                        fontSize = 15.sp,
                                        color = Color(0xFF2C221E)
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = if (reunionPlan != null) "Tap to edit ✈️" else "Dream one up 💛",
                                        fontSize = 12.sp,
                                        color = Color(0xFFE27B58),
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // SECTION: OUR PATTERNS
            item {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "OUR PATTERNS",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFA8998D),
                            letterSpacing = 1.2.sp
                        )
                        Text(
                            text = "See all →",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFFE27B58)
                        )
                    }

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFDF8)),
                        shape = RoundedCornerShape(24.dp),
                        border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(Color(0xFFF0E7DA)))
                    ) {
                        Column(modifier = Modifier.padding(18.dp)) {
                            Text(
                                text = buildAnnotatedString {
                                    append("Your patterns start after a ")
                                    withStyle(SpanStyle(fontStyle = FontStyle.Italic, color = Color(0xFFD77A61))) {
                                        append("week of moods.")
                                    }
                                },
                                fontFamily = FrauncesFontFamily,
                                fontSize = 17.sp,
                                color = Color(0xFF2C221E)
                            )

                            Spacer(modifier = Modifier.height(14.dp))

                            // 7 Rounded square mood day slots
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                repeat(7) {
                                    Box(
                                        modifier = Modifier
                                            .size(38.dp)
                                            .clip(RoundedCornerShape(12.dp))
                                            .border(1.dp, Color(0xFFDACFBF), RoundedCornerShape(12.dp))
                                            .background(Color(0xFFFAF5EE))
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Text(
                                text = "0 DAYS IN · 7 TO GO",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFA8998D),
                                letterSpacing = 1.sp
                            )
                        }
                    }
                }
            }

            // SECTION: MEMORIES
            item {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "MEMORIES",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFA8998D),
                        letterSpacing = 1.2.sp
                    )

                    // Big dashed memory card
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(110.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .border(1.dp, Color(0xFFDACFBF), RoundedCornerShape(20.dp))
                            .background(Color(0xFFFAF5EE))
                            .bounceClick { onOpenMemories() },
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("🖼️", fontSize = 28.sp)
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Your next memory goes here",
                                fontSize = 13.sp,
                                color = Color(0xFF8E8076)
                            )
                        }
                    }

                    // Record a hello card
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .bounceClick {
                                repo.addMemory("Voice Hello from Kola", isVoice = true, duration = 10)
                            },
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFDF8)),
                        shape = RoundedCornerShape(18.dp),
                        border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(Color(0xFFF0E7DA)))
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFFFAF2EB)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text("🎙️", fontSize = 16.sp)
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(
                                        text = "Record a hello",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = Color(0xFF2C221E)
                                    )
                                    Text(
                                        text = "Your first voice note becomes a keepsake",
                                        fontSize = 11.sp,
                                        color = Color(0xFF8E8076)
                                    )
                                }
                            }
                            Text("→", color = Color(0xFFD77A61), fontSize = 16.sp)
                        }
                    }

                    // The Whole Story card
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .bounceClick { onOpenMemories() },
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFDF8)),
                        shape = RoundedCornerShape(18.dp),
                        border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(Color(0xFFF0E7DA)))
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(Color(0xFFFAF2EB)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    PixelCompanionPet(size = 20.dp, showAura = false)
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(
                                        text = "THE WHOLE STORY",
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFFA8998D),
                                        letterSpacing = 1.sp
                                    )
                                    Text(
                                        text = "Everything we've done",
                                        fontSize = 15.sp,
                                        fontFamily = FrauncesFontFamily,
                                        color = Color(0xFF2C221E)
                                    )
                                    Text(
                                        text = "1 moments. It's just beginning.",
                                        fontSize = 11.sp,
                                        color = Color(0xFF8E8076)
                                    )
                                }
                            }
                            Text("→", color = Color(0xFFD77A61), fontSize = 16.sp)
                        }
                    }
                }
            }

            // SECTION: SHARED LISTS
            item {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "SHARED LISTS",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFA8998D),
                            letterSpacing = 1.2.sp
                        )
                        Text(
                            text = "ALL →",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFE27B58),
                            modifier = Modifier.bounceClick { onOpenLists() }
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // Bucket list preview
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(18.dp))
                                .border(1.dp, Color(0xFFDACFBF), RoundedCornerShape(18.dp))
                                .background(Color(0xFFFAF5EE))
                                .bounceClick { onOpenLists() }
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("🌍", fontSize = 22.sp)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Bucket list",
                                    fontFamily = FrauncesFontFamily,
                                    fontSize = 14.sp,
                                    color = Color(0xFF2C221E)
                                )
                                Text(
                                    text = "Add your first +",
                                    fontSize = 11.sp,
                                    color = Color(0xFFD77A61)
                                )
                            }
                        }

                        // When we're together preview
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(18.dp))
                                .border(1.dp, Color(0xFFDACFBF), RoundedCornerShape(18.dp))
                                .background(Color(0xFFFAF5EE))
                                .bounceClick { onOpenLists() }
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("🏡", fontSize = 22.sp)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "When we're together",
                                    fontFamily = FrauncesFontFamily,
                                    fontSize = 13.sp,
                                    color = Color(0xFF2C221E),
                                    maxLines = 1
                                )
                                Text(
                                    text = "Add your first +",
                                    fontSize = 11.sp,
                                    color = Color(0xFFD77A61)
                                )
                            }
                        }
                    }
                }
            }

            // SECTION: SPECIAL DATES
            item {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "SPECIAL DATES",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFA8998D),
                        letterSpacing = 1.2.sp
                    )

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFDF8)),
                        shape = RoundedCornerShape(22.dp),
                        border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(Color(0xFFF0E7DA)))
                    ) {
                        Column(modifier = Modifier.padding(18.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = "Our anniversary",
                                        fontFamily = FrauncesFontFamily,
                                        fontSize = 16.sp,
                                        color = Color(0xFF2C221E)
                                    )
                                    Text("2 MAY", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFFA8998D))
                                }

                                Text(
                                    text = "IN 212 D",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFFD77A61),
                                    letterSpacing = 1.sp
                                )
                            }

                            Spacer(modifier = Modifier.height(14.dp))
                            HorizontalDivider(color = Color(0xFFF0E7DA), thickness = 0.8.dp)
                            Spacer(modifier = Modifier.height(14.dp))

                            Text(
                                text = "+ Add a birthday, an anniversary...",
                                fontSize = 13.sp,
                                color = Color(0xFFD77A61),
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.bounceClick { showAddDateDialog = true }
                            )
                        }
                    }
                }
            }
        }

        // DIALOG: PLAN REUNION
        if (showPlanReunionDialog) {
            AlertDialog(
                onDismissRequest = { showPlanReunionDialog = false },
                containerColor = Color(0xFFFFFDF8),
                shape = RoundedCornerShape(24.dp),
                title = {
                    Text("Plan our next reunion", fontFamily = FrauncesFontFamily, fontSize = 20.sp)
                },
                text = {
                    Column {
                        Text(
                            text = "Set a flight, visit, or milestone date to count down together.",
                            fontSize = 13.sp,
                            color = Color(0xFF8E8076)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        OutlinedTextField(
                            value = reunionInput,
                            onValueChange = { reunionInput = it },
                            label = { Text("Trip name / location") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp)
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            reunionPlan = reunionInput
                            showPlanReunionDialog = false
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF7C5A0)),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text("Save Reunion", color = Color(0xFF332014), fontWeight = FontWeight.Bold)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showPlanReunionDialog = false }) {
                        Text("Cancel", color = Color(0xFF8E8076))
                    }
                }
            )
        }

        // DIALOG: ADD SPECIAL DATE
        if (showAddDateDialog) {
            var dateTitle by remember { mutableStateOf("") }
            AlertDialog(
                onDismissRequest = { showAddDateDialog = false },
                containerColor = Color(0xFFFFFDF8),
                shape = RoundedCornerShape(24.dp),
                title = {
                    Text("Add a special date", fontFamily = FrauncesFontFamily, fontSize = 20.sp)
                },
                text = {
                    Column {
                        OutlinedTextField(
                            value = dateTitle,
                            onValueChange = { dateTitle = it },
                            placeholder = { Text("e.g. Joy's Birthday 🎂") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp)
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = { showAddDateDialog = false },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF7C5A0)),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text("Save Date", color = Color(0xFF332014), fontWeight = FontWeight.Bold)
                    }
                }
            )
        }

        // US · SETTINGS MODAL SHEET (Screenshot 1)
        if (showUsSettingsSheet) {
            ModalBottomSheet(
                onDismissRequest = { showUsSettingsSheet = false },
                containerColor = Color(0xFFFAF5EE),
                shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 6.dp)
                        .padding(bottom = 36.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "US · SETTINGS",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFA8998D),
                                letterSpacing = 2.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Just the two of you.",
                                fontFamily = FrauncesFontFamily,
                                fontStyle = FontStyle.Italic,
                                fontSize = 18.sp,
                                color = Color(0xFF2C221E)
                            )
                        }
                    }

                    // THE COMPANION
                    item {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text(
                                text = "THE COMPANION",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFA8998D),
                                letterSpacing = 1.2.sp
                            )

                            Card(
                                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFDF8)),
                                shape = RoundedCornerShape(20.dp),
                                border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(Color(0xFFF0E7DA)))
                            ) {
                                Column {
                                    SettingsRow(
                                        iconLeading = { PixelCompanionPet(size = 28.dp, showAura = false) },
                                        title = "Companion name",
                                        trailingValue = coupleData.companionName
                                    )
                                    HorizontalDivider(color = Color(0xFFF6EFE6), thickness = 0.8.dp)
                                    SettingsRow(
                                        iconLeading = {
                                            Box(
                                                modifier = Modifier
                                                    .size(24.dp)
                                                    .clip(CircleShape)
                                                    .background(Color(0xFF8BBF9F))
                                            )
                                        },
                                        title = "Companion style",
                                        trailingValue = "In your colours"
                                    )
                                }
                            }

                            Text(
                                text = "🤍 You both see the same companion, always. The name and coat are shared.",
                                fontSize = 11.sp,
                                color = Color(0xFF8E8076)
                            )
                        }
                    }

                    // THE COUPLE
                    item {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text(
                                text = "THE COUPLE",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFA8998D),
                                letterSpacing = 1.2.sp
                            )

                            Card(
                                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFDF8)),
                                shape = RoundedCornerShape(20.dp),
                                border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(Color(0xFFF0E7DA)))
                            ) {
                                Column {
                                    SettingsRow(
                                        title = "Together since",
                                        trailingValue = "2 May 2026"
                                    )
                                    HorizontalDivider(color = Color(0xFFF6EFE6), thickness = 0.8.dp)
                                    SettingsRow(
                                        title = "Special dates",
                                        trailingValue = "No dates"
                                    )
                                    HorizontalDivider(color = Color(0xFFF6EFE6), thickness = 0.8.dp)
                                    SettingsRow(
                                        title = "Your actions",
                                        subtitle = "The chips on your Home",
                                        trailingValue = "🤗 💭 💭"
                                    )
                                }
                            }
                        }
                    }

                    // THE LEADERBOARD
                    item {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text(
                                text = "THE LEADERBOARD",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFA8998D),
                                letterSpacing = 1.2.sp
                            )

                            Card(
                                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFDF8)),
                                shape = RoundedCornerShape(20.dp),
                                border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(Color(0xFFF0E7DA)))
                            ) {
                                Column {
                                    SettingsRow(
                                        title = "Visible on the leaderboard",
                                        subtitle = "Off, you are ranked as Bond ${coupleData.bondRank}",
                                        trailingValue = "Private"
                                    )
                                    HorizontalDivider(color = Color(0xFFF6EFE6), thickness = 0.8.dp)
                                    SettingsRow(
                                        title = "Bond name",
                                        trailingValue = "Not set"
                                    )
                                }
                            }
                        }
                    }

                    // FOOTER INFO
                    item {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFF2ECE1)),
                            shape = RoundedCornerShape(18.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("👤", fontSize = 18.sp)
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = "Your own profile, photo and notifications live in your personal settings, not here. This page is only what you share.",
                                    fontSize = 12.sp,
                                    color = Color(0xFF75685F),
                                    lineHeight = 17.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SettingsRow(
    title: String,
    subtitle: String? = null,
    trailingValue: String? = null,
    iconLeading: (@Composable () -> Unit)? = null,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (iconLeading != null) {
                Box(modifier = Modifier.size(32.dp), contentAlignment = Alignment.Center) {
                    iconLeading()
                }
                Spacer(modifier = Modifier.width(12.dp))
            }
            Column {
                Text(
                    text = title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF2C221E)
                )
                if (subtitle != null) {
                    Text(
                        text = subtitle,
                        fontSize = 11.sp,
                        color = Color(0xFF8E8076)
                    )
                }
            }
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            if (trailingValue != null) {
                Text(
                    text = trailingValue,
                    fontSize = 13.sp,
                    color = Color(0xFF8E8076)
                )
                Spacer(modifier = Modifier.width(6.dp))
            }
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = Color(0xFFC0B3A6),
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

@Composable
private fun DashedUsLine(
    modifier: Modifier = Modifier,
    color: Color = Color(0xFFDACFBF)
) {
    Canvas(modifier = modifier.height(1.dp)) {
        drawLine(
            color = color,
            start = Offset(0f, 0f),
            end = Offset(size.width, 0f),
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(8f, 8f), 0f),
            strokeWidth = 2f
        )
    }
}
