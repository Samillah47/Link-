package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.entity.FinancialAccountEntity
import com.example.data.entity.TransactionEntity
import com.example.ui.LinkViewModel
import com.example.ui.components.ReceiveMoneyModal
import com.example.ui.components.ReportsDialog
import com.example.ui.components.TransactionDetailDialog
import com.example.ui.theme.*
import com.example.util.AppLanguage
import com.example.util.Localization
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun DashboardScreen(
    viewModel: LinkViewModel,
    onNavigateToAiPay: () -> Unit,
    onNavigateToAuraSync: () -> Unit,
    onNavigateToAccount: () -> Unit
) {
    val accounts by viewModel.accounts.collectAsState()
    val transactions by viewModel.transactions.collectAsState()
    val totalBalance by viewModel.totalBalance.collectAsState()
    val successMsg by viewModel.paymentSuccessMessage.collectAsState()
    val appLanguage by viewModel.appLanguage.collectAsState()

    var selectedTxForDetail by remember { mutableStateOf<TransactionEntity?>(null) }
    var showReportsDialog by remember { mutableStateOf(false) }
    var showReceiveModal by remember { mutableStateOf(false) }

    val currencyFormatter = NumberFormat.getNumberInstance(Locale.US)

    val currentSelectedTx = selectedTxForDetail
    if (currentSelectedTx != null) {
        TransactionDetailDialog(
            transaction = currentSelectedTx,
            onDismiss = { selectedTxForDetail = null }
        )
    }

    if (showReportsDialog) {
        ReportsDialog(
            accounts = accounts,
            transactions = transactions,
            onDismiss = { showReportsDialog = false }
        )
    }

    if (showReceiveModal) {
        ReceiveMoneyModal(
            appLanguage = appLanguage,
            onDismiss = { showReceiveModal = false },
            onSimulateReceive = { sender, amount ->
                viewModel.simulateReceiveMoney(sender, amount)
            }
        )
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(LinkLightBackground)
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 100.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Professional Combined Liquidity Card
        item {
            Card(
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(containerColor = LinkMintContainer),
                border = BorderStroke(1.dp, LinkMintBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Column {
                            Text(
                                text = Localization.getString(appLanguage, "total_liquidity"),
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = LinkDarkTeal.copy(alpha = 0.7f),
                                letterSpacing = 1.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(verticalAlignment = Alignment.Bottom) {
                                Text(
                                    text = "${currencyFormatter.format(totalBalance)} ",
                                    style = MaterialTheme.typography.headlineLarge,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = LinkDarkTeal
                                )
                                Text(
                                    text = "RWF",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = LinkDarkTeal,
                                    modifier = Modifier.padding(bottom = 2.dp)
                                )
                            }
                        }

                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(RoundedCornerShape(14.dp))
                                .background(LinkDeepTeal),
                            contentAlignment = Alignment.Center
                        ) {
                            IconButton(
                                onClick = onNavigateToAccount,
                                modifier = Modifier.testTag("btn_account_shortcut")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = "User Account",
                                    tint = LinkSurfaceCard
                                )
                            }
                        }
                    }

                    // Horizontal Scrollable Account Liquidity Cards to prevent vertical text wrap
                    LazyRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(accounts) { acc ->
                            Surface(
                                modifier = Modifier.width(135.dp),
                                color = LinkSurfaceCard.copy(alpha = 0.85f),
                                shape = RoundedCornerShape(16.dp),
                                border = BorderStroke(1.dp, LinkSurfaceCard)
                            ) {
                                Column(
                                    modifier = Modifier.padding(12.dp),
                                    verticalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Text(
                                        text = acc.name,
                                        style = MaterialTheme.typography.labelMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = LinkTextSecondary,
                                        maxLines = 1,
                                        fontSize = 11.sp
                                    )
                                    Text(
                                        text = "${currencyFormatter.format(acc.balance.toInt())} RWF",
                                        style = MaterialTheme.typography.titleSmall,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = LinkDarkTeal,
                                        maxLines = 1
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Success Notification Banner
        if (successMsg != null) {
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = LinkEmeraldGreen.copy(alpha = 0.1f)),
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(1.dp, LinkEmeraldGreen.copy(alpha = 0.3f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "Success",
                                tint = LinkEmeraldGreen
                            )
                            Text(
                                text = successMsg ?: "",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = LinkTextPrimary
                            )
                        }
                        IconButton(onClick = { viewModel.dismissSuccessMessage() }) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Close",
                                tint = LinkTextSecondary
                            )
                        }
                    }
                }
            }
        }

        // Quick Action Buttons (AI Pay, Receive, Aura Sync)
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                QuickActionButton(
                    icon = Icons.Default.AutoAwesome,
                    label = Localization.getString(appLanguage, "pay_via_ai"),
                    sublabel = "Voice / Text",
                    color = LinkDeepTeal,
                    modifier = Modifier.weight(1f),
                    onClick = onNavigateToAiPay
                )

                QuickActionButton(
                    icon = Icons.Default.ArrowDownward,
                    label = Localization.getString(appLanguage, "receive_money"),
                    sublabel = "QR / Link",
                    color = LinkEmeraldGreen,
                    modifier = Modifier.weight(1f),
                    onClick = { showReceiveModal = true }
                )

                QuickActionButton(
                    icon = Icons.Default.Sensors,
                    label = Localization.getString(appLanguage, "nav_aura_sync"),
                    sublabel = "Proximity Link",
                    color = LinkDarkButton,
                    modifier = Modifier.weight(1f),
                    onClick = onNavigateToAuraSync
                )
            }
        }

        // AI Smart Routing Callout
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = LinkSurfaceCard),
                shape = RoundedCornerShape(20.dp),
                border = BorderStroke(1.dp, LinkSurfaceBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(LinkMintContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Psychology,
                            contentDescription = "AI Brain",
                            tint = LinkDeepTeal
                        )
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = Localization.getString(appLanguage, "smart_routing_active"),
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = LinkTextPrimary
                        )
                        Text(
                            text = Localization.getString(appLanguage, "smart_routing_sub"),
                            style = MaterialTheme.typography.bodySmall,
                            color = LinkTextSecondary,
                            fontSize = 11.sp
                        )
                    }
                }
            }
        }

        // Recent Activity Feed Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = Localization.getString(appLanguage, "recent_activity"),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = LinkTextPrimary
                )
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    IconButton(
                        onClick = { showReportsDialog = true },
                        modifier = Modifier.testTag("btn_open_reports")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Analytics,
                            contentDescription = "Analytics",
                            tint = LinkDeepTeal
                        )
                    }
                }
            }
        }

        // Transaction Items
        items(transactions) { tx ->
            TransactionItemRow(
                tx = tx,
                appLanguage = appLanguage,
                currencyFormatter = currencyFormatter,
                onClick = { selectedTxForDetail = tx }
            )
        }
    }
}

