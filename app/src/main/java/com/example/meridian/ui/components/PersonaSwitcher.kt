package com.example.meridian.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.meridian.data.model.DemoSeedData
import com.example.ui.theme.MeridianApricot

@Composable
fun PersonaSwitcher(
    activeUid: String,
    onSelectPersona: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .testTag("persona_switcher")
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f))
            .padding(2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        PersonaPill(
            name = "Ayaan",
            emoji = "☕",
            isSelected = activeUid == DemoSeedData.AYAAN_UID,
            onClick = { onSelectPersona(DemoSeedData.AYAAN_UID) }
        )
        PersonaPill(
            name = "Linnea",
            emoji = "🌿",
            isSelected = activeUid == DemoSeedData.LINNEA_UID,
            onClick = { onSelectPersona(DemoSeedData.LINNEA_UID) }
        )
    }
}

@Composable
private fun PersonaPill(
    name: String,
    emoji: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val bg = if (isSelected) MeridianApricot else Color.Transparent
    val textCol = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(14.dp))
            .background(bg)
            .clickable(onClick = onClick)
            .padding(horizontal = 10.dp, vertical = 5.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "$emoji $name",
            fontSize = 12.sp,
            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
            color = textCol
        )
    }
}
