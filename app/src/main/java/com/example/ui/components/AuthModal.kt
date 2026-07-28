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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ui.theme.*

enum class AuthScreenMode {
    LOGIN, REGISTER, FORGOT_PASSWORD
}

@Composable
fun AuthModal(
    onDismiss: () -> Unit,
    onAuthSuccess: (String) -> Unit
) {
    var mode by remember { mutableStateOf(AuthScreenMode.LOGIN) }
    var phoneNumber by remember { mutableStateOf("788123456") }
    var countryCode by remember { mutableStateOf("+250 (RW)") }
    var password by remember { mutableStateOf("••••••••") }
    var fullName by remember { mutableStateOf("Keza Rugamba") }
    var selectedLanguage by remember { mutableStateOf("Kinyarwanda") }
    var enableBiometrics by remember { mutableStateOf(true) }
    var isSubmitting by remember { mutableStateOf(false) }

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
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Surface(
                            color = LinkDeepTeal,
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = "LINK",
                                fontWeight = FontWeight.ExtraBold,
                                color = LinkSurfaceCard,
                                fontSize = 16.sp,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                        Text(
                            text = when (mode) {
                                AuthScreenMode.LOGIN -> "Account Sign In"
                                AuthScreenMode.REGISTER -> "Register LINK ID"
                                AuthScreenMode.FORGOT_PASSWORD -> "Reset Security PIN"
                            },
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = LinkTextPrimary
                        )
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.size(32.dp).testTag("btn_close_auth")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = LinkTextSecondary
                        )
                    }
                }

                // Mode Selector Segmented Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    FilterChip(
                        selected = mode == AuthScreenMode.LOGIN,
                        onClick = { mode = AuthScreenMode.LOGIN },
                        label = { Text("Sign In", fontSize = 12.sp) },
                        modifier = Modifier.weight(1f),
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = LinkMintContainer,
                            selectedLabelColor = LinkDarkTeal
                        )
                    )
                    FilterChip(
                        selected = mode == AuthScreenMode.REGISTER,
                        onClick = { mode = AuthScreenMode.REGISTER },
                        label = { Text("Register", fontSize = 12.sp) },
                        modifier = Modifier.weight(1f),
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = LinkMintContainer,
                            selectedLabelColor = LinkDarkTeal
                        )
                    )
                }

                // Form Fields
                if (mode == AuthScreenMode.REGISTER) {
                    OutlinedTextField(
                        value = fullName,
                        onValueChange = { fullName = it },
                        label = { Text("Full Name / Business Title") },
                        leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = countryCode,
                        onValueChange = { countryCode = it },
                        label = { Text("Region") },
                        modifier = Modifier.width(110.dp),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = phoneNumber,
                        onValueChange = { phoneNumber = it },
                        label = { Text("Phone Number") },
                        leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null) },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                }

                if (mode != AuthScreenMode.FORGOT_PASSWORD) {
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("6-Digit Security PIN") },
                        leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                }

                if (mode == AuthScreenMode.REGISTER) {
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(
                            text = "Preferred Language for AI Assistant",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = LinkTextPrimary
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            listOf("Kinyarwanda", "English", "Kiswahili", "French").forEach { lang ->
                                FilterChip(
                                    selected = selectedLanguage == lang,
                                    onClick = { selectedLanguage = lang },
                                    label = { Text(lang, fontSize = 11.sp) },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = LinkMintContainer,
                                        selectedLabelColor = LinkDarkTeal
                                    )
                                )
                            }
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Enable Biometric Authentication",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = LinkTextPrimary
                        )
                        Switch(
                            checked = enableBiometrics,
                            onCheckedChange = { enableBiometrics = it },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = LinkSurfaceCard,
                                checkedTrackColor = LinkDeepTeal
                            )
                        )
                    }
                }

                // Quick Demo Sign In Accent Button
                Button(
                    onClick = {
                        isSubmitting = true
                        onAuthSuccess("Keza Rugamba")
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = LinkDeepTeal,
                        contentColor = LinkSurfaceCard
                    ),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("btn_submit_auth")
                ) {
                    if (isSubmitting) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = LinkSurfaceCard,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.Fingerprint,
                            contentDescription = "Authenticate"
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = when (mode) {
                                AuthScreenMode.LOGIN -> "Sign In (Biometric / PIN)"
                                AuthScreenMode.REGISTER -> "Register & Link Account"
                                AuthScreenMode.FORGOT_PASSWORD -> "Send Verification Code"
                            },
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}
