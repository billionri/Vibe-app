package com.example.ui.tabs

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MicOff
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.VibeAppViewModel
import com.example.ui.components.InFeedSponsoredAd
import com.example.util.formatDuration
import com.example.util.launchExternalLink
import com.example.ui.theme.VibeAccent
import com.example.ui.theme.VibeAccent2
import com.example.ui.theme.VibeBorder
import com.example.ui.theme.VibeCard
import com.example.ui.theme.VibeGold
import com.example.ui.theme.VibeGreen
import com.example.ui.theme.VibeMuted
import com.example.ui.theme.VibeSurface
import com.example.ui.theme.VibeText

@Composable
fun KaraokeTab(
    viewModel: VibeAppViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val isRecording by viewModel.isKaraokeRecording.collectAsStateWithLifecycle()
    val recordings by viewModel.karaokeRecordings.collectAsStateWithLifecycle()
    var searchSong by remember { mutableStateOf("") }

    val sampleKaraokeSongs = listOf(
        "Starboy - Karaoke with Lyrics",
        "Sweater Weather - Instrumental Piano",
        "Tum Hi Ho - Aashiqui 2 Karaoke",
        "Die For You - The Weeknd (Lower Key)",
        "Spirited Away (Always With Me) - Flute Karaoke"
    )

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 14.dp)
            .testTag("karaoke_tab_content"),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Column {
                Text(
                    text = "🎤 Karaoke Night — Sing Along",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = VibeText
                )
                Text(
                    text = "Sing with instrumental tracks, lyrics & record your vocals",
                    fontSize = 11.sp,
                    color = VibeMuted
                )
            }
        }

        // Search Karaoke Row
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = searchSong,
                    onValueChange = { searchSong = it },
                    placeholder = { Text("Search song or artist for karaoke...", color = VibeMuted, fontSize = 12.sp) },
                    singleLine = true,
                    modifier = Modifier.weight(1f).testTag("karaoke_search_input"),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = VibeSurface,
                        unfocusedContainerColor = VibeSurface,
                        focusedBorderColor = VibeAccent,
                        unfocusedBorderColor = VibeBorder
                    )
                )

                Button(
                    onClick = {
                        val q = if (searchSong.isNotBlank()) searchSong else "Popular karaoke songs with lyrics"
                        launchExternalLink(context, "$q karaoke lyrics")
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = VibeAccent2, contentColor = Color.White),
                    modifier = Modifier.testTag("karaoke_find_btn")
                ) {
                    Text("Find Karaoke", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        // Live Voice Recording Studio Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = VibeSurface),
                border = androidx.compose.foundation.BorderStroke(1.dp, if (isRecording) VibeAccent else VibeBorder)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = if (isRecording) "🎙️ RECORDING IN PROGRESS..." else "🎙️ VOCAL BOOTH",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Black,
                        color = if (isRecording) VibeAccent else VibeGold
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Live mic visualizer
                    LiveVisualizer(isRecording = isRecording)

                    Spacer(modifier = Modifier.height(14.dp))

                    Button(
                        onClick = { viewModel.toggleKaraokeRecording() },
                        modifier = Modifier
                            .fillMaxWidth(0.7f)
                            .testTag("karaoke_record_btn"),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isRecording) Color(0xFFEF4444) else VibeAccent,
                            contentColor = Color.White
                        )
                    ) {
                        Icon(
                            imageVector = if (isRecording) Icons.Default.Stop else Icons.Default.Mic,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.size(6.dp))
                        Text(
                            text = if (isRecording) "Stop & Save Recording" else "Start Recording",
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // Suggested Karaoke Tracks
        item {
            Text("Trending Karaoke Tracks:", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = VibeMuted)
            Spacer(modifier = Modifier.height(4.dp))
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                sampleKaraokeSongs.forEach { song ->
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .clickable {
                                launchExternalLink(context, "$song with lyrics")
                            },
                        shape = RoundedCornerShape(10.dp),
                        color = VibeSurface,
                        border = androidx.compose.foundation.BorderStroke(1.dp, VibeBorder)
                    ) {
                        Row(
                            modifier = Modifier.padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Text("🎵", fontSize = 16.sp)
                                Text(song, fontSize = 12.sp, color = VibeText, fontWeight = FontWeight.Medium)
                            }
                            Text("Sing ▶", fontSize = 11.sp, color = VibeAccent, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // Sponsored in-feed ad
        item {
            InFeedSponsoredAd(
                title = "JBL Wireless Karaoke Microphone",
                desc = "Plug & play · Vocal tuning effects · Rechargeable battery · 50% discount",
                brand = "JBL Audio India",
                onActionClick = { viewModel.triggerToast("JBL coupon applied!") }
            )
        }

        // Saved Karaoke Recordings
        item {
            Text("My Karaoke Recordings (${recordings.size})", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = VibeText)
        }

        items(recordings, key = { it.id }) { rec ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = VibeSurface),
                border = androidx.compose.foundation.BorderStroke(1.dp, VibeBorder)
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(VibeCard),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("🎙️", fontSize = 16.sp)
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Text(rec.songTitle, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = VibeText)
                        Text("${formatDuration(rec.durationSec)} duration • Saved locally", fontSize = 11.sp, color = VibeMuted)
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(
                            onClick = { viewModel.triggerToast("▶️ Playing ${rec.songTitle}...") },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(Icons.Default.PlayArrow, contentDescription = "Play", tint = VibeGreen)
                        }
                        IconButton(
                            onClick = { viewModel.deleteKaraokeRecording(rec) },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete", tint = VibeMuted)
                        }
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}

@Composable
fun LiveVisualizer(isRecording: Boolean, modifier: Modifier = Modifier) {
    val transition = rememberInfiniteTransition(label = "vis")
    val a1 by transition.animateFloat(0.1f, 1f, infiniteRepeatable(tween(300, easing = FastOutSlowInEasing), RepeatMode.Reverse), label = "a1")
    val a2 by transition.animateFloat(0.8f, 0.2f, infiniteRepeatable(tween(400, easing = FastOutSlowInEasing), RepeatMode.Reverse), label = "a2")
    val a3 by transition.animateFloat(0.3f, 0.9f, infiniteRepeatable(tween(250, easing = FastOutSlowInEasing), RepeatMode.Reverse), label = "a3")
    val a4 by transition.animateFloat(0.9f, 0.1f, infiniteRepeatable(tween(450, easing = FastOutSlowInEasing), RepeatMode.Reverse), label = "a4")
    val a5 by transition.animateFloat(0.4f, 1f, infiniteRepeatable(tween(350, easing = FastOutSlowInEasing), RepeatMode.Reverse), label = "a5")

    val heights = listOf(a1, a2, a3, a4, a5, a2, a4, a1, a3, a5)

    Canvas(modifier = modifier.size(width = 160.dp, height = 36.dp)) {
        val totalBars = heights.size
        val barWidth = size.width / (totalBars * 2f)
        for (i in 0 until totalBars) {
            val scale = if (isRecording) heights[i] else 0.2f
            val h = size.height * scale
            val left = i * (barWidth * 2)
            val top = size.height - h
            drawRoundRect(
                color = if (isRecording) VibeAccent else VibeMuted,
                topLeft = Offset(left, top),
                size = Size(barWidth, h),
                cornerRadius = CornerRadius(2f, 2f)
            )
        }
    }
}
