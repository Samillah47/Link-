package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.entity.TransactionEntity
import com.example.ui.theme.*
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun TransactionDetailDialog(
    transaction: TransactionEntity,
    onDismiss: () -> Unit
) {
    var isReceiptShared by remember { mutableStateOf(false) }
    val currencyFormatter = remember { NumberFormat.getNumberInstance(Locale.US) }
    val dateFormatter = remember { SimpleDateFormat("MMMM dd, yyyy • HH:mm", Locale.getDefault()) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = LinkSurfaceCard,
            border = BorderStroke(1.dp, LinkSurfaceBorder),
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Header Bar
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
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = "RECEIPT",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.ExtraBold,
                                color = LinkDarkTeal,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                            )
                        }
                        Text(
                            text = transaction.id,
                            style = MaterialTheme.typography.labelSmall,
                            color = LinkTextSecondary
                        )
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.size(32.dp).testTag("btn_close_tx_detail")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = LinkTextSecondary
                        )
                    }
                }

                // Main Amount Card
                Card(
                    colors = CardDefaults.cardColors(containerColor = LinkMintContainer.copy(alpha = 0.5f)),
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(1.dp, LinkMintBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = transaction.recipient,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = LinkDarkTeal
                        )
                        Text(
                            text = "-${currencyFormatter.format(transaction.amount)} RWF",
                            style = MaterialTheme.typography.headlineLarge,
                            fontWeight = FontWeight.ExtraBold,
                            color = LinkDarkTeal
                        )
                        Text(
                            text = dateFormatter.format(Date(transaction.timestamp)),
                            style = MaterialTheme.typography.bodySmall,
                            color = LinkTextSecondary
                        )
                    }
                }

                // AI Risk & Smart Routing Info
                Card(
                    colors = CardDefaults.cardColors(containerColor = LinkLightBackground),
                    shape = RoundedCornerShape(14.dp),
                    border = BorderStroke(1.dp, LinkSurfaceBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Security,
                                    contentDescription = "AI Risk",
                                    tint = LinkEmeraldGreen,
                                    modifier = Modifier.size(18.dp)
                                )
                                Text(
                                    text = "AI Guardian Assessment",
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = LinkTextPrimary
                                )
                            }

                            Surface(
                                color = LinkEmeraldGreen.copy(alpha = 0.15f),
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                Text(
                                    text = "RISK: ${transaction.riskLevel}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = LinkEmeraldGreen,
                                    fontWeight = FontWeight.ExtraBold,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }

                        if (transaction.riskReason.isNotBlank()) {
                            Text(
                                text = "• Reason: ${transaction.riskReason}",
                                style = MaterialTheme.typography.bodySmall,
                                color = LinkTextSecondary
                            )
                        }

                        HorizontalDivider(color = LinkSurfaceBorder)

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Source Account", style = MaterialTheme.typography.bodySmall, color = LinkTextSecondary)
                            Text(transaction.sourceAccount, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold, color = LinkTextPrimary)
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Category", style = MaterialTheme.typography.bodySmall, color = LinkTextSecondary)
                            Text(transaction.category, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold, color = LinkTextPrimary)
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Language Used", style = MaterialTheme.typography.bodySmall, color = LinkTextSecondary)
                            Text(transaction.languageUsed, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold, color = LinkDeepTeal)
                        }

                        if (transaction.purpose.isNotBlank()) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Purpose / Note", style = MaterialTheme.typography.bodySmall, color = LinkTextSecondary)
                                Text(transaction.purpose, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Medium, color = LinkTextPrimary)
                            }
                        }
                    }
                }

                // Share Receipt Action Button
                Button(
                    onClick = { isReceiptShared = true },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = LinkDeepTeal,
                        contentColor = LinkSurfaceCard
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("btn_share_digital_receipt")
                ) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = "Share",
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (isReceiptShared) "Receipt Copied to Clipboard!" else "Share Digital Receipt",
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
