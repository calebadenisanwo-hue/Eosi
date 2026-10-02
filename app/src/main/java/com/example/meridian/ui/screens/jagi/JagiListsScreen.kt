package com.example.meridian.ui.screens.jagi

import androidx.activity.compose.BackHandler
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
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
import com.example.meridian.data.jagi.SharedListItem
import com.example.meridian.ui.components.PixelCompanionPet
import com.example.meridian.ui.components.bounceClick
import com.example.ui.theme.FrauncesFontFamily

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JagiListsScreen(
    repo: JagiRepo,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { onBack() }
    val lists by repo.sharedLists.collectAsState()

    var activeListDetail by remember { mutableStateOf<SharedListItem?>(null) }
    var showCreateBlankDialog by remember { mutableStateOf(false) }
    var blankListTitle by remember { mutableStateOf("") }
    var newItemText by remember { mutableStateOf("") }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFFAF5EE))
            .testTag("jagi_lists_screen")
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
                Box(modifier = Modifier.fillMaxWidth()) {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier
                            .align(Alignment.CenterStart)
                            .size(40.dp)
                            .testTag("lists_back_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color(0xFF2C221E)
                        )
                    }

                    Text(
                        text = "SHARED LISTS",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFA8998D),
                        letterSpacing = 2.sp,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }

            // FLOATING COMPANION WITH AURA
            item {
                Spacer(modifier = Modifier.height(28.dp))
                PixelCompanionPet(size = 56.dp, showAura = true)
                Spacer(modifier = Modifier.height(20.dp))
            }

            // HEADLINE & SUBTITLE
            item {
                Text(
                    text = buildAnnotatedString {
                        append("Start a list ")
                        withStyle(SpanStyle(fontStyle = FontStyle.Italic, color = Color(0xFFD77A61))) {
                            append("together.")
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
                    text = "Two you might want. Tap one and it's yours to fill.",
                    fontSize = 13.sp,
                    color = Color(0xFF8E8076),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 20.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))
            }

            // LIST CARDS
            items(lists) { list ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .bounceClick { activeListDetail = list }
                        .testTag("list_card_${list.id}"),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFDF8)),
                    shape = RoundedCornerShape(22.dp),
                    border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(Color(0xFFF0E7DA)))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(46.dp)
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(Color(0xFFFAF2EB)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(list.emoji, fontSize = 24.sp)
                            }
                            Spacer(modifier = Modifier.width(14.dp))
                            Column {
                                Text(
                                    text = list.title,
                                    fontFamily = FrauncesFontFamily,
                                    fontSize = 17.sp,
                                    color = Color(0xFF2C221E)
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = list.description,
                                    fontSize = 12.sp,
                                    color = Color(0xFF8E8076)
                                )
                            }
                        }

                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add item",
                            tint = Color(0xFFD77A61),
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }

            // BUTTON: "+ Start a blank list"
            item {
                Spacer(modifier = Modifier.height(8.dp))
                TextButton(
                    onClick = { showCreateBlankDialog = true },
                    modifier = Modifier.bounceClick { showCreateBlankDialog = true }
                ) {
                    Text(
                        text = "+ Start a blank list",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFFD77A61)
                    )
                }
            }
        }

        // DETAIL SHEET: VIEW / ADD ITEMS TO ACTIVE LIST
        if (activeListDetail != null) {
            val currentList = activeListDetail!!
            ModalBottomSheet(
                onDismissRequest = { activeListDetail = null },
                containerColor = Color(0xFFFAF5EE),
                shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 6.dp)
                        .padding(bottom = 36.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(currentList.emoji, fontSize = 24.sp)
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = currentList.title,
                                fontFamily = FrauncesFontFamily,
                                fontSize = 22.sp,
                                color = Color(0xFF2C221E)
                            )
                        }
                    }

                    Text(
                        text = currentList.description,
                        fontSize = 13.sp,
                        color = Color(0xFF8E8076)
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    // Existing items
                    if (currentList.items.isEmpty()) {
                        Text(
                            text = "No items yet. Add your first plan!",
                            fontSize = 13.sp,
                            color = Color(0xFFA8998D)
                        )
                    } else {
                        currentList.items.forEach { item ->
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(14.dp),
                                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFDF8)),
                                border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(Color(0xFFF0E7DA)))
                            ) {
                                Row(
                                    modifier = Modifier.padding(14.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(20.dp)
                                            .clip(CircleShape)
                                            .border(1.5.dp, Color(0xFFD77A61), CircleShape)
                                    )
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Text(
                                        text = item,
                                        fontSize = 14.sp,
                                        color = Color(0xFF2C221E)
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Input to add a new item
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedTextField(
                            value = newItemText,
                            onValueChange = { newItemText = it },
                            placeholder = { Text("Add an idea for us...") },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(16.dp)
                        )
                        Button(
                            onClick = {
                                if (newItemText.isNotBlank()) {
                                    repo.addListItem(currentList.id, newItemText)
                                    activeListDetail = currentList.copy(items = currentList.items + newItemText)
                                    newItemText = ""
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF7C5A0)),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Text("Add", color = Color(0xFF332014), fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // DIALOG: CREATE BLANK LIST
        if (showCreateBlankDialog) {
            AlertDialog(
                onDismissRequest = { showCreateBlankDialog = false },
                containerColor = Color(0xFFFFFDF8),
                shape = RoundedCornerShape(24.dp),
                title = {
                    Text("Start a new shared list", fontFamily = FrauncesFontFamily, fontSize = 20.sp)
                },
                text = {
                    Column {
                        OutlinedTextField(
                            value = blankListTitle,
                            onValueChange = { blankListTitle = it },
                            placeholder = { Text("e.g. Movies to watch on call 🎬") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp)
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            if (blankListTitle.isNotBlank()) {
                                repo.createBlankList(blankListTitle)
                                blankListTitle = ""
                            }
                            showCreateBlankDialog = false
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF7C5A0)),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text("Create List", color = Color(0xFF332014), fontWeight = FontWeight.Bold)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showCreateBlankDialog = false }) {
                        Text("Cancel", color = Color(0xFF8E8076))
                    }
                }
            )
        }
    }
}
