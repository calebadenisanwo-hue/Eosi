package com.example.meridian.ui.screens.vault

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
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
import com.example.meridian.data.model.VaultItem
import com.example.meridian.data.questions.SpicyQuestionsData
import com.example.meridian.data.repository.MeridianRepo
import com.example.meridian.data.vault.FlirtPromptsData
import com.example.ui.theme.FrauncesFontFamily
import com.example.ui.theme.MeridianApricot
import com.example.ui.theme.MeridianDeepNight
import com.example.ui.theme.MeridianDuskBlue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VaultScreen(
    repo: MeridianRepo,
    onQuickHide: () -> Unit,
    modifier: Modifier = Modifier
) {
    val activeUser by repo.activeUser.collectAsState()
    val partnerUser by repo.partnerUser.collectAsState()
    val vaultItems by repo.vaultItems.collectAsState()
    val heartbeatCount by repo.heartbeatPulseCount.collectAsState()

    var showAddNoteDialog by remember { mutableStateOf(false) }
    var selectedItem by remember { mutableStateOf<VaultItem?>(null) }
    var showDeleteConfirmDialog by remember { mutableStateOf(false) }

    // Flirt Prompts rotation
    var currentFlirtIndex by remember { mutableIntStateOf(0) }
    val currentFlirt = FlirtPromptsData.prompts[currentFlirtIndex]

    // Spicy questions
    var currentSpicyIndex by remember { mutableIntStateOf(0) }
    val currentSpicy = SpicyQuestionsData.questions[currentSpicyIndex]

    // Heartbeat pulse animation
    val infiniteTransition = rememberInfiniteTransition(label = "heartbeat")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1.0f,
        targetValue = 1.25f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 650, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "heartPulse"
    )

    Scaffold(
        topBar = {
            Surface(
                color = MaterialTheme.colorScheme.background,
                tonalElevation = 2.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Lock, contentDescription = null, tint = MeridianApricot, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Private Vault",
                            fontFamily = FrauncesFontFamily,
                            fontSize = 20.sp,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        // Quick Hide Button: immediately closes and locks vault
                        Button(
                            onClick = onQuickHide,
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                            shape = RoundedCornerShape(12.dp),
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                            modifier = Modifier.testTag("quick_hide_button")
                        ) {
                            Text("Quick Hide 👁️", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurface)
                        }

                        IconButton(onClick = { showDeleteConfirmDialog = true }) {
                            Icon(Icons.Outlined.Delete, contentDescription = "Delete vault", tint = MaterialTheme.colorScheme.error)
                        }
                    }
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddNoteDialog = true },
                containerColor = MeridianApricot,
                contentColor = Color.White,
                shape = CircleShape,
                modifier = Modifier.testTag("add_vault_item_fab")
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add private item")
            }
        },
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .testTag("vault_scroll"),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Section 1: Heartbeat Taps
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(22.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "HEARTBEAT TAPS",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = MeridianApricot
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "A synchronized rhythmic pulse across continents.",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Box(
                            modifier = Modifier
                                .size((80 * pulseScale).dp)
                                .clip(CircleShape)
                                .background(MeridianApricot.copy(alpha = 0.2f))
                                .clickable { repo.sendHeartbeat() },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Filled.Favorite,
                                contentDescription = "Heartbeat",
                                tint = MeridianApricot,
                                modifier = Modifier.size(36.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = if (heartbeatCount > 0) "Pulses sent today: $heartbeatCount" else "Tap the heart to pulse ${partnerUser.displayName}'s phone",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }

            // Section 2: Tasteful Flirt Prompts
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(22.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f))
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "FLIRT PROMPT · INTIMATE & WARM",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = MeridianDuskBlue
                            )
                            IconButton(
                                onClick = {
                                    currentFlirtIndex = (currentFlirtIndex + 1) % FlirtPromptsData.prompts.size
                                },
                                modifier = Modifier.size(28.dp)
                            ) {
                                Icon(Icons.Default.Refresh, contentDescription = "Next prompt", modifier = Modifier.size(16.dp))
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "“$currentFlirt”",
                            fontFamily = FrauncesFontFamily,
                            fontSize = 16.sp,
                            lineHeight = 22.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        Spacer(modifier = Modifier.height(12.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedButton(
                                onClick = {
                                    repo.addVaultItem("Prompt for ${partnerUser.displayName}", currentFlirt, "note", false)
                                },
                                shape = RoundedCornerShape(12.dp),
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                            ) {
                                Text("Send as Private Note 💌", fontSize = 11.sp)
                            }
                        }
                    }
                }
            }

            // Section 3: Spicy Question Deck
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(22.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f))
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "SPICY QUESTION DECK (18+)",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = MeridianApricot
                            )
                            TextButton(
                                onClick = {
                                    currentSpicyIndex = (currentSpicyIndex + 1) % SpicyQuestionsData.questions.size
                                },
                                contentPadding = PaddingValues(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text("Next Question →", fontSize = 11.sp)
                            }
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "“${currentSpicy.promptText}”",
                            fontFamily = FrauncesFontFamily,
                            fontSize = 16.sp,
                            lineHeight = 22.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Reveal-together: neither sees the other's answer until both respond.",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            // Section 4: Private Notes & View-Once items
            item {
                Text(
                    text = "Vault Items",
                    fontFamily = FrauncesFontFamily,
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = "End-to-end encrypted with your secondary Vault Key. View-once items auto-destruct.",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            items(vaultItems) { item ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { selectedItem = item },
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f))
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = if (item.viewOnce) "👁️" else "🔒", fontSize = 22.sp)
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = item.title,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = if (item.viewOnce) "View-once item · Tap to open" else "Tap to read private note",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        if (item.viewOnce) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(MeridianApricot.copy(alpha = 0.15f))
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text("View-once", fontSize = 10.sp, color = MeridianApricot, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }

    // View Vault Item Dialog
    if (selectedItem != null) {
        val item = selectedItem!!
        AlertDialog(
            onDismissRequest = {
                if (item.viewOnce) repo.markVaultItemViewed(item.id)
                selectedItem = null
            },
            title = { Text(item.title, fontFamily = FrauncesFontFamily) },
            text = {
                Column {
                    if (item.viewOnce) {
                        Text(
                            text = "⚠️ View-once: this item will be permanently deleted after closing.",
                            fontSize = 11.sp,
                            color = MeridianApricot,
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                    Text(text = item.body, fontSize = 14.sp, lineHeight = 20.sp)
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (item.viewOnce) repo.markVaultItemViewed(item.id)
                        selectedItem = null
                    }
                ) {
                    Text(if (item.viewOnce) "Close & Delete" else "Done")
                }
            },
            shape = RoundedCornerShape(20.dp)
        )
    }

    // Add Vault Item Dialog
    if (showAddNoteDialog) {
        var newTitle by remember { mutableStateOf("") }
        var newBody by remember { mutableStateOf("") }
        var isViewOnce by remember { mutableStateOf(false) }

        AlertDialog(
            onDismissRequest = { showAddNoteDialog = false },
            title = { Text("New Private Item", fontFamily = FrauncesFontFamily) },
            text = {
                Column {
                    OutlinedTextField(
                        value = newTitle,
                        onValueChange = { newTitle = it },
                        label = { Text("Title") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = newBody,
                        onValueChange = { newBody = it },
                        label = { Text("Message / Note") },
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 4
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("View-once mode", fontSize = 13.sp)
                        Switch(checked = isViewOnce, onCheckedChange = { isViewOnce = it })
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (newTitle.isNotBlank() && newBody.isNotBlank()) {
                            repo.addVaultItem(newTitle, newBody, if (isViewOnce) "view_once" else "note", isViewOnce)
                            showAddNoteDialog = false
                        }
                    }
                ) {
                    Text("Save in Vault 🔒")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddNoteDialog = false }) { Text("Cancel") }
            },
            shape = RoundedCornerShape(20.dp)
        )
    }

    // Delete Vault Confirmation
    if (showDeleteConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirmDialog = false },
            title = { Text("Delete Vault for Both?", fontFamily = FrauncesFontFamily) },
            text = {
                Text(
                    text = "This will permanently purge all private items, notes, and the Vault Key for both you and ${partnerUser.displayName}. This cannot be undone.",
                    fontSize = 13.sp
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        repo.deleteVaultForBoth()
                        showDeleteConfirmDialog = false
                        onQuickHide()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Permanently Delete")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirmDialog = false }) { Text("Cancel") }
            },
            shape = RoundedCornerShape(20.dp)
        )
    }
}
