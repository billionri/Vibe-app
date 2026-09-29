package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.VibeTab
import com.example.ui.theme.VibeAccent
import com.example.ui.theme.VibeAccent2
import com.example.ui.theme.VibeBg
import com.example.ui.theme.VibeBorder
import com.example.ui.theme.VibeCard
import com.example.ui.theme.VibeGold
import com.example.ui.theme.VibeGoldBrush
import com.example.ui.theme.VibeLogoBrush
import com.example.ui.theme.VibeMuted
import com.example.ui.theme.VibeSurface
import com.example.ui.theme.VibeText

@Composable
fun VibeHeader(
    isTopAdVisible: Boolean,
    onCloseTopAd: () -> Unit,
    onOpenSpeakerOffer: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(VibeBg)
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .testTag("vibe_header")
    ) {
        // Logo & User Meta Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Stylized VIBE Logo
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "VIBE",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 2.sp,
                    color = VibeAccent,
                    modifier = Modifier.testTag("vibe_logo")
                )
                Box(
                    modifier = Modifier
                        .padding(start = 6.dp)
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(VibeAccent2)
                )
            }

            // User Info & Pro Badge
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = VibeSurface,
                    border = androidx.compose.foundation.BorderStroke(1.dp, VibeBorder)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .clip(CircleShape)
                                .background(VibeCard),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("🎵", fontSize = 12.sp)
                        }
                        Column {
                            Text(
                                text = "VIBE User",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = VibeText
                            )
                            Text(
                                text = "Personal Playlist",
                                fontSize = 9.sp,
                                color = VibeMuted
                            )
                        }
                    }
                }

                // PRO Badge
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = VibeGold.copy(alpha = 0.15f),
                    border = androidx.compose.foundation.BorderStroke(1.dp, VibeGold)
                ) {
                    Text(
                        text = "PRO",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Black,
                        color = VibeGold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }
        }

        // Top Google AdSense Banner (Matching site)
        AnimatedVisibility(
            visible = isTopAdVisible,
            exit = fadeOut() + shrinkVertically()
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp)
                    .testTag("top_ad_banner"),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = VibeSurface),
                border = androidx.compose.foundation.BorderStroke(1.dp, VibeBorder)
            ) {
                Column(modifier = Modifier.padding(10.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Surface(
                                shape = RoundedCornerShape(4.dp),
                                color = Color(0xFF252535)
                            ) {
                                Text(
                                    text = "ADVERTISEMENT",
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = VibeMuted,
                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                )
                            }
                            Text(
                                text = "ca-pub-8989568053300263",
                                fontSize = 9.sp,
                                color = VibeMuted
                            )
                        }

                        IconButton(
                            onClick = onCloseTopAd,
                            modifier = Modifier.size(20.dp).testTag("close_top_ad_btn")
                        ) {
                            Icon(Icons.Default.Close, contentDescription = "Close Ad", tint = VibeMuted, modifier = Modifier.size(14.dp))
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(VibeCard)
                            .clickable(onClick = onOpenSpeakerOffer)
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text("🔊", fontSize = 24.sp)
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "UBON 10W Bluetooth Speaker — ₹450 Only!",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = VibeGold,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = "Deep Bass · Wireless · Limited VIBE Flash Sale 55% OFF",
                                fontSize = 10.sp,
                                color = VibeText,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = VibeAccent
                        ) {
                            Text(
                                text = "Claim Deal",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun VibeTabBar(
    activeTab: VibeTab,
    onTabSelected: (VibeTab) -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(VibeBg)
            .horizontalScroll(scrollState)
            .padding(horizontal = 12.dp, vertical = 6.dp)
            .testTag("vibe_tab_bar"),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        VibeTab.values().forEach { tab ->
            val isSelected = tab == activeTab
            Surface(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .clickable { onTabSelected(tab) }
                    .testTag("tab_${tab.id}"),
                shape = RoundedCornerShape(12.dp),
                color = if (isSelected) VibeAccent.copy(alpha = 0.2f) else VibeSurface,
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    if (isSelected) VibeAccent else VibeBorder
                )
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(text = tab.emoji, fontSize = 15.sp)
                    Text(
                        text = tab.label,
                        fontSize = 13.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        color = if (isSelected) VibeAccent else VibeText
                    )
                }
            }
        }
    }
}
