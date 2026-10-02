package com.example.meridian.ui.screens.jagi

import androidx.activity.compose.BackHandler
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ChevronRight
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
import com.example.meridian.data.jagi.JagiRepo
import com.example.meridian.ui.components.PixelCompanionPet
import com.example.meridian.ui.components.PixelHeart
import com.example.meridian.ui.components.bounceClick
import com.example.ui.theme.FrauncesFontFamily

@Composable
fun JagiSettingsScreen(
    repo: JagiRepo,
    onBack: () -> Unit,
    onOpenUsSettings: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { onBack() }
    val coupleData by repo.coupleData.collectAsState()

    var showIconPickerSheet by remember { mutableStateOf(false) }
    var showToastMessage by remember { mutableStateOf<String?>(null) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFFAF5EE))
            .testTag("jagi_settings_screen")
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            contentPadding = PaddingValues(top = 16.dp, bottom = 48.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // TOP BAR
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Settings",
                        fontFamily = FrauncesFontFamily,
                        fontSize = 28.sp,
                        color = Color(0xFF2C221E)
                    )
                    IconButton(onClick = onBack, modifier = Modifier.size(36.dp)) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = Color(0xFF2C221E))
                    }
                }
            }

            // YOU
            item {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "YOU",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFA8998D),
                        letterSpacing = 1.2.sp
                    )

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .bounceClick { showToastMessage = "Kola's profile: Active in Europe/London" },
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFDF8)),
                        shape = RoundedCornerShape(20.dp),
                        border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(Color(0xFFF0E7DA)))
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(46.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFF4A3B32)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text("🧑🏾", fontSize = 24.sp)
                                }
                                Spacer(modifier = Modifier.width(14.dp))
                                Column {
                                    Text(
                                        text = coupleData.userName,
                                        fontFamily = FrauncesFontFamily,
                                        fontSize = 17.sp,
                                        color = Color(0xFF2C221E)
                                    )
                                    Text(
                                        text = "Profile & account",
                                        fontSize = 12.sp,
                                        color = Color(0xFF8E8076)
                                    )
                                }
                            }
                            Icon(Icons.Default.ChevronRight, contentDescription = null, tint = Color(0xFFC0B3A6))
                        }
                    }
                }
            }

            // YOUR COUPLE
            item {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "YOUR COUPLE",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFA8998D),
                        letterSpacing = 1.2.sp
                    )

                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFDF8)),
                        shape = RoundedCornerShape(20.dp),
                        border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(Color(0xFFF0E7DA)))
                    ) {
                        Column {
                            SettingsItemRow(
                                emoji = "💖",
                                title = "Our couple",
                                subtitle = "Companion, anniversary, your dates",
                                onClick = onOpenUsSettings
                            )
                            HorizontalDivider(color = Color(0xFFF6EFE6), thickness = 0.8.dp)
                            SettingsItemRow(
                                emoji = "📍",
                                title = "Location",
                                subtitle = "Off · you can't see how far apart you are",
                                onClick = { showToastMessage = "Location privacy is active." }
                            )
                        }
                    }
                }
            }

            // PLUS BANNER
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .bounceClick { showToastMessage = "Plus: Unlimited photos, custom audio, and cloud sync." },
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF2E1E12)),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = "PLUS",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFD77A61),
                                letterSpacing = 1.sp
                            )
                            Text(
                                text = "Keep every memory",
                                fontFamily = FrauncesFontFamily,
                                fontSize = 17.sp,
                                color = Color(0xFFFFF7F0)
                            )
                            Text(
                                text = "Your whole story, from day one",
                                fontSize = 11.sp,
                                color = Color(0xFFC7B7AB)
                            )
                        }
                        Text("→", color = Color.White, fontSize = 18.sp)
                    }
                }
            }

            // NOTIFICATIONS
            item {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "NOTIFICATIONS",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFA8998D),
                        letterSpacing = 1.2.sp
                    )

                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFDF8)),
                        shape = RoundedCornerShape(20.dp),
                        border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(Color(0xFFF0E7DA)))
                    ) {
                        Column {
                            SettingsItemRow(
                                emoji = "🔔",
                                title = "Notifications",
                                subtitle = "Choose what reaches you",
                                onClick = {}
                            )
                            HorizontalDivider(color = Color(0xFFF6EFE6), thickness = 0.8.dp)
                            SettingsItemRow(
                                emoji = "🌅",
                                title = "Good morning & good night",
                                subtitle = "When the one-tap send appears",
                                onClick = {}
                            )
                        }
                    }
                }
            }

            // WIDGETS
            item {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "WIDGETS",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFA8998D),
                        letterSpacing = 1.2.sp
                    )

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .bounceClick { showToastMessage = "Long-press your phone home screen to place the Eos widget!" },
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFDF8)),
                        shape = RoundedCornerShape(20.dp),
                        border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(Color(0xFFF0E7DA)))
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(Color(0xFFE8F0EA)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text("🪟", fontSize = 18.sp)
                                }
                                Spacer(modifier = Modifier.width(14.dp))
                                Column {
                                    Text(
                                        text = "Widget gallery",
                                        fontFamily = FrauncesFontFamily,
                                        fontSize = 16.sp,
                                        color = Color(0xFF2C221E)
                                    )
                                    Text(
                                        text = "Put us on your home screen",
                                        fontSize = 12.sp,
                                        color = Color(0xFF8E8076)
                                    )
                                }
                            }
                            Icon(Icons.Default.ChevronRight, contentDescription = null, tint = Color(0xFFC0B3A6))
                        }
                    }
                }
            }

            // APP
            item {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "APP",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFA8998D),
                        letterSpacing = 1.2.sp
                    )

                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFDF8)),
                        shape = RoundedCornerShape(20.dp),
                        border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(Color(0xFFF0E7DA)))
                    ) {
                        Column {
                            // Distance toggle (KM / MI)
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text("📐", fontSize = 18.sp)
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Text("Distance", fontSize = 14.sp, fontWeight = FontWeight.Medium, color = Color(0xFF2C221E))
                                }

                                Row(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(Color(0xFFF2ECE1))
                                        .padding(3.dp)
                                ) {
                                    UnitTogglePill(
                                        text = "KM",
                                        isSelected = coupleData.distanceUnit == "KM"
                                    ) {
                                        repo.toggleDistanceUnit()
                                    }
                                    UnitTogglePill(
                                        text = "MI",
                                        isSelected = coupleData.distanceUnit == "MI"
                                    ) {
                                        repo.toggleDistanceUnit()
                                    }
                                }
                            }

                            HorizontalDivider(color = Color(0xFFF6EFE6), thickness = 0.8.dp)

                            // Temperature toggle (°C / °F)
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text("🌡️", fontSize = 18.sp)
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Text("Temperature", fontSize = 14.sp, fontWeight = FontWeight.Medium, color = Color(0xFF2C221E))
                                }

                                Row(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(Color(0xFFF2ECE1))
                                        .padding(3.dp)
                                ) {
                                    UnitTogglePill(
                                        text = "°C",
                                        isSelected = coupleData.temperatureUnit == "°C"
                                    ) {
                                        repo.toggleTemperatureUnit()
                                    }
                                    UnitTogglePill(
                                        text = "°F",
                                        isSelected = coupleData.temperatureUnit == "°F"
                                    ) {
                                        repo.toggleTemperatureUnit()
                                    }
                                }
                            }

                            HorizontalDivider(color = Color(0xFFF6EFE6), thickness = 0.8.dp)

                            // App Icon Picker trigger
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { showIconPickerSheet = true }
                                    .padding(horizontal = 16.dp, vertical = 14.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text("🎨", fontSize = 18.sp)
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Text("App icon & theme", fontSize = 14.sp, fontWeight = FontWeight.Medium, color = Color(0xFF2C221E))
                                }

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(coupleData.selectedAppIcon, fontSize = 13.sp, color = Color(0xFF8E8076))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Icon(Icons.Default.ChevronRight, contentDescription = null, tint = Color(0xFFC0B3A6))
                                }
                            }
                        }
                    }
                }
            }
        }

        // APP ICON SELECTION MODAL SHEET (Screenshot 10)
        if (showIconPickerSheet) {
            AppIconPickerSheet(
                selectedIcon = coupleData.selectedAppIcon,
                onSelectIcon = { icon ->
                    repo.setSelectedAppIcon(icon)
                    showIconPickerSheet = false
                    showToastMessage = "Home icon updated to $icon!"
                },
                onDismiss = { showIconPickerSheet = false }
            )
        }
    }
}

