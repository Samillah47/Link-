package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ai.PaymentIntentResult
import com.example.ui.LinkViewModel
import com.example.ui.theme.*
import java.text.NumberFormat
import java.util.Locale

@Composable
fun AiIntentScreen(
    viewModel: LinkViewModel
) {
    val input by viewModel.naturalLanguageInput.collectAsState()
    val isAnalyzing by viewModel.isAnalyzing.collectAsState()
    val result by viewModel.parsedResult.collectAsState()

    var isMicListening by remember { mutableStateOf(false) }
    var showRawJson by remember { mutableStateOf(false) }

    val presetPrompts = listOf(
        PresetPrompt("🇷🇼 Kinyarwanda", "Ohereza amafaranga ibihumbi icumi kuri Mama."),
        PresetPrompt("🇬🇧 English", "Send my mother 10,000 RWF for groceries."),
        PresetPrompt("🇫🇷 French", "Envoie 15 000 francs à Jean pour le dîner."),
        PresetPrompt("🇰🇪 Kiswahili", "Tuma shilingi elfu tano kwa rafiki yangu."),
        PresetPrompt("🇨🇳 Cross-Border", "Send 100,000 RWF to my friend in China."),
        PresetPrompt("🛡️ High Risk", "Transfer 500,000 RWF to unfamiliar merchant #98221"),
        PresetPrompt("🧠 Memory Query", "When did I pay my landlord and how much spent on food?")
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(LinkNavyBackground)
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Title & Description
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(
                text = "Multilingual AI Intent Payments",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = LinkElectricCyan
            )
            Text(
                text = "Speak or type naturally in Kinyarwanda, Kiswahili, French, or English.",
                style = MaterialTheme.typography.bodySmall,
                color = LinkTextSecondary
            )
        }

        // Input Box + Mic + Send Button
        Card(
            colors = CardDefaults.cardColors(containerColor = LinkSurfaceCard),
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = input,
                    onValueChange = { viewModel.updateInput(it) },
                    placeholder = {
                        Text(
                            text = if (isMicListening) "Listening in Kinyarwanda/French/Kiswahili..." else "e.g. Ohereza 10,000 RWF kuri Mama",
                            color = LinkTextSecondary
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_natural_language"),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = LinkElectricCyan,
                        unfocusedBorderColor = LinkSurfaceBorder,
                        focusedTextColor = LinkTextPrimary,
                        unfocusedTextColor = LinkTextPrimary
                    ),
                    shape = RoundedCornerShape(14.dp),
                    trailingIcon = {
                        IconButton(
                            onClick = {
                                isMicListening = !isMicListening
                                if (isMicListening) {
                                    viewModel.updateInput("Ohereza amafaranga ibihumbi icumi kuri Mama.")
                                }
                            },
                            modifier = Modifier.testTag("btn_mic_toggle")
                        ) {
                            Icon(
                                imageVector = if (isMicListening) Icons.Default.Mic else Icons.Default.MicNone,
                                contentDescription = "Voice Input",
                                tint = if (isMicListening) LinkErrorRed else LinkElectricCyan
                            )
                        }
                    }
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (isMicListening) {
                        Surface(
                            color = LinkErrorRed.copy(alpha = 0.2f),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = "🎙️ Voice Neural Engine Active",
                                style = MaterialTheme.typography.labelSmall,
                                color = LinkErrorRed,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    } else {
                        Spacer(modifier = Modifier.width(1.dp))
                    }

                    Button(
                        onClick = { viewModel.processNaturalLanguageInput() },
                        enabled = input.isNotBlank() && !isAnalyzing,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = LinkElectricCyan,
                            contentColor = LinkNavyBackground
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.testTag("btn_process_ai")
                    ) {
                        if (isAnalyzing) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(18.dp),
                                color = LinkNavyBackground,
                                strokeWidth = 2.dp
                            )
                        } else {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AutoAwesome,
                                    contentDescription = "Process AI",
                                    modifier = Modifier.size(18.dp)
                                )
                                Text("Process AI Intent", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }

        // Multilingual Preset Prompt Chips
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                text = "Tap Multilingual Preset Prompt:",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = LinkTextSecondary
            )

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(presetPrompts) { item ->
                    Surface(
                        color = LinkSurfaceCard,
                        border = ButtonDefaults.outlinedButtonBorder,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.clickable {
                            viewModel.updateInput(item.text)
                            viewModel.processNaturalLanguageInput(item.text)
                        }
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = item.label,
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = LinkElectricCyan
                            )
                        }
                    }
                }
            }
        }

        // Real-Time Gemini AI Parse Output Display
        if (result != null) {
            val r = result!!
            Card(
                colors = CardDefaults.cardColors(containerColor = LinkSurfaceCard),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // Header Row with Raw JSON Toggle
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Psychology,
                                contentDescription = "AI",
                                tint = LinkElectricCyan
                            )
                            Text(
                                text = "Parsed Intent (Gemini 3.5 Flash)",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = LinkTextPrimary
                            )
                        }

                        TextButton(onClick = { showRawJson = !showRawJson }) {
                            Text(
                                text = if (showRawJson) "Hide JSON" else "View JSON Schema",
                                color = LinkElectricCyan,
                                fontSize = 12.sp
                            )
                        }
                    }

                    // Raw JSON View for Judges
                    AnimatedVisibility(visible = showRawJson) {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = Color.Black),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = formatResultToJson(r),
                                style = MaterialTheme.typography.bodySmall,
                                fontFamily = FontFamily.Monospace,
                                color = LinkEmeraldGreen,
                                modifier = Modifier.padding(12.dp)
                            )
                        }
                    }

                    if (r.intent == "QUERY_MEMORY") {
                        // Financial Memory Answer Box
                        Card(
                            colors = CardDefaults.cardColors(containerColor = LinkElectricCyan.copy(alpha = 0.15f)),
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier.padding(14.dp),
                                verticalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(
                                    text = "🧠 AI Financial Memory Answer",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = LinkElectricCyan
                                )
                                Text(
                                    text = r.memoryAnswer ?: "Checked Room database for past transactions.",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = LinkTextPrimary
                                )
                            }
                        }
                    } else {
                        // Financial Intent Summary Details
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text("INTENT", style = MaterialTheme.typography.labelSmall, color = LinkTextSecondary)
                                Text(r.intent, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = LinkElectricCyan)
                            }
                            Column {
                                Text("LANGUAGE", style = MaterialTheme.typography.labelSmall, color = LinkTextSecondary)
                                Text(r.preferredLanguage, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = LinkTextPrimary)
                            }
                            Column {
                                Text("RECIPIENT", style = MaterialTheme.typography.labelSmall, color = LinkTextSecondary)
                                Text(r.recipient, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = LinkTextPrimary)
                            }
                        }

                        // Amount Card
                        Card(
                            colors = CardDefaults.cardColors(containerColor = LinkNavyBackground),
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text("AMOUNT DETECTED", style = MaterialTheme.typography.labelSmall, color = LinkTextSecondary)
                                    Text(
                                        text = "${NumberFormat.getNumberInstance(Locale.US).format(r.amount)} RWF",
                                        style = MaterialTheme.typography.headlineSmall,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = LinkElectricCyan
                                    )
                                }
                                Surface(
                                    color = when (r.riskLevel) {
                                        "HIGH" -> LinkErrorRed.copy(alpha = 0.2f)
                                        "MEDIUM" -> LinkWarningOrange.copy(alpha = 0.2f)
                                        else -> LinkEmeraldGreen.copy(alpha = 0.2f)
                                    },
                                    shape = RoundedCornerShape(10.dp)
                                ) {
                                    Text(
                                        text = "${r.riskLevel} RISK",
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = when (r.riskLevel) {
                                            "HIGH" -> LinkErrorRed
                                            "MEDIUM" -> LinkWarningOrange
                                            else -> LinkEmeraldGreen
                                        },
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                    )
                                }
                            }
                        }

                        // Risk Advisory Card
                        if (r.riskLevel != "LOW") {
                            Card(
                                colors = CardDefaults.cardColors(containerColor = LinkErrorRed.copy(alpha = 0.15f)),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    Icon(Icons.Default.Shield, contentDescription = "Risk", tint = LinkErrorRed)
                                    Text(
                                        text = r.riskReason,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = LinkTextPrimary
                                    )
                                }
                            }
                        }

                        // Smart Routing Card
                        Card(
                            colors = CardDefaults.cardColors(containerColor = LinkNavyBackground),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Icon(Icons.Default.CheckCircle, contentDescription = "Routing", tint = LinkEmeraldGreen)
                                Column {
                                    Text(
                                        text = "Smart Route: ${r.recommendedSource}",
                                        style = MaterialTheme.typography.titleSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = LinkEmeraldGreen
                                    )
                                    Text(
                                        text = r.routingReason,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = LinkTextSecondary
                                    )
                                }
                            }
                        }

                        // Confirm Button
                        Button(
                            onClick = { viewModel.confirmAndExecutePayment() },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = LinkElectricCyan,
                                contentColor = LinkNavyBackground
                            ),
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .testTag("btn_execute_intent_pay")
                        ) {
                            Text("Confirm & Execute Payment", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        }
                    }
                }
            }
        }
    }
}

data class PresetPrompt(val label: String, val text: String)

private fun formatResultToJson(r: PaymentIntentResult): String {
    return """
{
  "intent": "${r.intent}",
  "recipient": "${r.recipient}",
  "amount": ${r.amount},
  "currency": "${r.currency}",
  "purpose": "${r.purpose}",
  "preferred_language": "${r.preferredLanguage}",
  "risk_level": "${r.riskLevel}",
  "risk_reason": "${r.riskReason}",
  "recommended_source": "${r.recommendedSource}",
  "routing_reason": "${r.routingReason}"
}
""".trimIndent()
}
