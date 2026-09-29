package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = VibeAccent,
    onPrimary = Color.White,
    primaryContainer = VibeAccent2,
    onPrimaryContainer = Color.White,
    secondary = VibeGold,
    onSecondary = Color.Black,
    tertiary = VibeGreen,
    onTertiary = Color.White,
    background = VibeBg,
    onBackground = VibeText,
    surface = VibeSurface,
    onSurface = VibeText,
    surfaceVariant = VibeCard,
    onSurfaceVariant = VibeMuted,
    outline = VibeBorder
)

@Composable
fun MyApplicationTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography,
        content = content
    )
}
