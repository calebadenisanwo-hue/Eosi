package com.example.meridian.ui.screens.dailyupdate

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
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
import com.example.meridian.data.model.DailyUpdate
import com.example.meridian.data.model.UserProfile
import com.example.ui.theme.FrauncesFontFamily
import com.example.ui.theme.MeridianApricot
import com.example.ui.theme.MeridianDuskBlue

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun DailyUpdateSheet(
    myProfile: UserProfile,
    partnerProfile: UserProfile,
    myUpdate: DailyUpdate?,
    partnerUpdate: DailyUpdate?,
    quietReactions: List<String>,
    onSaveUpdate: (List<String>, String, List<String>, String) -> Unit,
    onSendReaction: (String) -> Unit,
    onOpenLetterComposer: () -> Unit,
    onSendTap: () -> Unit,
    onDismiss: () -> Unit
) {
    val feelingOptions = listOf(
        "😊 happy", "🥰 loved", "😌 calm", "🤩 excited", "🙂 okay", "🤗 grateful",
        "😎 confident", "😴 tired", "🥱 drained", "😔 low", "😟 anxious", "😤 stressed",
        "😠 frustrated", "🫠 overwhelmed", "🥺 missing you", "😢 sad", "🤒 unwell", "😅 chaotic"
    )

    val actionOptions = listOf(
        "💼 working", "📚 studying", "🍳 cooking", "🏋️ working out", "🚶 out and about",
        "🛌 resting", "🎮 gaming", "🧹 chores", "🚗 commuting", "✈️ travelling",
        "👥 with friends", "👪 with family", "🎬 watching", "🎨 creating", "🛍️ errands",
        "📞 on a call", "🤒 sick day"
    )

    val selectedFeelings = remember { mutableStateListOf<String>().apply { myUpdate?.feelings?.let { addAll(it) } } }
    val selectedActions = remember { mutableStateListOf<String>().apply { myUpdate?.actions?.let { addAll(it) } } }
    var feelingNote by remember { mutableStateOf(myUpdate?.feelingNote ?: "") }
    var actionNote by remember { mutableStateOf(myUpdate?.actionNote ?: "") }

    val partnerHasHeavyFeeling = partnerUpdate?.feelings?.any { f ->
        f.contains("low") || f.contains("anxious") || f.contains("overwhelmed") || f.contains("sad") || f.contains("missing you")
    } == true

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = MaterialTheme.colorScheme.surface,
        modifier = Modifier.testTag("daily_update_sheet")
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
                    text = "Today, in two taps",
                    fontFamily = FrauncesFontFamily,
                    fontSize = 22.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
                IconButton(onClick = onDismiss) {
                    Icon(Icons.Default.Close, contentDescription = "Close")
                }
            }
            Text(
                text = "A low-effort snapshot. Stamped in your local time with zero guilt.",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            // Partner's update section if present
            if (partnerUpdate != null && partnerUpdate.feelings.isNotEmpty()) {
                Spacer(modifier = Modifier.height(14.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = "${partnerProfile.displayName} · Today",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MeridianApricot
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = partnerUpdate.feelings.joinToString("  "),
                            fontSize = 14.sp
                        )
                        if (partnerUpdate.feelingNote.isNotEmpty()) {
                            Text(
                                text = "“${partnerUpdate.feelingNote}”",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = partnerUpdate.actions.joinToString("  "),
                            fontSize = 13.sp
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // Quiet Reactions Row
                        Text(
                            text = "Send quiet reaction (no notification nag):",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            listOf("🤍 Hug", "👀 Tell me more", "📞 Call me?", "💬 Later?").forEach { item ->
                                val emoji = item.take(2).trim()
                                val isSent = quietReactions.contains(emoji)
                                FilterChip(
                                    selected = isSent,
                                    onClick = { onSendReaction(emoji) },
                                    label = { Text(item, fontSize = 11.sp) },
                                    shape = RoundedCornerShape(12.dp)
                                )
                            }
                        }

                        // Soft care prompt if heavy feeling
                        if (partnerHasHeavyFeeling) {
                            Spacer(modifier = Modifier.height(10.dp))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(MeridianApricot.copy(alpha = 0.12f))
                                    .padding(10.dp)
                            ) {
                                Column {
                                    Text(
                                        text = "${partnerProfile.displayName} might be having a tender day. Leave something gentle?",
                                        fontSize = 12.sp,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                        TextButton(onClick = {
                                            onDismiss()
                                            onOpenLetterComposer()
                                        }) {
                                            Text("Write letter ✉️", fontSize = 11.sp, color = MeridianApricot)
                                        }
                                        TextButton(onClick = {
                                            onSendTap()
                                        }) {
                                            Text("Send a tap 🤍", fontSize = 11.sp, color = MeridianApricot)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Feelings Section
            Text(
                text = "How are you feeling? (Pick up to 3)",
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(8.dp))

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                feelingOptions.forEach { feeling ->
                    val isSelected = selectedFeelings.contains(feeling)
                    FilterChip(
                        selected = isSelected,
                        onClick = {
                            if (isSelected) {
                                selectedFeelings.remove(feeling)
                            } else if (selectedFeelings.size < 3) {
                                selectedFeelings.add(feeling)
                            }
                        },
                        label = { Text(feeling, fontSize = 12.sp) },
                        shape = RoundedCornerShape(12.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = feelingNote,
                onValueChange = { if (it.length <= 80) feelingNote = it },
                placeholder = { Text("Feeling note (optional, max 80 chars)") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                maxLines = 2
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Actions Section
            Text(
                text = "What are you up to? (Pick up to 3)",
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(8.dp))

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                actionOptions.forEach { action ->
                    val isSelected = selectedActions.contains(action)
                    FilterChip(
                        selected = isSelected,
                        onClick = {
                            if (isSelected) {
                                selectedActions.remove(action)
                            } else if (selectedActions.size < 3) {
                                selectedActions.add(action)
                            }
                        },
                        label = { Text(action, fontSize = 12.sp) },
                        shape = RoundedCornerShape(12.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = actionNote,
                onValueChange = { if (it.length <= 80) actionNote = it },
                placeholder = { Text("Action note (optional, max 80 chars)") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                maxLines = 2
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    onSaveUpdate(selectedFeelings.toList(), feelingNote, selectedActions.toList(), actionNote)
                    onDismiss()
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text("Save Today's Update")
            }
        }
    }
}
