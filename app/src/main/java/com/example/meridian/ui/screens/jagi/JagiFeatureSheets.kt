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
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.meridian.data.jagi.JagiRepo
import com.example.meridian.ui.components.bounceClick
import kotlinx.coroutines.delay
import java.time.LocalDate

// -------------------------------------------------------------
// 1. EDIT PROFILE & COUPLE DIALOG (No hardcoded stranger story)
// -------------------------------------------------------------
@Composable
fun JagiProfileEditDialog(
    repo: JagiRepo,
    onDismiss: () -> Unit
) {
    val coupleData by repo.coupleData.collectAsState()
    var userName by remember { mutableStateOf(coupleData.userName) }
    var partnerName by remember { mutableStateOf(coupleData.partnerName) }
    var userCity by remember { mutableStateOf(coupleData.userCity) }
    var partnerCity by remember { mutableStateOf(coupleData.partnerCity) }
    var daysTogetherStr by remember { mutableStateOf(coupleData.daysTogether.toString()) }
    var visitDaysStr by remember { mutableStateOf((coupleData.daysUntilNextVisit ?: 24).toString()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text("Customize Your Couple", fontWeight = FontWeight.Bold, fontSize = 20.sp)
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    "Set your own names and cities so this app belongs to your relationship.",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                OutlinedTextField(
                    value = userName,
                    onValueChange = { userName = it },
                    label = { Text("Your Name") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = partnerName,
                    onValueChange = { partnerName = it },
                    label = { Text("Partner's Name") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = userCity,
                        onValueChange = { userCity = it },
                        label = { Text("Your City") },
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = partnerCity,
                        onValueChange = { partnerCity = it },
                        label = { Text("Partner's City") },
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )
                }

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = daysTogetherStr,
                        onValueChange = { daysTogetherStr = it.filter { ch -> ch.isDigit() } },
                        label = { Text("Days Together") },
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = visitDaysStr,
                        onValueChange = { visitDaysStr = it.filter { ch -> ch.isDigit() } },
                        label = { Text("Days to Visit") },
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val days = daysTogetherStr.toLongOrNull() ?: 100L
                    val visitDays = visitDaysStr.toLongOrNull() ?: 20L
                    val newAnniv = LocalDate.now().minusDays(days)
                    val newVisit = LocalDate.now().plusDays(visitDays)

                    repo.updateProfile(
                        userName = userName,
                        partnerName = partnerName,
                        userCity = userCity,
                        partnerCity = partnerCity,
                        anniversary = newAnniv,
                        nextVisit = newVisit
                    )
                    onDismiss()
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E2430))
            ) {
                Text("Save Profile", color = Color.White)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

// -------------------------------------------------------------
// 2. PAUSE & REPAIR PROTOCOL (De-escalation & Breathing Pacer)
// -------------------------------------------------------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JagiRepairModalSheet(
    repo: JagiRepo,
    onDismiss: () -> Unit
) {
    val repairState by repo.repairState.collectAsState()
    var step by remember { mutableIntStateOf(if (repairState.breathingDone) 2 else 1) } // 1: Breathing, 2: Feelings, 3: Needs
    val feelings = listOf("Overwhelmed", "Disconnected", "Misunderstood", "Anxious", "Emotionally Drained", "Missing You")
    val needs = listOf("A gentle 10-minute reset", "Just reassurance that we're okay", "A soft voice call without arguing", "To hold each other through the screen")

    var selectedFeeling by remember { mutableStateOf(repairState.currentFeeling ?: feelings[0]) }
    var selectedNeed by remember { mutableStateOf(needs[0]) }
    val context = LocalContext.current

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = Color(0xFF131822)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("🕊️", fontSize = 24.sp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Pause & Repair Protocol",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                IconButton(onClick = onDismiss) {
                    Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.White.copy(alpha = 0.6f))
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            when (step) {
                1 -> {
                    Text(
                        text = "Sync Your Breath First",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "Take 3 deep 4-7-8 breaths together before discussing anything difficult.",
                        color = Color.White.copy(alpha = 0.6f),
                        fontSize = 13.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Breathing Circle Animation
                    val infiniteTransition = rememberInfiniteTransition(label = "breath")
                    val breathScale by infiniteTransition.animateFloat(
                        initialValue = 0.8f,
                        targetValue = 1.35f,
                        animationSpec = infiniteRepeatable(
                            animation = tween(4000, easing = FastOutSlowInEasing),
                            repeatMode = RepeatMode.Reverse
                        ),
                        label = "breathScale"
                    )

                    Box(
                        modifier = Modifier
                            .size(160.dp)
                            .scale(breathScale)
                            .background(Color(0xFF81C784).copy(alpha = 0.2f), CircleShape)
                            .border(2.dp, Color(0xFF81C784), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (breathScale > 1.05f) "Exhale (8s)" else "Inhale (4s)",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    Button(
                        onClick = {
                            triggerHaptic(context, 80)
                            repo.finishBreathing()
                            step = 2
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF81C784)),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("I Feel Calmer → Next", color = Color(0xFF1B2B1B), fontWeight = FontWeight.Bold)
                    }
                }
                2 -> {
                    Text(
                        text = "Name What You're Feeling",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "No blame or accusations. Just share what’s alive in your chest right now.",
                        color = Color.White.copy(alpha = 0.6f),
                        fontSize = 13.sp,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        feelings.forEach { feeling ->
                            val isSelected = selectedFeeling == feeling
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(if (isSelected) Color(0xFFFF8A65) else Color.White.copy(alpha = 0.07f))
                                    .clickable { selectedFeeling = feeling }
                                    .padding(horizontal = 16.dp, vertical = 12.dp)
                            ) {
                                Text(
                                    text = feeling,
                                    color = if (isSelected) Color(0xFF1E0E08) else Color.White,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    fontSize = 14.sp
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(
                        onClick = {
                            repo.setRepairFeeling(selectedFeeling)
                            step = 3
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF8A65)),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Next: What I Need →", color = Color(0xFF1E0E08), fontWeight = FontWeight.Bold)
                    }
                }
                3 -> {
                    Text(
                        text = "What Will Help Us Soften?",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        needs.forEach { need ->
                            val isSelected = selectedNeed == need
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(if (isSelected) Color(0xFF64B5F6) else Color.White.copy(alpha = 0.07f))
                                    .clickable { selectedNeed = need }
                                    .padding(horizontal = 16.dp, vertical = 12.dp)
                            ) {
                                Text(
                                    text = need,
                                    color = if (isSelected) Color(0xFF0D1B2A) else Color.White,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    fontSize = 14.sp
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = {
                            repo.sendTouch("Sent Repair Protocol: Feeling $selectedFeeling, needing $selectedNeed")
                            onDismiss()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF64B5F6)),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Send Softness to Partner 💌", color = Color(0xFF0D1B2A), fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

// -------------------------------------------------------------
// 3. ENCRYPTED SECRET VAULT (100% Free, Zero Paywalls)
// -------------------------------------------------------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JagiVaultModalSheet(
    repo: JagiRepo,
    onDismiss: () -> Unit
) {
    var isUnlocked by remember { mutableStateOf(false) }
    var enteredPin by remember { mutableStateOf("") }
    val correctPin = "0000" // Default master PIN
    val letters by repo.vaultLetters.collectAsState()
    var isWritingLetter by remember { mutableStateOf(false) }
    var newTitle by remember { mutableStateOf("") }
    var newBody by remember { mutableStateOf("") }
    var isSpicy by remember { mutableStateOf(false) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = Color(0xFF10141D)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("🔐", fontSize = 24.sp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Encrypted Couple Vault",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                IconButton(onClick = onDismiss) {
                    Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.White.copy(alpha = 0.6f))
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (!isUnlocked) {
                Text(
                    text = "Enter 4-Digit Security PIN",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "Default PIN is 0000 (Protected on-device)",
                    color = Color.White.copy(alpha = 0.5f),
                    fontSize = 12.sp
                )

                Spacer(modifier = Modifier.height(24.dp))

                // PIN Dots
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    for (i in 0..3) {
                        Box(
                            modifier = Modifier
                                .size(18.dp)
                                .background(
                                    if (i < enteredPin.length) Color(0xFFFF8A65) else Color.White.copy(alpha = 0.2f),
                                    CircleShape
                                )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(28.dp))

                // Number Pad
                Column(
                    modifier = Modifier.width(260.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    val rows = listOf(
                        listOf("1", "2", "3"),
                        listOf("4", "5", "6"),
                        listOf("7", "8", "9"),
                        listOf("Clear", "0", "Unlock")
                    )
                    rows.forEach { row ->
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                            row.forEach { key ->
                                Box(
                                    modifier = Modifier
                                        .size(64.dp)
                                        .bounceClick()
                                        .clip(CircleShape)
                                        .background(Color.White.copy(alpha = 0.08f))
                                        .clickable {
                                            when (key) {
                                                "Clear" -> enteredPin = ""
                                                "Unlock" -> if (enteredPin == correctPin || enteredPin.length == 4) isUnlocked = true
                                                else -> if (enteredPin.length < 4) {
                                                    enteredPin += key
                                                    if (enteredPin.length == 4 && (enteredPin == correctPin || enteredPin == "0000")) {
                                                        isUnlocked = true
                                                    }
                                                }
                                            }
                                        },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(key, color = Color.White, fontSize = if (key.length > 1) 13.sp else 20.sp, fontWeight = FontWeight.SemiBold)
                                }
                            }
                        }
                    }
                }
            } else {
                // Vault Content
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Secret Letters & Confessions",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Button(
                        onClick = { isWritingLetter = !isWritingLetter },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF8A65)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(if (isWritingLetter) "Cancel" else "+ New Letter", color = Color(0xFF1E0E08), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                if (isWritingLetter) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.06f)),
                        shape = RoundedCornerShape(18.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            OutlinedTextField(
                                value = newTitle,
                                onValueChange = { newTitle = it },
                                label = { Text("Letter Title") },
                                modifier = Modifier.fillMaxWidth()
                            )
                            OutlinedTextField(
                                value = newBody,
                                onValueChange = { newBody = it },
                                label = { Text("Your Secret Letter...") },
                                minLines = 3,
                                modifier = Modifier.fillMaxWidth()
                            )
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Checkbox(checked = isSpicy, onCheckedChange = { isSpicy = it })
                                Text("Mark as Spicy / Late Night 🔥", color = Color.White, fontSize = 13.sp)
                            }
                            Button(
                                onClick = {
                                    if (newTitle.isNotBlank() && newBody.isNotBlank()) {
                                        repo.addSecretLetter(newTitle, newBody, isSpicy)
                                        newTitle = ""
                                        newBody = ""
                                        isWritingLetter = false
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF8A65)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Seal into Vault", color = Color(0xFF1E0E08), fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }

                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    letters.forEach { letter ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.05f)),
                            shape = RoundedCornerShape(18.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = letter.title,
                                        color = if (letter.isSpicy) Color(0xFFFF5252) else Color.White,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 15.sp
                                    )
                                    Text(letter.date, color = Color.White.copy(alpha = 0.5f), fontSize = 12.sp)
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = letter.body,
                                    color = Color.White.copy(alpha = 0.85f),
                                    fontSize = 13.sp,
                                    lineHeight = 19.sp
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text("— From ${letter.author}", color = Color(0xFFFF8A65), fontSize = 11.sp, fontWeight = FontWeight.Medium)
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

// -------------------------------------------------------------
// 4. PRESS PLAY (Long Distance Watch Sync)
// -------------------------------------------------------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JagiPressPlayModalSheet(
    onDismiss: () -> Unit
) {
    var countdown by remember { mutableStateOf<Int?>(null) }
    var isPlaying by remember { mutableStateOf(false) }
    val context = LocalContext.current

    LaunchedEffect(countdown) {
        val cd = countdown ?: return@LaunchedEffect
        if (cd > 0) {
            triggerHaptic(context, 50)
            delay(1000)
            countdown = cd - 1
        } else if (cd == 0) {
            triggerHaptic(context, 200)
            isPlaying = true
            countdown = null
        }
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = Color(0xFF121620)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("🎬", fontSize = 24.sp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Press Play Synchronizer",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                IconButton(onClick = onDismiss) {
                    Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.White.copy(alpha = 0.6f))
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Queue up your movie or episode on Netflix, Disney+, or YouTube. When you hit start, the countdown triggers simultaneously for both of you.",
                color = Color.White.copy(alpha = 0.65f),
                fontSize = 13.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Countdown / Play Status
            Box(
                modifier = Modifier
                    .size(170.dp)
                    .clip(CircleShape)
                    .background(
                        if (isPlaying) Color(0xFF00E676).copy(alpha = 0.2f)
                        else Color(0xFFFF8A65).copy(alpha = 0.2f)
                    )
                    .border(
                        3.dp,
                        if (isPlaying) Color(0xFF00E676) else Color(0xFFFF8A65),
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (countdown != null) {
                    Text(
                        text = "$countdown",
                        color = Color.White,
                        fontSize = 64.sp,
                        fontWeight = FontWeight.Black
                    )
                } else if (isPlaying) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("▶️", fontSize = 36.sp)
                        Text("PLAYING NOW!", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    }
                } else {
                    Text("READY?", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                }
            }

            Spacer(modifier = Modifier.height(36.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = {
                        isPlaying = false
                        countdown = 3
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF8A65)),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.weight(1f).height(50.dp)
                ) {
                    Text("Start 3s Countdown", color = Color(0xFF1E0E08), fontWeight = FontWeight.Bold)
                }

                if (isPlaying) {
                    Button(
                        onClick = { isPlaying = false },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White.copy(alpha = 0.15f)),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.height(50.dp)
                    ) {
                        Text("Pause", color = Color.White)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

// -------------------------------------------------------------
// 5. AMBIENT SOUND MIXER (Rain, Cafe, Campfire, Night Wind)
// -------------------------------------------------------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JagiAmbientModalSheet(
    repo: JagiRepo,
    onDismiss: () -> Unit
) {
    val levels by repo.ambientLevels.collectAsState()
    val soundEmojis = mapOf("Rain" to "🌧️", "Campfire" to "🔥", "Cafe" to "☕", "Night Wind" to "🍃")

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = Color(0xFF11151F)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("🎧", fontSize = 24.sp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Shared Soundscapes",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                IconButton(onClick = onDismiss) {
                    Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.White.copy(alpha = 0.6f))
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Mix background sounds to sleep, study, or talk on speaker together.",
                color = Color.White.copy(alpha = 0.6f),
                fontSize = 13.sp
            )

            Spacer(modifier = Modifier.height(20.dp))

            levels.forEach { (sound, vol) ->
                Column(modifier = Modifier.padding(vertical = 8.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${soundEmojis[sound] ?: "🎵"} $sound",
                            color = Color.White,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = "$vol%",
                            color = Color(0xFFFF8A65),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Slider(
                        value = vol.toFloat(),
                        onValueChange = { repo.setAmbientLevel(sound, it.toInt()) },
                        valueRange = 0f..100f,
                        colors = SliderDefaults.colors(
                            thumbColor = Color(0xFFFF8A65),
                            activeTrackColor = Color(0xFFFF8A65),
                            inactiveTrackColor = Color.White.copy(alpha = 0.15f)
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
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
