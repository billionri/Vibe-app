package com.example.ui.tabs

import android.content.Intent
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.model.ReelItem
import com.example.ui.VibeAppViewModel
import com.example.ui.components.InFeedSponsoredAd
import com.example.ui.theme.VibeAccent
import com.example.ui.theme.VibeAccent2
import com.example.ui.theme.VibeBorder
import com.example.ui.theme.VibeCard
import com.example.ui.theme.VibeGold
import com.example.ui.theme.VibeMuted
import com.example.ui.theme.VibeSurface
import com.example.ui.theme.VibeText

@Composable
fun ReelsTab(
    viewModel: VibeAppViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val reels by viewModel.reels.collectAsStateWithLifecycle()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 14.dp)
            .testTag("reels_tab_content"),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Column {
                Text(
                    text = "📱 Music Shorts & Reels",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = VibeText
                )
                Text(
                    text = "Trending aesthetic audio clips & vertical soundscapes",
                    fontSize = 11.sp,
                    color = VibeMuted
                )
            }
        }

        // Sponsored Ad
        item {
            InFeedSponsoredAd(
                title = "DJI Osmo Pocket 3 Creator Combo",
                desc = "1-Inch CMOS 4K/120fps · 3-Axis Gimbal · Creator Special Offer",
                brand = "DJI India Partner",
                onActionClick = { viewModel.triggerToast("DJI voucher saved!") }
            )
        }

        items(reels, key = { it.id }) { reel ->
            var liked by remember { mutableStateOf(false) }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("reel_card_${reel.id}"),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = VibeSurface),
                border = androidx.compose.foundation.BorderStroke(1.dp, VibeBorder)
            ) {
                Column {
                    // Video mock preview
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(220.dp)
                            .background(
                                Brush.verticalGradient(
                                    listOf(
                                        Color(0xFF2C1030),
                                        Color(0xFF131024),
                                        Color(0xFF0C0916)
                                    )
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(reel.emoji, fontSize = 64.sp)

                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(Color.Black.copy(alpha = 0.5f))
                                .clickable {
                                    viewModel.triggerToast("Playing sound preview: ${reel.sound}")
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.PlayArrow, contentDescription = "Play", tint = Color.White, modifier = Modifier.size(28.dp))
                        }
                    }

                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = reel.title,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = VibeText
                        )
                        Spacer(modifier = Modifier.height(3.dp))
                        Text(
                            text = "${reel.creator} • 🎵 ${reel.sound}",
                            fontSize = 11.sp,
                            color = VibeAccent
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .clickable { liked = !liked }
                                    .padding(vertical = 4.dp, horizontal = 6.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Favorite,
                                    contentDescription = "Like",
                                    tint = if (liked) VibeAccent else VibeMuted,
                                    modifier = Modifier.size(16.dp)
                                )
                                Text(
                                    text = if (liked) "Liked" else reel.likes,
                                    fontSize = 11.sp,
                                    color = if (liked) VibeAccent else VibeMuted,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Button(
                                onClick = {
                                    val sendIntent = Intent().apply {
                                        action = Intent.ACTION_SEND
                                        putExtra(Intent.EXTRA_TEXT, "Check out this reel vibe: ${reel.title} (${reel.sound}) on VIBE!")
                                        type = "text/plain"
                                    }
                                    context.startActivity(Intent.createChooser(sendIntent, "Share Reel"))
                                },
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = VibeCard, contentColor = VibeText),
                                contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                            ) {
                                Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Share", fontSize = 11.sp)
                            }
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
