package com.example.ui.tabs

import androidx.compose.animation.core.LinearEasing
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.model.MerchProduct
import com.example.ui.VibeAppViewModel
import com.example.ui.components.InFeedSponsoredAd
import com.example.ui.theme.VibeAccent
import com.example.ui.theme.VibeBorder
import com.example.ui.theme.VibeCard
import com.example.ui.theme.VibeGold
import com.example.ui.theme.VibeGreen
import com.example.ui.theme.VibeMuted
import com.example.ui.theme.VibeSurface
import com.example.ui.theme.VibeText

@Composable
fun VinylTab(
    viewModel: VibeAppViewModel,
    modifier: Modifier = Modifier
) {
    val vinyls by viewModel.vinylRecords.collectAsStateWithLifecycle()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 14.dp)
            .testTag("vinyl_tab_content"),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Column {
                Text(
                    text = "🛍️ Vinyl & Music Store",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = VibeText
                )
                Text(
                    text = "Collector 180g heavyweight vinyl pressings & master recordings",
                    fontSize = 11.sp,
                    color = VibeMuted
                )
            }
        }

        // Sponsored Ad
        item {
            InFeedSponsoredAd(
                title = "Audio-Technica AT-LP60X Turntable",
                desc = "Fully automatic belt-drive turntable · High fidelity analog sound · Flat ₹1500 off",
                brand = "Audio-Technica Partner",
                onActionClick = { viewModel.triggerToast("Turntable discount code applied!") }
            )
        }

        items(vinyls, key = { it.id }) { record ->
            Card(
                modifier = Modifier.fillMaxWidth().testTag("vinyl_card_${record.id}"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = VibeSurface),
                border = androidx.compose.foundation.BorderStroke(1.dp, VibeBorder)
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // Rotating Vinyl Disc
                    SpinningVinylDisc(emoji = record.emoji)

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = record.album,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = VibeText,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = "${record.artist} • ${record.year}",
                            fontSize = 12.sp,
                            color = VibeAccent
                        )
                        Text(
                            text = record.genre,
                            fontSize = 10.sp,
                            color = VibeMuted
                        )

                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "₹${record.price}",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Black,
                            color = VibeGold
                        )
                    }

                    Button(
                        onClick = {
                            viewModel.openCheckout(
                                MerchProduct(
                                    id = record.id,
                                    name = "${record.album} (180g Vinyl)",
                                    desc = "${record.artist} · Heavyweight Collector Pressing",
                                    price = record.price,
                                    originalPrice = record.price + 500,
                                    emoji = "💿",
                                    category = "vinyl"
                                )
                            )
                        },
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = VibeAccent, contentColor = Color.White),
                        modifier = Modifier.testTag("buy_vinyl_${record.id}")
                    ) {
                        Text("Buy", fontWeight = FontWeight.Bold)
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
fun SpinningVinylDisc(emoji: String, modifier: Modifier = Modifier) {
    val transition = rememberInfiniteTransition(label = "disc_spin")
    val angle by transition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(tween(8000, easing = LinearEasing), RepeatMode.Restart),
        label = "spin_angle"
    )

    Box(
        modifier = modifier
            .size(60.dp)
            .rotate(angle),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            // Vinyl grooves
            drawCircle(color = Color(0xFF14141A))
            drawCircle(color = Color(0xFF22222E), style = Stroke(width = 1.5.dp.toPx()))
            drawCircle(color = Color(0xFF2A2A3A), radius = size.minDimension / 3f, style = Stroke(width = 1.dp.toPx()))
            drawCircle(color = Color(0xFFFF3C6E), radius = size.minDimension / 4.5f)
            drawCircle(color = Color.Black, radius = size.minDimension / 10f)
        }
        Text(emoji, fontSize = 14.sp)
    }
}