@Composable
fun QuickActionButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    sublabel: String,
    color: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = LinkSurfaceCard),
        shape = RoundedCornerShape(22.dp),
        border = BorderStroke(1.dp, LinkSurfaceBorder),
        modifier = modifier
            .clip(RoundedCornerShape(22.dp))
            .clickable(onClick = onClick)
            .testTag("btn_quick_${label.lowercase().replace(" ", "_")}")
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(color.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = label,
                    tint = color,
                    modifier = Modifier.size(22.dp)
                )
            }
            Column {
                Text(
                    text = label,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = LinkTextPrimary
                )
                Text(
                    text = sublabel,
                    style = MaterialTheme.typography.bodySmall,
                    color = LinkTextSecondary,
                    fontSize = 11.sp
                )
            }
        }
    }
}

@Composable
fun TransactionItemRow(
    tx: TransactionEntity,
    appLanguage: AppLanguage = AppLanguage.EN,
    currencyFormatter: NumberFormat,
    onClick: () -> Unit
) {
    val dateFormat = SimpleDateFormat("MMM dd, HH:mm", Locale.getDefault())
    val isReceive = tx.type.equals("RECEIVE", ignoreCase = true)

    Card(
        colors = CardDefaults.cardColors(containerColor = LinkSurfaceCard),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, LinkSurfaceBorder),
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .testTag("tx_item_${tx.id}")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Icon Indicator (Received = Green Down Arrow; Sent = Category Icon)
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(
                        if (isReceive) LinkEmeraldGreen.copy(alpha = 0.15f)
                        else when (tx.category) {
                            "Groceries" -> LinkEmeraldGreen.copy(alpha = 0.12f)
                            "Rent" -> LinkBkBlue.copy(alpha = 0.12f)
                            "Aura Sync" -> LinkDeepTeal.copy(alpha = 0.12f)
                            else -> LinkAmberGold.copy(alpha = 0.12f)
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (isReceive) Icons.Default.ArrowDownward
                    else when (tx.category) {
                        "Groceries" -> Icons.Default.ShoppingCart
                        "Rent" -> Icons.Default.Home
                        "Aura Sync" -> Icons.Default.Sensors
                        "Cross-Border" -> Icons.Default.Public
                        else -> Icons.Default.ArrowUpward
                    },
                    contentDescription = tx.category,
                    tint = if (isReceive) LinkEmeraldGreen
                    else when (tx.category) {
                        "Groceries" -> LinkEmeraldGreen
                        "Rent" -> LinkBkBlue
                        "Aura Sync" -> LinkDeepTeal
                        else -> LinkDarkTeal
                    },
                    modifier = Modifier.size(20.dp)
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = if (isReceive) "${Localization.getString(appLanguage, "received_from")} ${tx.recipient}" else tx.recipient,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = LinkTextPrimary,
                        maxLines = 1
                    )
                    
                    // Language Badge with fixed maxLines to prevent vertical wrap
                    Surface(
                        color = LinkMintContainer,
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text(
                            text = tx.languageUsed,
                            style = MaterialTheme.typography.labelSmall,
                            color = LinkDarkTeal,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                        )
                    }
                }

                Text(
                    text = "${tx.purpose} • ${tx.sourceAccount}",
                    style = MaterialTheme.typography.bodySmall,
                    color = LinkTextSecondary,
                    fontSize = 11.sp,
                    maxLines = 1
                )
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = if (isReceive) "+${currencyFormatter.format(tx.amount)} ${tx.currency}"
                           else "-${currencyFormatter.format(tx.amount)} ${tx.currency}",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.ExtraBold,
                    color = if (isReceive) LinkEmeraldGreen else LinkTextPrimary
                )
                Text(
                    text = dateFormat.format(Date(tx.timestamp)),
                    style = MaterialTheme.typography.labelSmall,
                    color = LinkTextSecondary,
                    fontSize = 10.sp
                )
            }
        }
    }
}

