package com.example.meridian.ui.screens.jagi

import androidx.activity.compose.BackHandler
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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

@Composable
fun JagiMemoriesScreen(
    repo: JagiRepo,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { onBack() }
    val memories by repo.memories.collectAsState()

    var showAddDialog by remember { mutableStateOf(false) }
    var memoryTitle by remember { mutableStateOf("") }
    var memoryNote by remember { mutableStateOf("") }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFFAF5EE))
            .testTag("jagi_memories_screen")
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            contentPadding = PaddingValues(top = 16.dp, bottom = 48.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // TOP BAR
            item {
                Box(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier
                            .align(Alignment.CenterStart)
                            .size(40.dp)
                            .testTag("memories_back_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color(0xFF2C221E)
                        )
                    }

                    Text(
                        text = "MEMORIES",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFA8998D),
                        letterSpacing = 2.sp,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }

            // FLOATING COMPANION WITH WARM AURA GLOW
            item {
                Spacer(modifier = Modifier.height(28.dp))
                PixelCompanionPet(size = 56.dp, showAura = true)
                Spacer(modifier = Modifier.height(20.dp))
            }

            // HEADLINE & SUBTITLE
            item {
                Text(
                    text = buildAnnotatedString {
                        append("The album starts with ")
                        withStyle(SpanStyle(fontStyle = FontStyle.Italic, color = Color(0xFFD77A61))) {
                            append("one photo.")
                        }
                    },
                    fontFamily = FrauncesFontFamily,
                    fontSize = 26.sp,
                    color = Color(0xFF2C221E),
                    textAlign = TextAlign.Center,
                    lineHeight = 32.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Add a picture or a voice note from a moment together. Every one of them is kept for you.",
                    fontSize = 13.sp,
                    color = Color(0xFF8E8076),
                    textAlign = TextAlign.Center,
                    lineHeight = 19.sp,
                    modifier = Modifier.padding(horizontal = 24.dp)
                )
            }

            // BUTTON: "+ Add your first memory" (Rich dark chocolate)
            item {
                Spacer(modifier = Modifier.height(10.dp))
                Button(
                    onClick = { showAddDialog = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .bounceClick { showAddDialog = true }
                        .testTag("add_memory_button"),
                    shape = RoundedCornerShape(28.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E1E12)),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
                ) {
                    Text(
                        text = "+ Add your first memory",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFFFFF7F0)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // LINK: "Record a voice note 🎙️"
                TextButton(
                    onClick = {
                        repo.addMemory("Voice Note: Thinking of You", isVoice = true, duration = 12)
                    },
                    modifier = Modifier.bounceClick {
                        repo.addMemory("Voice Note: Thinking of You", isVoice = true, duration = 12)
                    }
                ) {
                    Text(
                        text = "Record a voice note 🎙️",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFFD77A61)
                    )
                }
            }

            // MEMORY ITEMS LIST OR DASHED EMPTY PLACEHOLDER
            if (memories.isEmpty()) {
                item {
                    Spacer(modifier = Modifier.height(24.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(22.dp))
                            .border(1.dp, Color(0xFFDACFBF), RoundedCornerShape(22.dp))
                            .background(Color(0xFFFAF5EE))
                            .padding(horizontal = 20.dp, vertical = 24.dp),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color(0xFFF3ECE2)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("🖼️", fontSize = 24.sp)
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            Column {
                                Text(
                                    text = "Your first memory goes here",
                                    fontFamily = FrauncesFontFamily,
                                    fontSize = 15.sp,
                                    color = Color(0xFF2C221E)
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "and the album grows from it",
                                    fontSize = 12.sp,
                                    color = Color(0xFF8E8076)
                                )
                            }
                        }
                    }
                }
            } else {
                items(memories) { mem ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFDF8)),
                        border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(Color(0xFFF0E7DA)))
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(if (mem.isVoiceNote) Color(0xFFFAF2EB) else Color(0xFFE8F0EA)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(if (mem.isVoiceNote) "🎙️" else "📸", fontSize = 22.sp)
                            }
                            Spacer(modifier = Modifier.width(14.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = mem.title,
                                    fontFamily = FrauncesFontFamily,
                                    fontSize = 16.sp,
                                    color = Color(0xFF2C221E)
                                )
                                Text(
                                    text = if (mem.isVoiceNote) "${mem.voiceDurationSeconds}s voice recording · ${mem.timestamp}" else mem.timestamp,
                                    fontSize = 11.sp,
                                    color = Color(0xFF8E8076)
                                )
                            }
                            Text("❤️", fontSize = 16.sp)
                        }
                    }
                }
            }
        }

        // DIALOG: ADD MEMORY
        if (showAddDialog) {
            AlertDialog(
                onDismissRequest = { showAddDialog = false },
                containerColor = Color(0xFFFFFDF8),
                shape = RoundedCornerShape(24.dp),
                title = {
                    Text("Add a shared memory", fontFamily = FrauncesFontFamily, fontSize = 20.sp)
                },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        OutlinedTextField(
                            value = memoryTitle,
                            onValueChange = { memoryTitle = it },
                            placeholder = { Text("e.g. Rainy coffee morning ☕") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp)
                        )
                        OutlinedTextField(
                            value = memoryNote,
                            onValueChange = { memoryNote = it },
                            placeholder = { Text("Add a sweet caption or note...") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp)
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            if (memoryTitle.isNotBlank()) {
                                repo.addMemory(memoryTitle, isVoice = false, note = memoryNote)
                                memoryTitle = ""
                                memoryNote = ""
                            }
                            showAddDialog = false
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF7C5A0)),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text("Save Memory", color = Color(0xFF332014), fontWeight = FontWeight.Bold)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showAddDialog = false }) {
                        Text("Cancel", color = Color(0xFF8E8076))
                    }
                }
            )
        }
    }
}
