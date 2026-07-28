package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ui.LinkNotification
import com.example.ui.theme.*
import com.example.util.AppLanguage
import com.example.util.Localization
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun NotificationCenterDialog(
    notifications: List<LinkNotification>,
    appLanguage: AppLanguage,
    onDismiss: () -> Unit,
    onClearAll: () -> Unit
) {
    val dateFormat = SimpleDateFormat("HH:mm, MMM dd", Locale.getDefault())

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
                    .fillMaxWidth(),
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
                                    imageVector = Icons.Default.NotificationsActive,
                                    contentDescription = null,
                                    tint = LinkDarkTeal,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                        Text(
                            text = Localization.getString(appLanguage, "notifications"),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = LinkTextPrimary
                        )
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.size(32.dp).testTag("btn_close_notif")
                    ) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = LinkTextSecondary)
                    }
                }

                if (notifications.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = Localization.getString(appLanguage, "no_notifications"),
                            color = LinkTextSecondary,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 380.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(notifications) { notif ->
                            NotificationRowItem(notif = notif, dateFormat = dateFormat)
                        }
                    }
                }

                TextButton(
                    onClick = onDismiss,
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text("Close", color = LinkDeepTeal, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun NotificationRowItem(
    notif: LinkNotification,
    dateFormat: SimpleDateFormat
) {
    val icon = when (notif.type) {
        "RECEIVE" -> Icons.Default.ArrowDownward
        "SEND" -> Icons.Default.ArrowUpward
        "SECURITY" -> Icons.Default.Security
        else -> Icons.Default.Info
    }

    val iconBg = when (notif.type) {
        "RECEIVE" -> LinkEmeraldGreen
        "SEND" -> LinkDeepTeal
        "SECURITY" -> Color(0xFFD97706)
        else -> LinkMintContainer
    }

    Surface(
        color = Color.White,
        shape = RoundedCornerShape(14.dp),
        border = BorderStroke(1.dp, LinkSurfaceBorder),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.Top
        ) {
            Surface(
                color = iconBg,
                shape = CircleShape,
                modifier = Modifier.size(36.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = notif.title,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = LinkTextPrimary
                    )
                    Text(
                        text = dateFormat.format(Date(notif.timestamp)),
                        style = MaterialTheme.typography.labelSmall,
                        color = LinkTextSecondary,
                        fontSize = 10.sp
                    )
                }
                Text(
                    text = notif.message,
                    style = MaterialTheme.typography.bodySmall,
                    color = LinkTextSecondary,
                    fontSize = 12.sp
                )
            }
        }
    }
}
