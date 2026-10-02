package com.example.meridian.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.meridian.data.model.Letter
import com.example.meridian.data.repository.MeridianRepo
import com.example.meridian.ui.screens.letters.WriteLetterSheet
import com.example.ui.theme.FrauncesFontFamily
import com.example.ui.theme.MeridianApricot
import com.example.ui.theme.MeridianDuskBlue

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TalkScreen(
    repo: MeridianRepo,
    modifier: Modifier = Modifier
) {
    val activeUser by repo.activeUser.collectAsState()
    val partnerUser by repo.partnerUser.collectAsState()
    val currentQuestion by repo.currentQuestion.collectAsState()
    val answers by repo.currentQuestionAnswers.collectAsState()
    val letters by repo.letters.collectAsState()

    var answerInput by remember { mutableStateOf("") }
    var selectedLetter by remember { mutableStateOf<Letter?>(null) }
    var showWriteLetterSheet by remember { mutableStateOf(false) }

    val myAnswer = answers[activeUser.uid]
    val partnerAnswer = answers[partnerUser.uid]

    // Group letters by trigger
    val lettersByTrigger = remember(letters) {
        letters.groupBy { it.trigger }
    }

    Box(modifier = modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .testTag("talk_screen_scroll"),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 100.dp)
        ) {
            // Section Header
            item {
                Text(
                    text = "Talk",
                    fontFamily = FrauncesFontFamily,
                    fontSize = 28.sp,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = "No general chat. Letters, questions, and quiet notes.",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Daily Question Card (Reveal-Together)
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("daily_question_card"),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "DAILY QUESTION · ${currentQuestion.deck.uppercase()}",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = MeridianApricot
                            )
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                TextButton(
                                    onClick = { repo.drawNextQuestion() },
                                    contentPadding = PaddingValues(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Icon(Icons.Default.Refresh, contentDescription = "Draw another", modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Draw another", fontSize = 11.sp)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "“${currentQuestion.promptText}”",
                            fontFamily = FrauncesFontFamily,
                            fontSize = 18.sp,
                            lineHeight = 24.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        if (myAnswer != null) {
                            // User has submitted: Show both answers side by side
                            Text(
                                text = "Your Answer:",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = myAnswer.answerText,
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            if (partnerAnswer != null) {
                                Text(
                                    text = "${partnerUser.displayName}'s Answer:",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = partnerAnswer.answerText,
                                    fontSize = 14.sp,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            } else {
                                Text(
                                    text = "Waiting for ${partnerUser.displayName}'s answer...",
                                    fontSize = 12.sp,
                                    fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        } else {
                            // User has not submitted yet
                            Text(
                                text = "No pressure to go first. Both answers reveal together once both respond.",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            OutlinedTextField(
                                value = answerInput,
                                onValueChange = { answerInput = it },
                                placeholder = { Text("Write your answer...") },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(16.dp)
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Button(
                                onClick = {
                                    if (answerInput.isNotBlank()) {
                                        repo.submitAnswer(currentQuestion.id, answerInput)
                                        answerInput = ""
                                    }
                                },
                                enabled = answerInput.isNotBlank(),
                                shape = RoundedCornerShape(16.dp),
                                modifier = Modifier.align(Alignment.End)
                            ) {
                                Text("Submit & Seal")
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
            }

            // Open When Shelf Header
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Open When Shelf",
                            fontFamily = FrauncesFontFamily,
                            fontSize = 20.sp,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Text(
                            text = "Sealed envelopes for moments you need them most.",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Button(
                        onClick = { showWriteLetterSheet = true },
                        shape = RoundedCornerShape(14.dp),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Write Letter", fontSize = 12.sp)
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
            }

            // Letters List
            items(letters) { letter ->
                LetterItem(
                    letter = letter,
                    partnerName = partnerUser.displayName,
                    onClick = { selectedLetter = letter }
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }

        // Dialogs
        if (selectedLetter != null) {
            val let = selectedLetter!!
            AlertDialog(
                onDismissRequest = { selectedLetter = null },
                confirmButton = {
                    TextButton(onClick = {
                        repo.openLetter(let.id)
                        selectedLetter = null
                    }) {
                        Text("Close with a Thank You 🤍")
                    }
                },
                title = {
                    Text(text = "✉️ ${let.title}", fontFamily = FrauncesFontFamily)
                },
                text = {
                    Column {
                        Text(
                            text = "Trigger: Open when ${let.trigger}",
                            fontSize = 12.sp,
                            color = MeridianApricot,
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(text = let.body, fontSize = 14.sp, lineHeight = 20.sp)
                    }
                },
                shape = RoundedCornerShape(24.dp)
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
}

@Composable
private fun LetterItem(
    letter: Letter,
    partnerName: String,
    onClick: () -> Unit
) {
    val isOpened = letter.openedAt != null
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isOpened) MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
            else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f)
        )
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = if (isOpened) "📖" else "✉️", fontSize = 24.sp)
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Open when ${letter.trigger}",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = if (isOpened) "Opened · Tap to re-read" else "Sealed envelope from $partnerName",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            if (!isOpened) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(MeridianApricot.copy(alpha = 0.15f))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(text = "Sealed", fontSize = 10.sp, color = MeridianApricot, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
