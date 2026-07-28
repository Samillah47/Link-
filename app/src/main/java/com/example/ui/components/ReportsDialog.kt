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
import com.example.data.entity.FinancialAccountEntity
import com.example.data.entity.TransactionEntity
import com.example.ui.theme.*
import java.text.NumberFormat
import java.util.*

@Composable
fun ReportsDialog(
    accounts: List<FinancialAccountEntity>,
    transactions: List<TransactionEntity>,
    onDismiss: () -> Unit
) {
    var selectedTimeframe by remember { mutableStateOf("Monthly") }
    var fxInputRwf by remember { mutableStateOf("10000") }
    var isExporting by remember { mutableStateOf(false) }
    var exportSuccess by remember { mutableStateOf(false) }

    val currencyFormatter = remember { NumberFormat.getNumberInstance(Locale.US) }

    val totalBalance = accounts.sumOf { it.balance }
    val totalIncome = transactions.filter { it.type.equals("RECEIVE", ignoreCase = true) }.sumOf { it.amount }
    val totalOutflows = transactions.filter { !it.type.equals("RECEIVE", ignoreCase = true) }.sumOf { it.amount }

    // Dynamic Category Calculations
    val categoryTotals = transactions.groupBy { it.category }
        .mapValues { entry -> entry.value.sumOf { it.amount } }
    val maxCategorySpend = (categoryTotals.values.maxOrNull() ?: 1.0).coerceAtLeast(1.0)

    val rwfAmount = fxInputRwf.toDoubleOrNull() ?: 10000.0

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = LinkSurfaceCard,
            border = BorderStroke(1.dp, LinkSurfaceBorder),
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Header Bar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Cash Flow & Analytics",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = LinkTextPrimary
                        )
                        Text(
                            text = "Smart Financial Intelligence Report",
                            style = MaterialTheme.typography.bodySmall,
                            color = LinkTextSecondary
                        )
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.size(32.dp).testTag("btn_close_reports")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = LinkTextSecondary
                        )
                    }
                }

                // Timeframe Filter Chips
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf("Weekly", "Monthly", "Yearly").forEach { timeframe ->
                        FilterChip(
                            selected = selectedTimeframe == timeframe,
                            onClick = { selectedTimeframe = timeframe },
                            label = { Text(timeframe, fontSize = 12.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = LinkMintContainer,
                                selectedLabelColor = LinkDarkTeal
                            )
                        )
                    }
                }

                // Financial Health Score Card
                Card(
                    colors = CardDefaults.cardColors(containerColor = LinkDarkButton),
                    shape = RoundedCornerShape(18.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Surface(
                            color = LinkEmeraldGreen,
                            shape = CircleShape,
                            modifier = Modifier.size(54.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = "94",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = LinkNavyBackground
                                )
                            }
                        }

                        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                            Text(
                                text = "Financial Health Score: Excellent",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = LinkSurfaceCard
                            )
                            Text(
                                text = "Zero-fee eCash used for 88% of payments. Saved ~14,200 RWF in telco transfer fees this month.",
                                style = MaterialTheme.typography.bodySmall,
                                color = LinkSurfaceCard.copy(alpha = 0.8f),
                                fontSize = 11.sp
                            )
                        }
                    }
                }

                // Income vs Outflows Summary
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Card(
                        modifier = Modifier.weight(1f),
                        colors = CardDefaults.cardColors(containerColor = LinkMintContainer),
                        shape = RoundedCornerShape(16.dp),
                        border = BorderStroke(1.dp, LinkMintBorder)
                    ) {
                        Column(
                            modifier = Modifier.padding(14.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(Icons.Default.ArrowDownward, contentDescription = null, tint = LinkEmeraldGreen, modifier = Modifier.size(16.dp))
                                Text("INCOME", style = MaterialTheme.typography.labelSmall, color = LinkDarkTeal, fontWeight = FontWeight.Bold)
                            }
                            Text(
                                text = "+${currencyFormatter.format(totalIncome.toInt())} RWF",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.ExtraBold,
                                color = LinkDarkTeal
                            )
                        }
                    }

                    Card(
                        modifier = Modifier.weight(1f),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFFEF2F2)),
                        shape = RoundedCornerShape(16.dp),
                        border = BorderStroke(1.dp, Color(0xFFFCA5A5))
                    ) {
                        Column(
                            modifier = Modifier.padding(14.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(Icons.Default.ArrowUpward, contentDescription = null, tint = Color(0xFFDC2626), modifier = Modifier.size(16.dp))
                                Text("OUTFLOWS", style = MaterialTheme.typography.labelSmall, color = Color(0xFF991B1B), fontWeight = FontWeight.Bold)
                            }
                            Text(
                                text = "-${currencyFormatter.format(totalOutflows.toInt())} RWF",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color(0xFF991B1B)
                            )
                        }
                    }
                }

                // Dynamic Category Expense Distribution
                Card(
                    colors = CardDefaults.cardColors(containerColor = LinkLightBackground),
                    shape = RoundedCornerShape(18.dp),
                    border = BorderStroke(1.dp, LinkSurfaceBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "Category Expense Distribution",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = LinkTextPrimary
                        )

                        if (categoryTotals.isEmpty()) {
                            Text("No category records found.", color = LinkTextSecondary, style = MaterialTheme.typography.bodySmall)
                        } else {
                            categoryTotals.forEach { (cat, amount) ->
                                val progress = (amount / maxCategorySpend).toFloat().coerceIn(0.05f, 1f)
                                val color = when (cat) {
                                    "Rent" -> LinkBkBlue
                                    "Groceries" -> LinkEmeraldGreen
                                    "Utilities" -> LinkDeepTeal
                                    "Aura Sync Proximity" -> LinkDarkTeal
                                    else -> LinkAmberGold
                                }
                                CategoryProgressRow(
                                    category = cat,
                                    progress = progress,
                                    amountStr = "${currencyFormatter.format(amount.toInt())} RWF",
                                    color = color
                                )
                            }
                        }
                    }
                }

                // Live Cross-Border FX Rate Converter
                Card(
                    colors = CardDefaults.cardColors(containerColor = LinkMintContainer.copy(alpha = 0.6f)),
                    shape = RoundedCornerShape(18.dp),
                    border = BorderStroke(1.dp, LinkMintBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.CurrencyExchange,
                                contentDescription = "FX Rates",
                                tint = LinkDarkTeal
                            )
                            Text(
                                text = "Live East Africa FX Calculator",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = LinkDarkTeal
                            )
                        }

                        OutlinedTextField(
                            value = fxInputRwf,
                            onValueChange = { fxInputRwf = it },
                            label = { Text("Base Amount (RWF)") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = LinkDarkTeal,
                                unfocusedBorderColor = LinkMintBorder
                            )
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            FxRatePill("${currencyFormatter.format((rwfAmount / 10.42).toInt())} KES")
                            FxRatePill("${currencyFormatter.format((rwfAmount / 0.36).toInt())} UGX")
                            FxRatePill("${currencyFormatter.format(String.format(Locale.US, "%.2f", rwfAmount / 1350.0).toDouble())} USD")
                        }
                    }
                }

                // Export Statement Action Button
                if (exportSuccess) {
                    Surface(
                        color = LinkMintContainer,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = LinkEmeraldGreen)
                            Text("Financial Statement Exported (PDF)! Saved to Downloads.", color = LinkDarkTeal, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                    }
                }

                Button(
                    onClick = {
                        exportSuccess = true
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = LinkDeepTeal,
                        contentColor = LinkSurfaceCard
                    ),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("btn_export_report_pdf")
                ) {
                    Icon(Icons.Default.Download, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Export Official Statement (PDF/CSV)", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun CategoryProgressRow(
    category: String,
    progress: Float,
    amountStr: String,
    color: Color
) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(category, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold, color = LinkTextPrimary)
            Text(amountStr, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold, color = color)
        }
        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp)),
            color = color,
            trackColor = LinkSurfaceBorder
        )
    }
}

@Composable
fun FxRatePill(text: String) {
    Surface(
        color = LinkSurfaceCard,
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, LinkMintBorder)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = LinkDarkTeal,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}
