package com.example.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
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
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.AuraSyncStep
import com.example.ui.LinkViewModel
import com.example.ui.theme.*

@Composable
fun AuraSyncScreen(
    viewModel: LinkViewModel
) {
    val auraStep by viewModel.auraStep.collectAsState()
    val auraMode by viewModel.auraMode.collectAsState()
    val recipientInput by viewModel.auraRecipientInput.collectAsState()
    val amount by viewModel.auraAmount.collectAsState()
    val appLanguage by viewModel.appLanguage.collectAsState()

    var customAmountText by remember { mutableStateOf(amount.toInt().toString()) }

    // Pulsing animation for proximity radar
    val infiniteTransition = rememberInfiniteTransition(label = "aura_pulse")
    val pulseRadius by infiniteTransition.animateFloat(
        initialValue = 40f,
        targetValue = 150f,
        animationSpec = infiniteRepeatable(
            animation = tween(1300, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "pulse_radius"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(LinkNavyBackground)
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Title Header
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Sensors,
                    contentDescription = "Aura Sync",
                    tint = LinkEmeraldGreen
                )
                Text(
                    text = "Aura Sync Proximity",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = LinkEmeraldGreen
                )
            }
            Text(
                text = "Encrypted human-to-human payment linking via eCash protocol. Zero phone numbers required.",
                style = MaterialTheme.typography.bodySmall,
                color = LinkTextSecondary
            )
        }

        // Mode Switcher: Send vs Receive
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(LinkSurfaceCard, RoundedCornerShape(16.dp))
                .padding(4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Button(
                onClick = { viewModel.setAuraMode("SEND") },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (auraMode == "SEND") LinkDeepTeal else Color.Transparent,
                    contentColor = if (auraMode == "SEND") LinkSurfaceCard else LinkTextSecondary
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.weight(1f).testTag("btn_aura_mode_send")
            ) {
                Icon(Icons.Default.ArrowUpward, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Send Money", fontWeight = FontWeight.Bold)
            }

            Button(
                onClick = { viewModel.setAuraMode("RECEIVE") },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (auraMode == "RECEIVE") LinkEmeraldGreen else Color.Transparent,
                    contentColor = if (auraMode == "RECEIVE") LinkNavyBackground else LinkTextSecondary
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.weight(1f).testTag("btn_aura_mode_receive")
            ) {
                Icon(Icons.Default.ArrowDownward, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Receive Money", fontWeight = FontWeight.Bold)
            }
        }

        // Radar Visualizer Box
        Card(
            colors = CardDefaults.cardColors(containerColor = LinkSurfaceCard),
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp)
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                if (auraStep == AuraSyncStep.SCANNING || auraStep == AuraSyncStep.DEVICE_FOUND || auraMode == "RECEIVE") {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        drawCircle(
                            color = if (auraMode == "SEND") LinkEmeraldGreen.copy(alpha = (150f - pulseRadius) / 150f) else LinkElectricCyan.copy(alpha = (150f - pulseRadius) / 150f),
                            radius = pulseRadius * 1.4f,
                            style = Stroke(width = 4.dp.toPx())
                        )
                        drawCircle(
                            color = LinkEmeraldGreen.copy(alpha = (150f - pulseRadius) / 150f),
                            radius = pulseRadius,
                            style = Stroke(width = 2.dp.toPx())
                        )
                    }
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape)
                            .background(
                                when (auraStep) {
                                    AuraSyncStep.SUCCESS -> LinkEmeraldGreen
                                    AuraSyncStep.CONFIRM_PAYMENT -> LinkElectricCyan
                                    AuraSyncStep.SCANNING -> LinkAmberGold
                                    else -> if (auraMode == "RECEIVE") LinkDeepTeal else LinkSurfaceBorder
                                }
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = when {
                                auraStep == AuraSyncStep.SUCCESS -> Icons.Default.CheckCircle
                                auraStep == AuraSyncStep.CONFIRM_PAYMENT -> Icons.Default.PhonelinkRing
                                auraStep == AuraSyncStep.SCANNING -> Icons.Default.Radar
                                auraMode == "RECEIVE" -> Icons.Default.QrCodeScanner
                                else -> Icons.Default.Smartphone
                            },
                            contentDescription = "Status Icon",
                            tint = if (auraStep == AuraSyncStep.IDLE && auraMode == "SEND") LinkTextSecondary else LinkNavyBackground,
                            modifier = Modifier.size(42.dp)
                        )
                    }

                    Text(
                        text = when (auraStep) {
                            AuraSyncStep.IDLE -> if (auraMode == "SEND") "Bring phones close to pay $recipientInput" else "Aura Beacon Active for @keza.rw"
                            AuraSyncStep.SCANNING -> "Scanning encrypted UWB/BLE channel for $recipientInput..."
                            AuraSyncStep.DEVICE_FOUND -> "Proximity Handshake Verified with $recipientInput"
                            AuraSyncStep.SECURE_HANDSHAKE -> "Creating temporary 256-bit ECDH session..."
                            AuraSyncStep.CONFIRM_PAYMENT -> "Authorized Session Ready: $recipientInput"
                            AuraSyncStep.SUCCESS -> if (auraMode == "SEND") "Payment Sent & Channel Burned! 🔥" else "Money Received via Aura Sync! 🎉"
                        },
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = LinkTextPrimary,
                        modifier = Modifier.padding(horizontal = 20.dp)
                    )
                }
            }
        }

        // Configuration Card: Recipient/Sender Handle & Amount Entry
        if (auraStep == AuraSyncStep.IDLE || auraStep == AuraSyncStep.CONFIRM_PAYMENT) {
            Card(
                colors = CardDefaults.cardColors(containerColor = LinkSurfaceCard),
                shape = RoundedCornerShape(20.dp),
                border = BorderStroke(1.dp, LinkSurfaceBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = if (auraMode == "SEND") "Recipient & Amount" else "Sender & Expected Amount",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = LinkTextPrimary
                    )

                    OutlinedTextField(
                        value = recipientInput,
                        onValueChange = { viewModel.setAuraRecipient(it) },
                        label = { Text(if (auraMode == "SEND") "Recipient Handle" else "Sender Handle") },
                        leadingIcon = { Icon(Icons.Default.AlternateEmail, contentDescription = null, tint = LinkDeepTeal) },
                        modifier = Modifier.fillMaxWidth().testTag("input_aura_recipient"),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = LinkDeepTeal,
                            unfocusedBorderColor = LinkSurfaceBorder
                        )
                    )

                    // Quick Handle Pills
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        listOf("@alice", "@bob", "@mutesi", "@claire").forEach { handle ->
                            SuggestionChip(
                                onClick = { viewModel.setAuraRecipient(handle) },
                                label = { Text(handle, fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                                colors = SuggestionChipDefaults.suggestionChipColors(
                                    containerColor = if (recipientInput == handle) LinkMintContainer else LinkNavyBackground,
                                    labelColor = if (recipientInput == handle) LinkDarkTeal else LinkTextSecondary
                                )
                            )
                        }
                    }

                    OutlinedTextField(
                        value = customAmountText,
                        onValueChange = { input ->
                            customAmountText = input
                            input.toDoubleOrNull()?.let { viewModel.updateAuraAmount(it) }
                        },
                        label = { Text("Amount (RWF)") },
                        leadingIcon = { Icon(Icons.Default.AttachMoney, contentDescription = null, tint = LinkEmeraldGreen) },
                        modifier = Modifier.fillMaxWidth().testTag("input_aura_amount"),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = LinkEmeraldGreen,
                            unfocusedBorderColor = LinkSurfaceBorder
                        )
                    )

                    // Quick Amount Chips
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        listOf(2000, 5000, 12000, 25000).forEach { amt ->
                            FilterChip(
                                selected = amount.toInt() == amt,
                                onClick = {
                                    viewModel.updateAuraAmount(amt.toDouble())
                                    customAmountText = amt.toString()
                                },
                                label = { Text("${amt / 1000}k RWF", fontSize = 11.sp) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = LinkEmeraldGreen,
                                    selectedLabelColor = LinkNavyBackground
                                )
                            )
                        }
                    }
                }
            }
        }

        // Action Trigger Button
        when (auraStep) {
            AuraSyncStep.IDLE -> {
                Button(
                    onClick = {
                        if (auraMode == "SEND") {
                            viewModel.startAuraSyncScan()
                        } else {
                            viewModel.authorizeAuraPayment() // Receive instantly
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (auraMode == "SEND") LinkDeepTeal else LinkEmeraldGreen,
                        contentColor = if (auraMode == "SEND") LinkSurfaceCard else LinkNavyBackground
                    ),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp)
                        .testTag("btn_action_aura_trigger")
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = if (auraMode == "SEND") Icons.Default.Sensors else Icons.Default.QrCode,
                            contentDescription = "Action"
                        )
                        Text(
                            text = if (auraMode == "SEND") "Pay $recipientInput (${amount.toInt()} RWF)" else "Receive ${amount.toInt()} RWF from $recipientInput",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                    }
                }
            }
            AuraSyncStep.DEVICE_FOUND -> {
                Button(
                    onClick = { viewModel.connectAuraDevice() },
                    colors = ButtonDefaults.buttonColors(containerColor = LinkElectricCyan, contentColor = LinkNavyBackground),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp)
                        .testTag("btn_connect_aura")
                ) {
                    Text("Confirm Handshake with $recipientInput", fontWeight = FontWeight.Bold)
                }
            }
            AuraSyncStep.CONFIRM_PAYMENT -> {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Button(
                        onClick = { viewModel.authorizeAuraPayment() },
                        colors = ButtonDefaults.buttonColors(containerColor = LinkEmeraldGreen, contentColor = LinkNavyBackground),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp)
                            .testTag("btn_authorize_aura_pay")
                    ) {
                        Text("Authorize Aura Transfer (${amount.toInt()} RWF)", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    }
                }
            }
            AuraSyncStep.SUCCESS -> {
                Card(
                    colors = CardDefaults.cardColors(containerColor = LinkMintContainer),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = LinkEmeraldGreen, modifier = Modifier.size(36.dp))
                        Text(
                            text = if (auraMode == "SEND") "Transferred ${amount.toInt()} RWF to $recipientInput!" else "Received ${amount.toInt()} RWF from $recipientInput!",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = LinkDarkTeal
                        )
                        Text(
                            text = "0% Protocol Fee • Cryptographic ECDH Channel Burned",
                            style = MaterialTheme.typography.bodySmall,
                            color = LinkDarkTeal.copy(alpha = 0.8f)
                        )
                    }
                }

                Button(
                    onClick = { viewModel.resetAuraSync() },
                    colors = ButtonDefaults.buttonColors(containerColor = LinkSurfaceBorder, contentColor = LinkTextPrimary),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    Text("Done (Reset Aura Link)")
                }
            }
            else -> {
                CircularProgressIndicator(color = LinkEmeraldGreen)
            }
        }

        // Security Model Guarantees Card
        Card(
            colors = CardDefaults.cardColors(containerColor = LinkSurfaceCard),
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Security",
                        tint = LinkAmberGold
                    )
                    Text(
                        text = "Aura Sync Proximity Security",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = LinkTextPrimary
                    )
                }

                HorizontalDivider(color = LinkSurfaceBorder)

                SecurityFeatureRow(
                    icon = Icons.Default.VisibilityOff,
                    title = "Never Publicly Discoverable",
                    desc = "Your phone never broadcasts visible Bluetooth or Wi-Fi beacon signals to strangers."
                )

                SecurityFeatureRow(
                    icon = Icons.Default.Rule,
                    title = "Intentional Proximity Handshake",
                    desc = "Both devices exchange temporary 256-bit keys only when within 1 meter."
                )

                SecurityFeatureRow(
                    icon = Icons.Default.Key,
                    title = "Ephemeral Session Burn",
                    desc = "The cryptographic channel self-destructs immediately after payment execution."
                )
            }
        }
    }
}

@Composable
fun SecurityFeatureRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    desc: String
) {
    Row(
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            tint = LinkElectricCyan,
            modifier = Modifier.size(20.dp)
        )
        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = LinkTextPrimary
            )
            Text(
                text = desc,
                style = MaterialTheme.typography.bodySmall,
                color = LinkTextSecondary,
                fontSize = 11.sp
            )
        }
    }
}
