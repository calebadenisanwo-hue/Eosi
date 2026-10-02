package com.example.meridian.ui.screens.together

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
fun AsyncGamesSheet(
    partnerName: String,
    onDismiss: () -> Unit
) {
    var selectedGame by remember { mutableIntStateOf(1) } // 1: Two Truths & A Lie, 2: This or That, 3: Story Builder

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = MaterialTheme.colorScheme.surface,
        modifier = Modifier.testTag("async_games_sheet")
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
                    text = "Async Turn-Based Games",
                    fontFamily = FrauncesFontFamily,
                    fontSize = 20.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
                IconButton(onClick = onDismiss) {
                    Icon(Icons.Default.Close, contentDescription = "Close")
                }
            }

            Text(
                text = "Play together on your own schedule across time zones without needing both on a live call.",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Game selector tabs
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                FilterChip(
                    selected = selectedGame == 1,
                    onClick = { selectedGame = 1 },
                    label = { Text("Two Truths & A Lie", fontSize = 11.sp) },
                    shape = RoundedCornerShape(12.dp)
                )
                FilterChip(
                    selected = selectedGame == 2,
                    onClick = { selectedGame = 2 },
                    label = { Text("This or That", fontSize = 11.sp) },
                    shape = RoundedCornerShape(12.dp)
                )
                FilterChip(
                    selected = selectedGame == 3,
                    onClick = { selectedGame = 3 },
                    label = { Text("Story Builder", fontSize = 11.sp) },
                    shape = RoundedCornerShape(12.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            when (selectedGame) {
                1 -> {
                    // Two Truths & A Lie
                    Text(
                        text = "Two Truths & A Lie from $partnerName",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp
                    )
                    Text(
                        text = "Guess which one is the lie:",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    var guessedStatement by remember { mutableIntStateOf(0) }
                    var guessResult by remember { mutableStateOf<String?>(null) }

                    val statements = listOf(
                        "1. I drank three flat whites before 11am today at the studio.",
                        "2. I got lost on tram line 11 and ended up at Saltholmen harbor.",
                        "3. I accidentally wore mismatched socks to my architecture presentation."
                    )

                    statements.forEachIndexed { idx, st ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clickable {
                                    guessedStatement = idx + 1
                                    guessResult = if (idx == 1) {
                                        "🎉 You got it! Statement 2 is the lie. (I took tram line 9, not 11!)"
                                    } else {
                                        "❌ Nope, that one is actually true! Try another."
                                    }
                                },
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (guessedStatement == idx + 1) MeridianApricot.copy(alpha = 0.15f)
                                else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                            )
                        ) {
                            Text(
                                text = st,
                                fontSize = 13.sp,
                                modifier = Modifier.padding(14.dp),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }

                    if (guessResult != null) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = guessResult!!,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            color = if (guessResult!!.startsWith("🎉")) Color(0xFF4CAF50) else MaterialTheme.colorScheme.error
                        )
                    }
                }

                2 -> {
                    // This or That
                    Text(
                        text = "Rapid-Fire This or That",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp
                    )
                    Text(
                        text = "Select your picks; responses reveal together to check alignment.",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    val pairs = listOf(
                        "Early Morning 🌅" to "Late Night 🌙",
                        "Window Seat ✈️" to "Aisle Seat 💺",
                        "Cooking at Home 🍳" to "Eating Out 🍜",
                        "Beach Holiday 🏖️" to "Mountain Cabin 🏔️",
                        "Long Phone Call 📞" to "Voice Notes 🎙️"
                    )

                    pairs.forEach { (optA, optB) ->
                        var myPick by remember { mutableStateOf<String?>(null) }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = { myPick = optA },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (myPick == optA) MeridianApricot else MaterialTheme.colorScheme.surfaceVariant
                                )
                            ) {
                                Text(optA, fontSize = 11.sp, color = if (myPick == optA) Color.White else MaterialTheme.colorScheme.onSurface)
                            }

                            Button(
                                onClick = { myPick = optB },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (myPick == optB) MeridianApricot else MaterialTheme.colorScheme.surfaceVariant
                                )
                            ) {
                                Text(optB, fontSize = 11.sp, color = if (myPick == optB) Color.White else MaterialTheme.colorScheme.onSurface)
                            }
                        }
                    }
                }

                3 -> {
                    // Story Builder
                    Text(
                        text = "One-Sentence Story Builder",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp
                    )
                    Text(
                        text = "Take turns writing alternate sentences of our future adventure.",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    val storyLines = listOf(
                        "Ayaan: We finally unpacked the last cardboard box in our new apartment.",
                        "Linnea: The kettle started whistling before we could find two matching mugs.",
                        "Ayaan: So we sat on the floor by the window and drank tea from jam jars."
                    )

                    storyLines.forEach { line ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
                        ) {
                            Text(
                                text = line,
                                fontSize = 13.sp,
                                modifier = Modifier.padding(12.dp),
                                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    var nextSentence by remember { mutableStateOf("") }
                    OutlinedTextField(
                        value = nextSentence,
                        onValueChange = { nextSentence = it },
                        placeholder = { Text("Write the next sentence...") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = { nextSentence = "" },
                        modifier = Modifier.align(Alignment.End),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Text("Add Sentence ✍️")
                    }
                }
            }
        }
    }
}
