package com.example.meridian.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.meridian.data.repository.MeridianRepo
import com.example.meridian.ui.screens.care.AgreementsDialog
import com.example.meridian.ui.screens.care.PauseAndRepairDialog
import com.example.meridian.ui.screens.care.WeeklyCheckinDialog
import com.example.ui.theme.FrauncesFontFamily
import com.example.ui.theme.MeridianApricot
import com.example.ui.theme.MeridianDuskBlue

@Composable
fun CareScreen(
    repo: MeridianRepo,
    modifier: Modifier = Modifier
) {
    val activeUser by repo.activeUser.collectAsState()
    val partnerUser by repo.partnerUser.collectAsState()

    var showPauseSheet by remember { mutableStateOf(false) }
    var showAgreementsSheet by remember { mutableStateOf(false) }
    var showCheckinSheet by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("care_screen_scroll"),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp)
    ) {
        item {
            Text(
                text = "Care",
                fontFamily = FrauncesFontFamily,
                fontSize = 28.sp,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = "Tools to protect the relationship when distance makes things hard.",
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Prominent "Pause and Repair" button / card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("pause_and_repair_card"),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f))
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "PAUSE & REPAIR",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = MeridianApricot
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "When a conversation hurts across distance",
                        fontFamily = FrauncesFontFamily,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "One tap calls a 20-minute breathing pause. Then, both privately complete 4 grounding prompts: what I heard, what I felt, what I need, and one thing I can do. Answers reveal together.",
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        lineHeight = 18.sp
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                    Button(
                        onClick = { showPauseSheet = true },
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Text("Tap to Call a Pause 🕊️")
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Park It
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showPauseSheet = true },
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Park It",
                            fontFamily = FrauncesFontFamily,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Schedule discussion →",
                            fontSize = 11.sp,
                            color = MeridianDuskBlue,
                            fontWeight = FontWeight.Medium
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Never fight at 2am before sleep. Park difficult topics for the next shared awake window with a kind message so nobody is left spiraling.",
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Us Agreements
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showAgreementsSheet = true },
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Us Agreements",
                            fontFamily = FrauncesFontFamily,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "5 active agreements →",
                            fontSize = 11.sp,
                            color = MeridianApricot,
                            fontWeight = FontWeight.Medium
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "• Busy day rule: 'A quick goodnight ping is always enough.'\n• How we handle silence: 'No guilt if replies take hours.'\n• Next visit: 'Never leave an airport without the next date in mind.'",
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        lineHeight = 20.sp
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Weekly Check-in (Opt-in)
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showCheckinSheet = true },
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Weekly Check-in",
                            fontFamily = FrauncesFontFamily,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Week 40 Open →",
                            fontSize = 11.sp,
                            color = MeridianApricot,
                            fontWeight = FontWeight.Medium
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Five gentle reflection points (closeness, feeling heard, plan hope, effort balance, logistics). Revealed together without scores or grading.",
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }

    if (showPauseSheet) {
        PauseAndRepairDialog(
            partnerName = partnerUser.displayName,
            onDismiss = { showPauseSheet = false }
        )
    }

    if (showAgreementsSheet) {
        AgreementsDialog(
            onDismiss = { showAgreementsSheet = false }
        )
    }

    if (showCheckinSheet) {
        WeeklyCheckinDialog(
            partnerName = partnerUser.displayName,
            onDismiss = { showCheckinSheet = false }
        )
    }
}
