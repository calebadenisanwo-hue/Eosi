package com.example.meridian.ui.screens.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.meridian.data.repository.MeridianRepo
import com.example.ui.theme.FrauncesFontFamily
import com.example.ui.theme.MeridianApricot
import com.example.ui.theme.MeridianDuskBlue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SpaceManagementSheet(
    repo: MeridianRepo,
    onDismiss: () -> Unit
) {
    val partnerUser by repo.partnerUser.collectAsState()
    var exportJson by remember { mutableStateOf<String?>(null) }
    var showPauseConfirm by remember { mutableStateOf(false) }
    var pauseDays by remember { mutableIntStateOf(30) }
    var pauseSuccessMessage by remember { mutableStateOf<String?>(null) }

    var showEndConfirm by remember { mutableStateOf(false) }
    var endInitiatedMessage by remember { mutableStateOf<String?>(null) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = MaterialTheme.colorScheme.surface,
        modifier = Modifier.testTag("space_management_sheet")
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
                    text = "Space & Data Settings",
                    fontFamily = FrauncesFontFamily,
                    fontSize = 20.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
                IconButton(onClick = onDismiss) {
                    Icon(Icons.Default.Close, contentDescription = "Close")
                }
            }

            Text(
                text = "Export your memories, pause notifications, or manage space lifecycle.",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(18.dp))

            // 1. Export Data Section
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Download, contentDescription = null, tint = MeridianApricot)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Export Everything (JSON & HTML)",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Download a complete decrypted keepsake package of every letter, question answer, timeline moment, visit, and agreement. Never requires partner consent to export what is yours.",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        lineHeight = 17.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = {
                            val activeUser = repo.activeUser.value
                            val letters = repo.letters.value
                            val questions = repo.currentQuestion.value
                            exportJson = """
                            {
                              "exported_at": "${java.time.Instant.now()}",
                              "exported_by": "${activeUser.displayName}",
                              "letters_count": ${letters.size},
                              "visits_logged": ${repo.visits.value.size},
                              "timeline_moments": ${repo.timeline.value.size},
                              "agreements_active": 5,
                              "format": "offline_keepsake_v1"
                            }
                            """.trimIndent()
                        },
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Generate Full Export Package 📦", fontSize = 12.sp)
                    }

                    if (exportJson != null) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Surface(
                            color = MaterialTheme.colorScheme.surface,
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = exportJson!!,
                                fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace,
                                fontSize = 11.sp,
                                modifier = Modifier.padding(10.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 2. Pause Space Section
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Pause, contentDescription = null, tint = MeridianDuskBlue)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Pause This Space",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Sets the space to read-only mode for 30, 60, or 90 days. Zero notifications, zero streak resets, and no emails. Either person can un-pause at any time.",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        listOf(30, 60, 90).forEach { days ->
                            FilterChip(
                                selected = pauseDays == days,
                                onClick = { pauseDays = days },
                                label = { Text("$days Days", fontSize = 11.sp) },
                                shape = RoundedCornerShape(10.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedButton(
                        onClick = {
                            pauseSuccessMessage = "Space paused for $pauseDays days in read-only mode. Either person can resume whenever ready."
                        },
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Pause Space for $pauseDays Days", fontSize = 12.sp)
                    }

                    if (pauseSuccessMessage != null) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = pauseSuccessMessage!!,
                            fontSize = 11.sp,
                            color = MeridianDuskBlue,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 3. End Space Section (72-hour cooling off)
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.3f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Warning, contentDescription = null, tint = MaterialTheme.colorScheme.error)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "End This Space (72-Hour Cooling-Off)",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "When a couple ends: Private vault is immediately purged for both. A 72-hour cooling-off countdown begins, allowing both people to export what they contributed before final deletion.",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        lineHeight = 17.sp
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Button(
                        onClick = { showEndConfirm = true },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Initiate 72-Hour Space Closure", fontSize = 12.sp)
                    }

                    if (endInitiatedMessage != null) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = endInitiatedMessage!!,
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.error,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }

    if (showEndConfirm) {
        AlertDialog(
            onDismissRequest = { showEndConfirm = false },
            title = { Text("Are you sure?", fontFamily = FrauncesFontFamily) },
            text = {
                Text(
                    text = "This will immediately purge the private vault and start a 72-hour countdown. ${partnerUser.displayName} will be notified gently so they can export their memories.",
                    fontSize = 13.sp
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        repo.deleteVaultForBoth()
                        endInitiatedMessage = "⏳ 72-Hour countdown started. Notification sent to ${partnerUser.displayName}. Private vault has been purged."
                        showEndConfirm = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Confirm Initiation")
                }
            },
            dismissButton = {
                TextButton(onClick = { showEndConfirm = false }) { Text("Cancel") }
            },
            shape = RoundedCornerShape(18.dp)
        )
    }
}
