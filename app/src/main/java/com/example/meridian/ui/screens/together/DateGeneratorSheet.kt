package com.example.meridian.ui.screens.together

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.meridian.data.dates.DateIdea
import com.example.meridian.data.dates.DateIdeasData
import com.example.ui.theme.FrauncesFontFamily
import com.example.ui.theme.MeridianApricot
import com.example.ui.theme.MeridianDuskBlue

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun DateGeneratorSheet(
    onLogToTimeline: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var selectedEnergy by remember { mutableStateOf("All") }
    var selectedCost by remember { mutableStateOf("All") }
    var loggedDateMessage by remember { mutableStateOf<String?>(null) }

    val filteredIdeas = remember(selectedEnergy, selectedCost) {
        DateIdeasData.ideas.filter { idea ->
            (selectedEnergy == "All" || idea.energy == selectedEnergy) &&
            (selectedCost == "All" || idea.cost == selectedCost)
        }
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = MaterialTheme.colorScheme.surface,
        modifier = Modifier.testTag("date_generator_sheet")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 6.dp)
                .padding(bottom = 36.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Date Generator",
                    fontFamily = FrauncesFontFamily,
                    fontSize = 22.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
                IconButton(onClick = onDismiss) {
                    Icon(Icons.Default.Close, contentDescription = "Close")
                }
            }

            Text(
                text = "40 warm, inventive date ideas tailored for distance, time zone offsets, and varied energy levels.",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Filters
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                listOf("All", "Low", "Medium", "High").forEach { energy ->
                    FilterChip(
                        selected = selectedEnergy == energy,
                        onClick = { selectedEnergy = energy },
                        label = { Text(if (energy == "All") "Any Energy" else "$energy Energy", fontSize = 11.sp) },
                        shape = RoundedCornerShape(12.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                listOf("All", "Free", "Low Cost").forEach { cost ->
                    FilterChip(
                        selected = selectedCost == cost,
                        onClick = { selectedCost = cost },
                        label = { Text(cost, fontSize = 11.sp) },
                        shape = RoundedCornerShape(12.dp)
                    )
                }
            }

            if (loggedDateMessage != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = loggedDateMessage!!,
                    fontSize = 12.sp,
                    color = MeridianApricot,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(filteredIdeas) { idea ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = idea.title,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 14.sp,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "${idea.energy} · ${idea.cost}",
                                    fontSize = 10.sp,
                                    color = MeridianApricot,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = idea.description,
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                lineHeight = 17.sp
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End
                            ) {
                                TextButton(
                                    onClick = {
                                        onLogToTimeline("Date Night: ${idea.title}")
                                        loggedDateMessage = "✨ Logged '${idea.title}' to Our Story timeline!"
                                    }
                                ) {
                                    Text("We did this! 📖", fontSize = 11.sp)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
