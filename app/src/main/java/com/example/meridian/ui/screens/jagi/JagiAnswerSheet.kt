package com.example.meridian.ui.screens.jagi

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.meridian.data.jagi.JagiRepo
import com.example.meridian.ui.components.PixelHeart
import com.example.meridian.ui.components.bounceClick
import com.example.ui.theme.FrauncesFontFamily

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JagiAnswerSheet(
    repo: JagiRepo,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    val coupleData by repo.coupleData.collectAsState()
    var answerText by remember { mutableStateOf(coupleData.userAnswer ?: "") }
    var isSealed by remember { mutableStateOf(coupleData.isUserAnswerSealed) }

    // Flipping animation when unsealed
    val flipRotation by animateFloatAsState(
        targetValue = if (isSealed) 180f else 0f,
        animationSpec = tween(durationMillis = 600, easing = FastOutSlowInEasing),
        label = "flip_envelope"
    )

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = Color(0xFFFAF5EE),
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
        modifier = modifier.testTag("answer_question_sheet")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 6.dp)
                .padding(bottom = 36.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.size(24.dp))
                Text(
                    text = "TODAY'S QUESTION",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFA8998D),
                    letterSpacing = 1.5.sp
                )
                IconButton(onClick = onDismiss, modifier = Modifier.size(32.dp)) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = Color(0xFF8E8076)
                    )
                }
            }

            // Question Headline
            Text(
                text = coupleData.todayQuestion,
                fontFamily = FrauncesFontFamily,
                fontSize = 24.sp,
                color = Color(0xFF2C221E),
                lineHeight = 30.sp
            )

            // YOUR ANSWER
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "YOUR ANSWER",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFA8998D),
                    letterSpacing = 1.sp
                )

                OutlinedTextField(
                    value = answerText,
                    onValueChange = { if (!isSealed) answerText = it },
                    placeholder = {
                        Text(
                            "Write it like you'd say it",
                            color = Color(0xFFB5A89D),
                            fontSize = 15.sp
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(130.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color(0xFFFFFDF8),
                        unfocusedContainerColor = Color(0xFFFFFDF8),
                        focusedBorderColor = Color(0xFFEFE8DD),
                        unfocusedBorderColor = Color(0xFFF0E7DA)
                    ),
                    enabled = !isSealed
                )
            }

            // PARTNER'S ANSWER (Sealed envelope card or revealed text)
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                if (!isSealed) {
                    // Sealed envelope state
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(20.dp))
                            .border(1.dp, Color(0xFFDACFBF), RoundedCornerShape(20.dp))
                            .background(Color(0xFFFAF2EB))
                            .padding(18.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            // Envelope with red pixel heart
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color(0xFFEEDACD)),
                                contentAlignment = Alignment.Center
                            ) {
                                PixelHeart(size = 20.dp)
                            }

                            Column {
                                Text(
                                    text = "HER ANSWER · SEALED",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFFA8998D),
                                    letterSpacing = 1.sp
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "Hers stays sealed until you answer.",
                                    fontFamily = FrauncesFontFamily,
                                    fontStyle = FontStyle.Italic,
                                    fontSize = 14.sp,
                                    color = Color(0xFF59483F)
                                )
                            }
                        }
                    }
                } else {
                    // Revealed partner answer
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFDF8)),
                        shape = RoundedCornerShape(20.dp),
                        border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(Color(0xFFE27B58)))
                    ) {
                        Column(modifier = Modifier.padding(18.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("👩🏾", fontSize = 18.sp)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "JOY'S ANSWER · UNSEALED",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFFE27B58),
                                    letterSpacing = 1.sp
                                )
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = coupleData.partnerAnswer ?: "How can I make more time for the things that quietly bring me peace?",
                                fontFamily = FrauncesFontFamily,
                                fontStyle = FontStyle.Italic,
                                fontSize = 16.sp,
                                color = Color(0xFF2C221E),
                                lineHeight = 22.sp
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Sticky Bottom Button: "Seal my answer / AND UNSEAL HERS ->"
            Button(
                onClick = {
                    if (!isSealed && answerText.isNotBlank()) {
                        repo.sealUserAnswer(answerText)
                        isSealed = true
                    } else if (isSealed) {
                        onDismiss()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .bounceClick {
                        if (!isSealed && answerText.isNotBlank()) {
                            repo.sealUserAnswer(answerText)
                            isSealed = true
                        } else if (isSealed) {
                            onDismiss()
                        }
                    }
                    .testTag("seal_answer_button"),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF7C5A0)),
                shape = RoundedCornerShape(28.dp),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = if (!isSealed) "Seal my answer" else "Done · Keep In Journal",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF332014)
                    )
                    if (!isSealed) {
                        Text(
                            text = "AND UNSEAL HERS →",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF7A4A28),
                            letterSpacing = 0.5.sp
                        )
                    }
                }
            }
        }
    }
}
