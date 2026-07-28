package com.example.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.components.IntentConfirmationSheet
import com.example.ui.components.NotificationCenterDialog
import com.example.ui.screens.*
import com.example.ui.theme.*
import com.example.util.AppLanguage
import com.example.util.Localization

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewModel: LinkViewModel = viewModel()
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    var showNotificationsDialog by remember { mutableStateOf(false) }
    var languageMenuExpanded by remember { mutableStateOf(false) }

    val showConfirmation by viewModel.showPaymentConfirmation.collectAsState()
    val parsedResult by viewModel.parsedResult.collectAsState()
    val appLanguage by viewModel.appLanguage.collectAsState()
    val notifications by viewModel.notifications.collectAsState()

    val unreadCount = notifications.count { !it.isRead }

    if (showNotificationsDialog) {
        NotificationCenterDialog(
            notifications = notifications,
            appLanguage = appLanguage,
            onDismiss = {
                showNotificationsDialog = false
                viewModel.markNotificationsRead()
            },
            onClearAll = {}
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
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
                        Column {
                            Text(
                                text = "LINK Financial OS",
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.titleMedium,
                                color = LinkTextPrimary
                            )
                            Text(
                                text = Localization.getString(appLanguage, "app_slogan"),
                                style = MaterialTheme.typography.labelSmall,
                                color = LinkDeepTeal,
                                fontSize = 10.sp
                            )
                        }
                    }
                },
                actions = {
                    // Language Switcher Dropdown Chip
                    Box {
                        Surface(
                            color = LinkMintContainer,
                            shape = RoundedCornerShape(20.dp),
                            onClick = { languageMenuExpanded = true },
                            modifier = Modifier.padding(end = 4.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(
                                    text = "${appLanguage.flag} ${appLanguage.name}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = LinkDarkTeal,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp
                                )
                                Icon(
                                    imageVector = Icons.Default.ArrowDropDown,
                                    contentDescription = "Select Language",
                                    tint = LinkDarkTeal,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }

                        DropdownMenu(
                            expanded = languageMenuExpanded,
                            onDismissRequest = { languageMenuExpanded = false }
                        ) {
                            AppLanguage.values().forEach { lang ->
                                DropdownMenuItem(
                                    text = { Text("${lang.flag} ${lang.displayName}") },
                                    onClick = {
                                        viewModel.setLanguage(lang)
                                        languageMenuExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    // Notifications Bell with Badge Counter
                    IconButton(
                        onClick = { showNotificationsDialog = true },
                        modifier = Modifier.testTag("btn_top_notifications")
                    ) {
                        BadgedBox(
                            badge = {
                                if (unreadCount > 0) {
                                    Badge(
                                        containerColor = LinkEmeraldGreen,
                                        contentColor = Color.White
                                    ) {
                                        Text(unreadCount.toString(), fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Notifications,
                                contentDescription = "Notifications",
                                tint = LinkDeepTeal
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = LinkLightBackground,
                    titleContentColor = LinkTextPrimary
                )
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = LinkSurfaceCard,
                contentColor = LinkDeepTeal,
                tonalElevation = 8.dp,
                modifier = Modifier.windowInsetsPadding(WindowInsets.navigationBars)
            ) {
                NavigationBarItem(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    icon = { Icon(Icons.Default.AccountBalanceWallet, contentDescription = "Hub") },
                    label = {
                        Text(
                            text = Localization.getString(appLanguage, "nav_hub"),
                            fontSize = 11.sp,
                            fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Normal,
                            maxLines = 1
                        )
                    },
                    modifier = Modifier.testTag("nav_tab_hub"),
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = LinkSurfaceCard,
                        selectedTextColor = LinkDeepTeal,
                        indicatorColor = LinkDeepTeal,
                        unselectedIconColor = LinkTextSecondary,
                        unselectedTextColor = LinkTextSecondary
                    )
                )

                NavigationBarItem(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    icon = { Icon(Icons.Default.AutoAwesome, contentDescription = "AI Intent") },
                    label = {
                        Text(
                            text = Localization.getString(appLanguage, "nav_ai_pay"),
                            fontSize = 11.sp,
                            fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Normal,
                            maxLines = 1
                        )
                    },
                    modifier = Modifier.testTag("nav_tab_ai_intent"),
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = LinkSurfaceCard,
                        selectedTextColor = LinkDeepTeal,
                        indicatorColor = LinkDeepTeal,
                        unselectedIconColor = LinkTextSecondary,
                        unselectedTextColor = LinkTextSecondary
                    )
                )

                NavigationBarItem(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    icon = { Icon(Icons.Default.Sensors, contentDescription = "Aura Sync") },
                    label = {
                        Text(
                            text = Localization.getString(appLanguage, "nav_aura_sync"),
                            fontSize = 11.sp,
                            fontWeight = if (selectedTab == 2) FontWeight.Bold else FontWeight.Normal,
                            maxLines = 1
                        )
                    },
                    modifier = Modifier.testTag("nav_tab_aura_sync"),
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = LinkSurfaceCard,
                        selectedTextColor = LinkDarkButton,
                        indicatorColor = LinkDarkButton,
                        unselectedIconColor = LinkTextSecondary,
                        unselectedTextColor = LinkTextSecondary
                    )
                )

                NavigationBarItem(
                    selected = selectedTab == 3,
                    onClick = { selectedTab = 3 },
                    icon = { Icon(Icons.Default.Psychology, contentDescription = "Memory") },
                    label = {
                        Text(
                            text = Localization.getString(appLanguage, "nav_memory"),
                            fontSize = 11.sp,
                            fontWeight = if (selectedTab == 3) FontWeight.Bold else FontWeight.Normal,
                            maxLines = 1
                        )
                    },
                    modifier = Modifier.testTag("nav_tab_memory"),
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = LinkSurfaceCard,
                        selectedTextColor = LinkDeepTeal,
                        indicatorColor = LinkDeepTeal,
                        unselectedIconColor = LinkTextSecondary,
                        unselectedTextColor = LinkTextSecondary
                    )
                )

                NavigationBarItem(
                    selected = selectedTab == 4,
                    onClick = { selectedTab = 4 },
                    icon = { Icon(Icons.Default.Person, contentDescription = "Account") },
                    label = {
                        Text(
                            text = Localization.getString(appLanguage, "nav_account"),
                            fontSize = 11.sp,
                            fontWeight = if (selectedTab == 4) FontWeight.Bold else FontWeight.Normal,
                            maxLines = 1
                        )
                    },
                    modifier = Modifier.testTag("nav_tab_account"),
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = LinkSurfaceCard,
                        selectedTextColor = LinkDarkTeal,
                        indicatorColor = LinkDarkTeal,
                        unselectedIconColor = LinkTextSecondary,
                        unselectedTextColor = LinkTextSecondary
                    )
                )
            }
        },
        containerColor = LinkLightBackground
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (selectedTab) {
                0 -> DashboardScreen(
                    viewModel = viewModel,
                    onNavigateToAiPay = { selectedTab = 1 },
                    onNavigateToAuraSync = { selectedTab = 2 },
                    onNavigateToAccount = { selectedTab = 4 }
                )
                1 -> AiIntentScreen(viewModel = viewModel)
                2 -> AuraSyncScreen(viewModel = viewModel)
                3 -> FinancialMemoryScreen(viewModel = viewModel)
                4 -> AccountScreen(viewModel = viewModel)
            }

            // Payment Confirmation Sheet
            if (showConfirmation && parsedResult != null) {
                IntentConfirmationSheet(
                    result = parsedResult!!,
                    onConfirm = { viewModel.confirmAndExecutePayment() },
                    onDismiss = { viewModel.dismissConfirmation() }
                )
            }
        }
    }
}
