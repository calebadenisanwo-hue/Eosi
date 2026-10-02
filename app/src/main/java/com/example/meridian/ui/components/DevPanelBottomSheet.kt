package com.example.meridian.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.meridian.core.crypto.MeridianCrypto
import com.example.meridian.core.time.MeridianTime
import com.example.meridian.data.model.DemoSeedData
import com.example.ui.theme.FrauncesFontFamily
import com.example.ui.theme.MeridianApricot
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DevPanelBottomSheet(
    activeUid: String,
    onSwitchPersona: (String) -> Unit,
    onToggleOffline: (Boolean) -> Unit,
    onDismiss: () -> Unit
) {
    var isSimulatingOffline by remember { mutableStateOf(false) }
    var currentClockLabel by remember {
        mutableStateOf(if (MeridianTime.clockOverride != null) "Time Machine Active" else "Real Time Clock")
    }

    val fingerprint = remember {
        MeridianCrypto.computeSafetyFingerprint(
            DemoSeedData.ayaanProfile.publicKey,
            DemoSeedData.linneaProfile.publicKey
        )
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = MaterialTheme.colorScheme.surface,
        modifier = Modifier.testTag("dev_panel_sheet")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 8.dp)
                .padding(bottom = 32.dp)
        ) {
            Text(
                text = "Developer Time Machine & Tools",
                fontFamily = FrauncesFontFamily,
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "Route /#/dev emulation. Inspect crypto, DST jumps, and offline behavior.",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Safety Fingerprint
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "Safety Fingerprint (Public Key Hash)",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        fingerprint.forEach { emoji ->
                            Box(
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(MaterialTheme.colorScheme.surface)
                                    .padding(4.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = emoji, fontSize = 20.sp)
                            }
                        }
                        Spacer(modifier = Modifier.weight(1f))
                        Text(
                            text = "Verified",
                            fontSize = 11.sp,
                            color = MeridianApricot,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Persona Switcher in Dev
            Text(
                text = "Act As Persona",
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(6.dp))
            PersonaSwitcher(
                activeUid = activeUid,
                onSelectPersona = onSwitchPersona,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Offline Toggle
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Simulate Offline Mode",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Queues actions locally, syncs automatically on reconnect",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Switch(
                    checked = isSimulatingOffline,
                    onCheckedChange = { checked ->
                        isSimulatingOffline = checked
                        onToggleOffline(checked)
                    },
                    modifier = Modifier.testTag("offline_toggle_switch")
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Time Machine Jumps
            Text(
                text = "Time Machine: $currentClockLabel",
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = {
                        MeridianTime.resetClock()
                        currentClockLabel = "Real Time Clock"
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Real Time", fontSize = 11.sp)
                }

                OutlinedButton(
                    onClick = {
                        // Jump to DST shift on 25 Oct 2026 01:00 UTC
                        val dstInstant = ZonedDateTime.of(2026, 10, 24, 18, 0, 0, 0, ZoneId.of("UTC")).toInstant()
                        MeridianTime.setOverrideInstant(dstInstant)
                        currentClockLabel = "Oct 24, 2026 (DST Eve)"
                    },
                    modifier = Modifier.weight(1.3f)
                ) {
                    Text("DST 25 Oct 2026", fontSize = 11.sp)
                }

                OutlinedButton(
                    onClick = {
                        // Jump to DST shift on 28 Mar 2027 01:00 UTC
                        val dstInstant = ZonedDateTime.of(2027, 3, 27, 18, 0, 0, 0, ZoneId.of("UTC")).toInstant()
                        MeridianTime.setOverrideInstant(dstInstant)
                        currentClockLabel = "Mar 27, 2027 (DST Spring)"
                    },
                    modifier = Modifier.weight(1.3f)
                ) {
                    Text("DST 28 Mar 2027", fontSize = 11.sp)
                }
            }
        }
    }
}
