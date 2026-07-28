package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ui.theme.*
import com.example.util.AppLanguage
import com.example.util.Localization

@Composable
fun ReceiveMoneyModal(
    appLanguage: AppLanguage,
    onDismiss: () -> Unit,
    onSimulateReceive: (String, Double) -> Unit
) {
    var senderName by remember { mutableStateOf("Jean Luc (Kigali Tech)") }
    var receiveAmount by remember { mutableStateOf("25000") }
    var copiedLink by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(28.dp),
            color = LinkSurfaceCard,
            border = BorderStroke(1.dp, LinkSurfaceBorder),
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Surface(
                            color = LinkMintContainer,
                            shape = CircleShape,
                            modifier = Modifier.size(36.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.QrCodeScanner,
                                    contentDescription = null,
                                    tint = LinkDarkTeal,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                        Text(
                            text = Localization.getString(appLanguage, "receive_money"),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = LinkTextPrimary
                        )
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.size(32.dp).testTag("btn_close_receive")
                    ) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = LinkTextSecondary)
                    }
                }

                // QR Code Display Card
                Surface(
                    color = Color.White,
                    shape = RoundedCornerShape(20.dp),
                    border = BorderStroke(1.dp, LinkSurfaceBorder),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Simulated QR Code Visual Grid
                        Box(
                            modifier = Modifier
                                .size(160.dp)
                                .background(Color(0xFF0F172A), RoundedCornerShape(16.dp))
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.QrCode2,
                                    contentDescription = "QR Code",
                                    tint = Color.White,
                                    modifier = Modifier.size(100.dp)
                                )
                                Text(
                                    text = "@keza.rw",
                                    color = LinkMintContainer,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp
                                )
                            }
                        }

                        Text(
                            text = "LINK ID: RW-2026-99201",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.ExtraBold,
                            color = LinkTextPrimary
                        )

                        Text(
                            text = Localization.getString(appLanguage, "receive_qr_sub"),
                            style = MaterialTheme.typography.bodySmall,
                            color = LinkTextSecondary,
                            fontSize = 11.sp
                        )

                        // Copyable Payment Link Pill
                        Surface(
                            color = LinkMintContainer,
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 12.dp, vertical = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "https://pay.link.rw/@keza",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = LinkDarkTeal,
                                    fontWeight = FontWeight.Bold
                                )
                                TextButton(
                                    onClick = { copiedLink = true },
                                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp)
                                ) {
                                    Text(
                                        text = if (copiedLink) "Copied ✓" else "Copy",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = LinkDeepTeal
                                    )
                                }
                            }
                        }
                    }
                }

                // Divider / Simulator Section
                HorizontalDivider(color = LinkSurfaceBorder)

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = Localization.getString(appLanguage, "simulate_receive"),
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        color = LinkTextPrimary
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = senderName,
                            onValueChange = { senderName = it },
                            label = { Text("Sender Name") },
                            modifier = Modifier.weight(1.3f),
                            singleLine = true
                        )
                        OutlinedTextField(
                            value = receiveAmount,
                            onValueChange = { receiveAmount = it },
                            label = { Text("Amount (RWF)") },
                            modifier = Modifier.weight(1f),
                            singleLine = true
                        )
                    }

                    Button(
                        onClick = {
                            val amt = receiveAmount.toDoubleOrNull() ?: 25000.0
                            onSimulateReceive(senderName, amt)
                            onDismiss()
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = LinkEmeraldGreen,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("btn_trigger_simulate_receive")
                    ) {
                        Icon(Icons.Default.ArrowDownward, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Receive Funds Now (+ RWF)",
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}