@Composable
private fun SettingsItemRow(
    emoji: String,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
            Text(emoji, fontSize = 18.sp)
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF2C221E)
                )
                Text(
                    text = subtitle,
                    fontSize = 11.sp,
                    color = Color(0xFF8E8076)
                )
            }
        }
        Icon(Icons.Default.ChevronRight, contentDescription = null, tint = Color(0xFFC0B3A6))
    }
}

@Composable
private fun UnitTogglePill(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(9.dp))
            .background(if (isSelected) Color(0xFFFFFDF8) else Color.Transparent)
            .bounceClick { onClick() }
            .padding(horizontal = 14.dp, vertical = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = 12.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            color = if (isSelected) Color(0xFF2C221E) else Color(0xFF8E8076)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AppIconPickerSheet(
    selectedIcon: String,
    onSelectIcon: (String) -> Unit,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = Color(0xFFFAF5EE),
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 6.dp)
                .padding(bottom = 36.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "APP ICON",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFA8998D),
                        letterSpacing = 1.5.sp
                    )
                    Text(
                        text = "Choose the face Eos wears on your Home screen.",
                        fontSize = 12.sp,
                        color = Color(0xFF8E8076)
                    )
                }
                IconButton(onClick = onDismiss) {
                    Icon(Icons.Default.Close, contentDescription = "Close", tint = Color(0xFF2C221E))
                }
            }

            // 4x2 Grid of Icons
            val icons = listOf(
                "Default" to "❤️",
                "Sweetheart" to "🥰",
                "Blush" to "🤎",
                "Midnight" to "🌌",
                "자기" to "🇰🇷",
                "Cocoa" to "🤍",
                "Momo" to "🐣",
                "Companion" to "⭐"
            )

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                icons.take(4).forEach { (name, emoji) ->
                    AppIconCard(
                        name = name,
                        emoji = emoji,
                        isSelected = selectedIcon == name,
                        onSelect = { onSelectIcon(name) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                icons.drop(4).forEach { (name, emoji) ->
                    AppIconCard(
                        name = name,
                        emoji = emoji,
                        isSelected = selectedIcon == name,
                        onSelect = { onSelectIcon(name) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
private fun AppIconCard(
    name: String,
    emoji: String,
    isSelected: Boolean,
    onSelect: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .bounceClick { onSelect() },
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFDF8)),
        border = CardDefaults.outlinedCardBorder().copy(
            brush = androidx.compose.ui.graphics.SolidColor(
                if (isSelected) Color(0xFFD77A61) else Color(0xFFF0E7DA)
            )
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp, horizontal = 4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color(0xFFF5EEE4)),
                contentAlignment = Alignment.Center
            ) {
                Text(emoji, fontSize = 24.sp)
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = name,
                fontSize = 11.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                color = Color(0xFF2C221E)
            )
        }
    }
}
