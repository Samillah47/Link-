package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val ProfessionalPolishColorScheme = lightColorScheme(
    primary = LinkDeepTeal,
    onPrimary = LinkSurfaceCard,
    primaryContainer = LinkMintContainer,
    onPrimaryContainer = LinkDarkTeal,
    secondary = LinkEmeraldGreen,
    onSecondary = LinkSurfaceCard,
    tertiary = LinkAmberGold,
    background = LinkLightBackground,
    onBackground = LinkTextPrimary,
    surface = LinkSurfaceCard,
    onSurface = LinkTextPrimary,
    surfaceVariant = LinkSurfaceBorder,
    onSurfaceVariant = LinkTextSecondary,
    error = LinkErrorRed
)

@Composable
fun LINKTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = ProfessionalPolishColorScheme,
        typography = Typography,
        content = content
    )
}

