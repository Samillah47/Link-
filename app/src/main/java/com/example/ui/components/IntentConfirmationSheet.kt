package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ai.PaymentIntentResult
import com.example.ui.theme.*
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IntentConfirmationSheet(
    result: PaymentIntentResult,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = LinkSurfaceCard,
        contentColor = LinkTextPrimary,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "AI Payment Intent",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = LinkElectricCyan
                    )
                    Text(
                        text = "Detected Language: ${result.preferredLanguage}",
                        style = MaterialTheme.typography.bodySmall,
                        color = LinkTextSecondary
                    )
                }
                Surface(
                    color = when (result.riskLevel) {
                        "HIGH" -> LinkErrorRed.copy(alpha = 0.2f)
                        "MEDIUM" -> LinkWarningOrange.copy(alpha = 0.2f)
                        else -> LinkEmeraldGreen.copy(alpha = 0.2f)
                    },
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = when (result.riskLevel) {
                                "HIGH" -> Icons.Default.Warning
                                "MEDIUM" -> Icons.Default.Info
                                else -> Icons.Default.Shield
                            },
                            contentDescription = "Risk level",
                            tint = when (result.riskLevel) {
                                "HIGH" -> LinkErrorRed
                                "MEDIUM" -> LinkWarningOrange
                                else -> LinkEmeraldGreen
                            },
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "${result.riskLevel} RISK",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = when (result.riskLevel) {
                                "HIGH" -> LinkErrorRed
                                "MEDIUM" -> LinkWarningOrange
                                else -> LinkEmeraldGreen
                            }
                        )
                    }
                }
            }

            HorizontalDivider(color = LinkSurfaceBorder)

            // Recipient & Amount Card
            Card(
                colors = CardDefaults.cardColors(containerColor = LinkNavyBackground),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "RECIPIENT",
                        style = MaterialTheme.typography.labelSmall,
                        color = LinkTextSecondary
                    )
                    Text(
                        text = result.recipient.ifBlank { "Contact" },
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = LinkTextPrimary
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "AMOUNT",
                        style = MaterialTheme.typography.labelSmall,
                        color = LinkTextSecondary
                    )
                    Text(
                        text = "${NumberFormat.getNumberInstance(Locale.US).format(result.amount)} ${result.currency}",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = LinkElectricCyan
                    )

                    if (!result.purpose.isBlank()) {
                        Text(
                            text = "Purpose: ${result.purpose}",
                            style = MaterialTheme.typography.bodySmall,
                            color = LinkTextSecondary
                        )
                    }
                }
            }

            // Cross Border FX Info if applicable
            if (result.intent == "CROSS_BORDER" && result.targetCurrency != null) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = LinkAmberGold.copy(alpha = 0.15f)),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = "🌍 Cross-Border FX Estimate",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = LinkAmberGold
                        )
                        Text(
                            text = "Estimated Received: ${result.estimatedForeignAmount ?: 0.0} ${result.targetCurrency}",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = LinkTextPrimary
                        )
                        Text(
                            text = result.exchangeRateText ?: "Est. Rate: 1 CNY = 182.40 RWF",
                            style = MaterialTheme.typography.bodySmall,
                            color = LinkTextSecondary
                        )
                    }
                }
            }

            // AI Smart Routing Card
            Card(
                colors = CardDefaults.cardColors(containerColor = LinkNavyBackground),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Smart routing",
                        tint = LinkEmeraldGreen,
                        modifier = Modifier.size(28.dp)
                    )
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "AI Smart Routing Recommendation",
                            style = MaterialTheme.typography.labelSmall,
                            color = LinkTextSecondary
                        )
                        Text(
                            text = "Use ${result.recommendedSource}",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = LinkEmeraldGreen
                        )
                        Text(
                            text = result.routingReason,
                            style = MaterialTheme.typography.bodySmall,
                            color = LinkTextSecondary
                        )
                    }
                }
            }

            // Financial Guardian Warning if risk != LOW
            if (result.riskLevel != "LOW") {
                Card(
                    colors = CardDefaults.cardColors(containerColor = LinkErrorRed.copy(alpha = 0.15f)),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = "Guardian",
                            tint = LinkErrorRed,
                            modifier = Modifier.size(28.dp)
                        )
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "AI Financial Guardian Advisory",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = LinkErrorRed
                            )
                            Text(
                                text = result.riskReason,
                                style = MaterialTheme.typography.bodySmall,
                                color = LinkTextPrimary
                            )
                        }
                    }
                }
            }

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .weight(1f)
                        .height(52.dp)
                        .testTag("btn_cancel_intent"),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text("Cancel", color = LinkTextSecondary)
                }

                Button(
                    onClick = onConfirm,
                    modifier = Modifier
                        .weight(1f)
                        .height(52.dp)
                        .testTag("btn_confirm_intent"),
                    colors = ButtonDefaults.buttonColors(containerColor = LinkElectricCyan, contentColor = LinkNavyBackground),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        text = "Authorize Pay",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}
