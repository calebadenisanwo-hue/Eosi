package com.example.meridian.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Flight
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
import com.example.meridian.data.plan.PlanData
import com.example.meridian.data.repository.MeridianRepo
import com.example.ui.theme.FrauncesFontFamily
import com.example.ui.theme.MeridianApricot
import com.example.ui.theme.MeridianDuskBlue

@Composable
fun PlanScreen(
    repo: MeridianRepo,
    modifier: Modifier = Modifier
) {
    val activeUser by repo.activeUser.collectAsState()
    val partnerUser by repo.partnerUser.collectAsState()

    var showEditWhyDialog by remember { mutableStateOf(false) }
    var currentWhy by remember { mutableStateOf(PlanData.pinnedWhy) }

    var stages by remember { mutableStateOf(PlanData.initialStages) }
    var packingList by remember { mutableStateOf(PlanData.packingItems) }
    var itineraryList by remember { mutableStateOf(PlanData.itineraryItems) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("plan_screen_scroll"),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 100.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "Plan",
                fontFamily = FrauncesFontFamily,
                fontSize = 28.sp,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = "A concrete path from two different time zones to one shared front door.",
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        // Pinned "Why" Hero Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showEditWhyDialog = true }
                    .testTag("pinned_why_card"),
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = MeridianApricot.copy(alpha = 0.12f))
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "OUR PINNED 'WHY'",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = MeridianApricot
                        )
                        Icon(
                            Icons.Default.Edit,
                            contentDescription = "Edit Why",
                            tint = MeridianApricot,
                            modifier = Modifier.size(15.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "“$currentWhy”",
                        fontFamily = FrauncesFontFamily,
                        fontSize = 16.sp,
                        lineHeight = 23.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }

        // Shared Goal / Relocation Fund Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "RELOCATION & VISA FUND",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = MeridianDuskBlue
                        )
                        Text(
                            text = "€6,850 / €12,000 (57%)",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    LinearProgressIndicator(
                        progress = { 0.57f },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(CircleShape),
                        color = MeridianApricot,
                        trackColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Covers flights, visa apostilles, first month rent, and emergency reserve.",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        // Close the Distance Roadmap Header
        item {
            Text(
                text = "Close the Distance Roadmap",
                fontFamily = FrauncesFontFamily,
                fontSize = 20.sp,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = "4 clear stages to ending long distance permanently.",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        // 4 Roadmap Stages
        items(stages) { stage ->
            val isCurrent = stage.status == "IN_PROGRESS"
            val isCompleted = stage.status == "COMPLETED"

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isCurrent) MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f)
                    else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f)
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Stage ${stage.stageNumber}: ${stage.name}",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp,
                            color = if (isCurrent) MeridianApricot else MaterialTheme.colorScheme.onSurface
                        )
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(
                                    when (stage.status) {
                                        "COMPLETED" -> Color(0xFF4CAF50).copy(alpha = 0.15f)
                                        "IN_PROGRESS" -> MeridianApricot.copy(alpha = 0.15f)
                                        else -> MaterialTheme.colorScheme.surface
                                    }
                                )
                                .padding(horizontal = 8.dp, vertical = 3.dp)
                        ) {
                            Text(
                                text = when (stage.status) {
                                    "COMPLETED" -> "Completed ✓"
                                    "IN_PROGRESS" -> "In Progress"
                                    else -> "Upcoming"
                                },
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = when (stage.status) {
                                    "COMPLETED" -> Color(0xFF4CAF50)
                                    "IN_PROGRESS" -> MeridianApricot
                                    else -> MaterialTheme.colorScheme.onSurfaceVariant
                                }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = stage.description,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Tasks in this stage
                    stage.tasks.forEach { task ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 3.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = task.isCompleted,
                                onCheckedChange = { checked ->
                                    val updatedTasks = stage.tasks.map {
                                        if (it.id == task.id) it.copy(isCompleted = checked) else it
                                    }
                                    stages = stages.map {
                                        if (it.stageNumber == stage.stageNumber) it.copy(tasks = updatedTasks) else it
                                    }
                                },
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = task.title,
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.weight(1f)
                            )
                            Text(
                                text = task.assignedTo,
                                fontSize = 10.sp,
                                color = MeridianDuskBlue,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }
        }

        // Visit Planner Section
        item {
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "Next Visit Planner · Pune in 41 Days",
                fontFamily = FrauncesFontFamily,
                fontSize = 20.sp,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = "14 days planned together · Shared packing list & itinerary bucket list",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        // Split Packing List Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Split Packing List",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    packingList.forEach { item ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 3.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = item.isChecked,
                                onCheckedChange = { checked ->
                                    packingList = packingList.map {
                                        if (it.id == item.id) it.copy(isChecked = checked) else it
                                    }
                                },
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = item.name,
                                fontSize = 12.sp,
                                modifier = Modifier.weight(1f)
                            )
                            Text(
                                text = item.assignedUid,
                                fontSize = 11.sp,
                                color = MeridianApricot,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }
        }

        // Itinerary Bucket List
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Itinerary Bucket List",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    itineraryList.forEach { item ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 3.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "•", fontSize = 16.sp, color = MeridianApricot)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = item.activity,
                                fontSize = 12.sp,
                                modifier = Modifier.weight(1f)
                            )
                            Text(
                                text = item.category,
                                fontSize = 10.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }

    if (showEditWhyDialog) {
        var tempWhy by remember { mutableStateOf(currentWhy) }

        AlertDialog(
            onDismissRequest = { showEditWhyDialog = false },
            title = { Text("Edit Our Pinned 'Why'", fontFamily = FrauncesFontFamily) },
            text = {
                Column {
                    Text(
                        text = "A quiet sentence that reminds both of you what every late-night call and layover is for.",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(
                        value = tempWhy,
                        onValueChange = { tempWhy = it },
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 4
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        currentWhy = tempWhy
                        PlanData.pinnedWhy = tempWhy
                        showEditWhyDialog = false
                    }
                ) {
                    Text("Save & Confirm")
                }
            },
            dismissButton = {
                TextButton(onClick = { showEditWhyDialog = false }) { Text("Cancel") }
            },
            shape = RoundedCornerShape(20.dp)
        )
    }
}
