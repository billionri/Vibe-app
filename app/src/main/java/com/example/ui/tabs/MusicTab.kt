package com.example.ui.tabs

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.model.TrackItem
import com.example.ui.VibeAppViewModel
import com.example.ui.components.InFeedSponsoredAd
import com.example.ui.components.VideoPlayerView
import com.example.ui.theme.VibeAccent
import com.example.ui.theme.VibeAccent2
import com.example.ui.theme.VibeBorder
import com.example.ui.theme.VibeCard
import com.example.ui.theme.VibeGold
import com.example.ui.theme.VibeGreen
import com.example.ui.theme.VibeMoodLabels
import com.example.ui.theme.VibeMuted
import com.example.ui.theme.VibeSurface
import com.example.ui.theme.VibeText

@Composable
fun MusicTab(
    viewModel: VibeAppViewModel,
    modifier: Modifier = Modifier
) {
    val tracks by viewModel.tracks.collectAsStateWithLifecycle()
    val currentTrack by viewModel.currentTrack.collectAsStateWithLifecycle()
    val isPlaying by viewModel.isPlaying.collectAsStateWithLifecycle()
    val activeLabel by viewModel.activeLabelFilter.collectAsStateWithLifecycle()
    val searchQuery by viewModel.trackSearchQuery.collectAsStateWithLifecycle()

    var inputSearchText by remember { mutableStateOf("") }

    val filteredTracks = tracks.filter { track ->
        val matchesQuery = inputSearchText.isBlank() ||
                track.title.contains(inputSearchText, ignoreCase = true) ||
                track.channel.contains(inputSearchText, ignoreCase = true)
        val matchesLabel = activeLabel == null || track.labels.any { it.equals(activeLabel, ignoreCase = true) }
        matchesQuery && matchesLabel
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 14.dp)
            .testTag("music_tab_content"),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Player Video Wrap / Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("player_wrap_card"),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = VibeSurface),
                border = androidx.compose.foundation.BorderStroke(1.dp, VibeBorder)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .background(
                            Brush.verticalGradient(
                                listOf(
                                    Color(0xFF1B1B2A),
                                    Color(0xFF101018)
                                )
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .clip(CircleShape)
                                .background(VibeAccent.copy(alpha = 0.2f))
                                .clickable { viewModel.togglePlay() },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(if (isPlaying) "⏸" else "▶", fontSize = 24.sp, color = VibeAccent)
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = currentTrack?.title ?: "NOTHING PLAYING",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = VibeText,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )

                        Text(
                            text = if (isPlaying) "Streaming High-Fidelity Audio • ${currentTrack?.channel ?: "VIBE"}" else "Select a song below or tap to play",
                            fontSize = 11.sp,
                            color = if (isPlaying) VibeGreen else VibeMuted
                        )
                    }
                }
            }
        }

        // Action Buttons Row: "📤 Share with GF / Bestie"
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = {
                        currentTrack?.let { viewModel.openShareTrack(it) }
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = VibeAccent, contentColor = Color.White),
                    modifier = Modifier.testTag("share_track_gf_btn")
                ) {
                    Text("📤 Share with GF / Bestie", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }

                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = VibeCard,
                    border = androidx.compose.foundation.BorderStroke(1.dp, VibeBorder)
                ) {
                    Text(
                        text = "${filteredTracks.size} tracks",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = VibeMuted,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                    )
                }
            }
        }

        // Search Input Row
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = inputSearchText,
                    onValueChange = { inputSearchText = it },
                    placeholder = { Text("Search songs, artists, or paste YouTube link...", color = VibeMuted, fontSize = 12.sp) },
                    singleLine = true,
                    modifier = Modifier.weight(1f).testTag("track_search_field"),
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
                        if (inputSearchText.isNotBlank()) {
                            viewModel.addTrackFromSearch(inputSearchText)
                            inputSearchText = ""
                        }
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = VibeAccent2, contentColor = Color.White),
                    modifier = Modifier.testTag("track_search_btn")
                ) {
                    Text("+ Add", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        // Vibe Mood Label Pills
        item {
            Text("Mood & Vibe Labels:", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = VibeMuted)
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                VibeMoodLabels.forEach { labelData ->
                    val name = labelData.first.first
                    val emoji = labelData.first.second
                    val color = labelData.second
                    val isSelected = activeLabel == name

                    Surface(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .clickable { viewModel.setLabelFilter(name) }
                            .testTag("mood_label_$name"),
                        shape = RoundedCornerShape(12.dp),
                        color = if (isSelected) color.copy(alpha = 0.25f) else VibeSurface,
                        border = androidx.compose.foundation.BorderStroke(1.dp, if (isSelected) color else VibeBorder)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(emoji, fontSize = 13.sp)
                            Text(
                                text = name,
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) color else VibeText
                            )
                        }
                    }
                }
            }
        }

        // Sponsored in-feed ad
        item {
            InFeedSponsoredAd(
                title = "Spotify Premium — 3 Months Free",
                desc = "Download songs, play offline without ad interruptions. Exclusive code: VIBEPREM",
                brand = "Spotify India",
                onActionClick = { viewModel.triggerToast("Spotify Promo code VIBEPREM copied!") }
            )
        }

        // Playlist Tracks Header
        item {
            Text(
                text = "My Playlist Queue",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = VibeText
            )
        }

        // Tracks Items
        items(filteredTracks, key = { it.id }) { track ->
            val isCurrent = currentTrack?.id == track.id
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { viewModel.playTrack(track) }
                    .testTag("track_row_${track.id}"),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isCurrent) VibeAccent.copy(alpha = 0.12f) else VibeSurface
                ),
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    if (isCurrent) VibeAccent else VibeBorder
                )
            ) {
                Row(
                    modifier = Modifier.padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isCurrent) VibeAccent else VibeCard),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (isCurrent && isPlaying) "▶" else "🎵",
                            fontSize = 16.sp,
                            color = if (isCurrent) Color.White else VibeMuted
                        )
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = track.title,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isCurrent) VibeAccent else VibeText,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text(track.channel, fontSize = 11.sp, color = VibeMuted, maxLines = 1)
                            if (track.labels.isNotEmpty()) {
                                Text("• ${track.labels.joinToString(", ")}", fontSize = 10.sp, color = VibeGold)
                            }
                        }
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(
                            onClick = { viewModel.openShareTrack(track) },
                            modifier = Modifier.size(30.dp)
                        ) {
                            Icon(Icons.Default.Share, contentDescription = "Share", tint = VibeMuted, modifier = Modifier.size(15.dp))
                        }
                        IconButton(
                            onClick = { viewModel.deleteTrack(track) },
                            modifier = Modifier.size(30.dp)
                        ) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete", tint = VibeMuted, modifier = Modifier.size(15.dp))
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
