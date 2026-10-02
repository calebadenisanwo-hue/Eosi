package com.example.meridian.ui.screens.letters

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.FrauncesFontFamily
import com.example.ui.theme.MeridianApricot
import java.time.Instant

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun WriteLetterSheet(
    partnerName: String,
    onSaveLetter: (trigger: String, title: String, body: String, unsealAt: Instant?) -> Unit,
    onDismiss: () -> Unit
) {
    val triggers = listOf(
        "I miss you", "can't sleep", "bad day", "we fought", "good news",
        "feeling anxious", "I need a laugh", "feeling lonely", "can't wait to see you",
        "after a visit", "birthday", "custom"
    )

    var selectedTrigger by remember { mutableStateOf(triggers[0]) }
    var customTrigger by remember { mutableStateOf("") }
    var title by remember { mutableStateOf("") }
    var body by remember { mutableStateOf("") }

    val effectiveTrigger = if (selectedTrigger == "custom" && customTrigger.isNotBlank()) customTrigger else selectedTrigger

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = MaterialTheme.colorScheme.surface,
        modifier = Modifier.testTag("write_letter_sheet")
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
                    text = "Write an Open When letter",
                    fontFamily = FrauncesFontFamily,
                    fontSize = 20.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
                IconButton(onClick = onDismiss) {
                    Icon(Icons.Default.Close, contentDescription = "Close")
                }
            }
            Text(
                text = "For $partnerName to unseal in the exact moment they need it most.",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Trigger Picker
            Text(
                text = "Open when:",
                fontWeight = FontWeight.SemiBold,
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(6.dp))
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                triggers.forEach { trig ->
                    val isSelected = selectedTrigger == trig
                    FilterChip(
                        selected = isSelected,
                        onClick = { selectedTrigger = trig },
                        label = { Text(trig, fontSize = 12.sp) },
                        shape = RoundedCornerShape(12.dp)
                    )
                }
            }

            if (selectedTrigger == "custom") {
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = customTrigger,
                    onValueChange = { customTrigger = it },
                    placeholder = { Text("e.g. your exam results arrive") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp)
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Title
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Letter Title / Envelope Label") },
                placeholder = { Text("e.g. A pocket of warmth for tonight") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Body
            OutlinedTextField(
                value = body,
                onValueChange = { if (it.length <= 2000) body = it },
                label = { Text("Letter Body (${body.length}/2000)") },
                placeholder = { Text("Write with warmth and no rush...") },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 140.dp),
                shape = RoundedCornerShape(14.dp),
                maxLines = 8
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if (title.isNotBlank() && body.isNotBlank()) {
                        onSaveLetter(effectiveTrigger, title, body, null)
                        onDismiss()
                    }
                },
                enabled = title.isNotBlank() && body.isNotBlank(),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text("Seal & Encrypt Envelope ✉️")
            }
        }
    }
}
