package com.example.ui.screens

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
import com.example.ui.LinkViewModel
import com.example.ui.components.AuthModal
import com.example.ui.components.EditCredentialsDialog
import com.example.ui.theme.*
import com.example.util.AppLanguage
import com.example.util.Localization

@Composable
fun AccountScreen(
    viewModel: LinkViewModel
) {
    val appLanguage by viewModel.appLanguage.collectAsState()

    var biometricEnabled by remember { mutableStateOf(true) }
    var highValueAlerts by remember { mutableStateOf(true) }
    var showResetSuccess by remember { mutableStateOf(false) }
    var showAuthModal by remember { mutableStateOf(false) }
    var showEditCredentials by remember { mutableStateOf(false) }

    var currentUserName by remember { mutableStateOf("Keza Rugamba") }
    var currentPhone by remember { mutableStateOf("+250 788 123 456") }
    var currentNationalId by remember { mutableStateOf("1 1998 8 0012345 1 20") }
    var currentBankAcc by remember { mutableStateOf("00011-889922-01") }

    if (showAuthModal) {
        AuthModal(
            onDismiss = { showAuthModal = false },
            onAuthSuccess = { userName ->
                currentUserName = userName
                showAuthModal = false
            }
        )
    }

    if (showEditCredentials) {
        EditCredentialsDialog(
            initialName = currentUserName,
            initialPhone = currentPhone,
            initialNationalId = currentNationalId,
            initialBankAcc = currentBankAcc,
            onDismiss = { showEditCredentials = false },
            onSave = { name, phone, nid, bank ->
                currentUserName = name
                currentPhone = phone
                currentNationalId = nid
                currentBankAcc = bank
                showEditCredentials = false
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(LinkLightBackground)
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // User Profile Header Card
        Card(
            colors = CardDefaults.cardColors(containerColor = LinkSurfaceCard),
            shape = RoundedCornerShape(24.dp),
            border = BorderStroke(1.dp, LinkSurfaceBorder),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .clip(CircleShape)
                            .background(LinkMintContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (currentUserName.length >= 2) currentUserName.take(2).uppercase() else "KR",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.ExtraBold,
                            color = LinkDarkTeal
                        )
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = currentUserName,
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = LinkTextPrimary
                            )
                            Icon(
                                imageVector = Icons.Default.Verified,
                                contentDescription = "Verified ID",
                                tint = LinkDeepTeal,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        Text(
                            text = "$currentPhone • @keza.rw",
                            style = MaterialTheme.typography.bodyMedium,
                            color = LinkTextSecondary
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Button(
                                onClick = { showEditCredentials = true },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = LinkMintContainer,
                                    contentColor = LinkDarkTeal
                                ),
                                shape = RoundedCornerShape(10.dp),
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                                modifier = Modifier.height(32.dp).testTag("btn_edit_credentials")
                            ) {
                                Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = Localization.getString(appLanguage, "edit_credentials"),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Button(
                                onClick = { showAuthModal = true },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFFFEE2E2),
                                    contentColor = Color(0xFF991B1B)
                                ),
                                shape = RoundedCornerShape(10.dp),
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                                modifier = Modifier.height(32.dp).testTag("btn_logout")
                            ) {
                                Icon(Icons.Default.Logout, contentDescription = null, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = Localization.getString(appLanguage, "logout"),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }

        // Linked Financial Credentials
        Card(
            colors = CardDefaults.cardColors(containerColor = LinkSurfaceCard),
            shape = RoundedCornerShape(20.dp),
            border = BorderStroke(1.dp, LinkSurfaceBorder),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Text(
                    text = Localization.getString(appLanguage, "linked_credentials"),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = LinkTextPrimary
                )

                CredentialItem(
                    icon = Icons.Default.PhoneAndroid,
                    provider = "MTN Mobile Money",
                    identifier = currentPhone,
                    status = "Active Token",
                    color = LinkAmberGold
                )

                HorizontalDivider(color = LinkSurfaceBorder)

                CredentialItem(
                    icon = Icons.Default.Badge,
                    provider = "RW National ID (NIDA)",
                    identifier = currentNationalId,
                    status = "KYC Verified",
                    color = LinkDeepTeal
                )

                HorizontalDivider(color = LinkSurfaceBorder)

                CredentialItem(
                    icon = Icons.Default.AccountBalanceWallet,
                    provider = "eCash Rwanda Protocol",
                    identifier = "ECASH-RW-88291",
                    status = "Instant Settlement",
                    color = LinkEmeraldGreen
                )

                HorizontalDivider(color = LinkSurfaceBorder)

                CredentialItem(
                    icon = Icons.Default.AccountBalance,
                    provider = "Bank of Kigali",
                    identifier = currentBankAcc,
                    status = "OAuth Synced",
                    color = LinkBkBlue
                )
            }
        }

        // AI Guardian & Security Preferences
        Card(
            colors = CardDefaults.cardColors(containerColor = LinkSurfaceCard),
            shape = RoundedCornerShape(20.dp),
            border = BorderStroke(1.dp, LinkSurfaceBorder),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Text(
                    text = Localization.getString(appLanguage, "security_settings"),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = LinkTextPrimary
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = Localization.getString(appLanguage, "biometric_confirmation"),
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = LinkTextPrimary
                        )
                        Text(
                            text = "Require fingerprint/PIN before executing any AI routing",
                            style = MaterialTheme.typography.bodySmall,
                            color = LinkTextSecondary
                        )
                    }
                    Switch(
                        checked = biometricEnabled,
                        onCheckedChange = { biometricEnabled = it },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = LinkSurfaceCard,
                            checkedTrackColor = LinkDeepTeal
                        )
                    )
                }

                HorizontalDivider(color = LinkSurfaceBorder)

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = Localization.getString(appLanguage, "payment_alerts"),
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = LinkTextPrimary
                        )
                        Text(
                            text = "AI Guardian warns on transactions > 100,000 RWF",
                            style = MaterialTheme.typography.bodySmall,
                            color = LinkTextSecondary
                        )
                    }
                    Switch(
                        checked = highValueAlerts,
                        onCheckedChange = { highValueAlerts = it },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = LinkSurfaceCard,
                            checkedTrackColor = LinkDeepTeal
                        )
                    )
                }

                HorizontalDivider(color = LinkSurfaceBorder)

                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(
                        text = Localization.getString(appLanguage, "app_language"),
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = LinkTextPrimary
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        AppLanguage.values().forEach { lang ->
                            FilterChip(
                                selected = appLanguage == lang,
                                onClick = { viewModel.setLanguage(lang) },
                                label = { Text("${lang.flag} ${lang.displayName}", fontSize = 12.sp) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = LinkMintContainer,
                                    selectedLabelColor = LinkDarkTeal
                                )
                            )
                        }
                    }
                }
            }
        }

        // System Node & Environment Info
        Card(
            colors = CardDefaults.cardColors(containerColor = LinkSurfaceCard),
            shape = RoundedCornerShape(20.dp),
            border = BorderStroke(1.dp, LinkSurfaceBorder),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "System Node & AI API Status",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = LinkTextPrimary
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Gemini 3.5 Flash NLU", style = MaterialTheme.typography.bodyMedium, color = LinkTextSecondary)
                    Text("ONLINE (Server Key)", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, color = LinkEmeraldGreen)
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Aura Sync Security Engine", style = MaterialTheme.typography.bodyMedium, color = LinkTextSecondary)
                    Text("256-bit ECDH Active", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, color = LinkDeepTeal)
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Regional Financial Gateway", style = MaterialTheme.typography.bodyMedium, color = LinkTextSecondary)
                    Text("rw-kgl-01 (Kigali)", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, color = LinkTextPrimary)
                }
            }
        }

        // Demo Data Reset Action
        Card(
            colors = CardDefaults.cardColors(containerColor = LinkSurfaceCard),
            shape = RoundedCornerShape(20.dp),
            border = BorderStroke(1.dp, LinkSurfaceBorder),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Demo Data Control",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = LinkTextPrimary
                )

                Text(
                    text = "Reset all connected financial balances and clear transaction history back to initial state.",
                    style = MaterialTheme.typography.bodySmall,
                    color = LinkTextSecondary
                )

                Button(
                    onClick = {
                        viewModel.resetDemoData()
                        showResetSuccess = true
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = LinkDarkButton,
                        contentColor = LinkSurfaceCard
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("btn_reset_demo_data")
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Reset",
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Reset Demo Seed Data", fontWeight = FontWeight.Bold)
                }

                if (showResetSuccess) {
                    Text(
                        text = "✓ Seed data reset successfully!",
                        style = MaterialTheme.typography.labelSmall,
                        color = LinkEmeraldGreen,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun CredentialItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    provider: String,
    identifier: String,
    status: String,
    color: Color
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(color.copy(alpha = 0.12f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = provider,
                tint = color,
                modifier = Modifier.size(20.dp)
            )
        }

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = provider,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = LinkTextPrimary
            )
            Text(
                text = identifier,
                style = MaterialTheme.typography.bodySmall,
                color = LinkTextSecondary
            )
        }

        Surface(
            color = color.copy(alpha = 0.12f),
            shape = RoundedCornerShape(6.dp)
        ) {
            Text(
                text = status,
                style = MaterialTheme.typography.labelSmall,
                color = color,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
            )
        }
    }
}
