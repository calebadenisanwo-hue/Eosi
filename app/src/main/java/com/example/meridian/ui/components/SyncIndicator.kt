package com.example.meridian.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.meridian.data.model.SyncStatus
import com.example.ui.theme.MeridianApricot
import com.example.ui.theme.MeridianDuskBlue

@Composable
fun SyncIndicator(
    status: SyncStatus,
    modifier: Modifier = Modifier
) {
    val (label, dotColor) = when (status) {
        SyncStatus.SAVED -> "Saved" to Color(0xFF5B9E78)
        SyncStatus.SENDING -> "Sending..." to MeridianApricot
        SyncStatus.SAVED_OFFLINE -> "Saved on phone" to MeridianDuskBlue
        SyncStatus.OFFLINE -> "Offline" to Color(0xFF9E9E9E)
    }

    val animatedColor by animateColorAsState(targetValue = dotColor, label = "syncDotColor")

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .testTag("sync_indicator")
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f))
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Box(
            modifier = Modifier
                .size(6.dp)
                .clip(CircleShape)
                .background(animatedColor)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = label,
            fontSize = 11.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
