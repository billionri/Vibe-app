package com.example.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// Exact color palette from https://business-apps.infinityfreeapp.com/vibe-playlist.html
val VibeBg = Color(0xFF0A0A0F)
val VibeSurface = Color(0xFF13131A)
val VibeCard = Color(0xFF1A1A24)
val VibeBorder = Color(0xFF2A2A3A)
val VibeAccent = Color(0xFFFF3C6E) // Pink-red
val VibeAccent2 = Color(0xFF7C3AED) // Purple
val VibeGold = Color(0xFFF5C842)
val VibeGreen = Color(0xFF1DB954)
val VibeText = Color(0xFFE8E8F0)
val VibeMuted = Color(0xFF6B6B80)

// Gradients
val VibeLogoBrush = Brush.linearGradient(listOf(VibeAccent, VibeAccent2))
val VibeGoldBrush = Brush.linearGradient(listOf(Color(0xFFF5C842), Color(0xFFFFA500)))
val VibeGreenBrush = Brush.linearGradient(listOf(Color(0xFF1DB954), Color(0xFF17A045)))
val VibeCardBorderBrush = Brush.linearGradient(listOf(VibeBorder, VibeAccent.copy(alpha = 0.3f)))

// Presets for categories
val VibeMoodLabels = listOf(
    "Romantic" to "❤️" to Color(0xFFFF5078),
    "Sad" to "😢" to Color(0xFF5078FF),
    "Party" to "🎉" to Color(0xFFFFB400),
    "Gym" to "💪" to Color(0xFF1DB954),
    "Chill" to "😌" to Color(0xFF64B4FF),
    "Hype" to "🔥" to Color(0xFFFF5000),
    "Night" to "🌙" to Color(0xFF783CC8),
    "Morning" to "☀️" to Color(0xFFFFC800)
)
