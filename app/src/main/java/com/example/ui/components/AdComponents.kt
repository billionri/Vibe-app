package com.example.ui.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.AlertDialog
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
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.VibeDataDefaults
import com.example.model.Coupon
import com.example.model.MerchProduct
import com.example.model.TrackItem
import com.example.ui.theme.VibeAccent
import com.example.ui.theme.VibeAccent2
import com.example.ui.theme.VibeBorder
import com.example.ui.theme.VibeCard
import com.example.ui.theme.VibeGold
import com.example.ui.theme.VibeGoldBrush
import com.example.ui.theme.VibeGreen
import com.example.ui.theme.VibeMuted
import com.example.ui.theme.VibeSurface
import com.example.ui.theme.VibeText

@Composable
fun InFeedSponsoredAd(
    title: String = "boAt Rockerz 450 Pro — Flat 60% Off",
    desc: String = "70 Hours Battery · ASAP Charge · 40mm Drivers · Best Bluetooth Headphones for Music",
    brand: String = "boAt Lifestyle",
    onActionClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("in_feed_ad_card"),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = VibeSurface),
        border = androidx.compose.foundation.BorderStroke(1.dp, VibeBorder)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = Color(0xFF28283C)
                ) {
                    Text(
                        text = "SPONSORED",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = VibeGold,
                        modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                    )
                }
                Text(
                    text = brand,
                    fontSize = 10.sp,
                    color = VibeMuted
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(VibeCard),
                    contentAlignment = Alignment.Center
                ) {
                    Text("🎧", fontSize = 22.sp)
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = title,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = VibeText,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = desc,
                        fontSize = 10.sp,
                        color = VibeMuted,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Button(
                    onClick = onActionClick,
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = VibeAccent, contentColor = Color.White),
                    modifier = Modifier.testTag("claim_ad_btn")
                ) {
                    Text("Claim", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun SpeakerOfferModal(
    onDismiss: () -> Unit,
    onOrderGPay: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = VibeSurface,
        title = null,
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(VibeCard),
                    contentAlignment = Alignment.Center
                ) {
                    Text("🔊", fontSize = 54.sp)
                }

                Spacer(modifier = Modifier.height(14.dp))

                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = VibeGold.copy(alpha = 0.2f),
                    border = androidx.compose.foundation.BorderStroke(1.dp, VibeGold)
                ) {
                    Text(
                        text = "⚡ EXCLUSIVE 55% OFF DEAL",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = VibeGold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "UBON 10W Bluetooth Speaker",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = VibeText
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Where Style Meets Sound · Deep Bass · Wireless · Carry Strap · Perfect for parties & travel",
                    fontSize = 12.sp,
                    color = VibeMuted,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "₹450",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Black,
                        color = VibeGreen
                    )
                    Text(
                        text = "₹999",
                        fontSize = 15.sp,
                        color = VibeMuted,
                        textDecoration = TextDecoration.LineThrough
                    )
                    Text(
                        text = "FREE Delivery",
                        fontSize = 11.sp,
                        color = VibeGold,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onOrderGPay,
                modifier = Modifier.fillMaxWidth().testTag("order_speaker_btn"),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = VibeGreen, contentColor = Color.White)
            ) {
                Text("🛒 Order ₹450 via GPay", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Maybe Later", color = VibeMuted)
            }
        }
    )
}

@Composable
fun TshirtOfferModal(
    onDismiss: () -> Unit,
    onOrderGPay: () -> Unit
) {
    var selectedSize by remember { mutableStateOf("L") }
    val sizes = listOf("S", "M", "L", "XL", "XXL")

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = VibeSurface,
        title = null,
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(VibeCard),
                    contentAlignment = Alignment.Center
                ) {
                    Text("👕", fontSize = 54.sp)
                }

                Spacer(modifier = Modifier.height(14.dp))

                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = VibeAccent.copy(alpha = 0.2f),
                    border = androidx.compose.foundation.BorderStroke(1.dp, VibeAccent)
                ) {
                    Text(
                        text = "LIMITED EDITION MERCH",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = VibeAccent,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "VIBE Quote Oversized Tee",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = VibeText
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "\"Money follows maah brothaaa\" · 240 GSM Pure Bio-Washed Cotton · Unisex Streetwear Fit",
                    fontSize = 12.sp,
                    color = VibeMuted,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text("Select Size:", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = VibeText)
                Spacer(modifier = Modifier.height(6.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    sizes.forEach { size ->
                        val isSelected = size == selectedSize
                        Surface(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .clickable { selectedSize = size },
                            shape = RoundedCornerShape(8.dp),
                            color = if (isSelected) VibeAccent else VibeCard,
                            border = androidx.compose.foundation.BorderStroke(1.dp, if (isSelected) VibeAccent else VibeBorder)
                        ) {
                            Text(
                                text = size,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isSelected) Color.White else VibeText,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "₹500",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Black,
                        color = VibeGreen
                    )
                    Text(
                        text = "₹999",
                        fontSize = 15.sp,
                        color = VibeMuted,
                        textDecoration = TextDecoration.LineThrough
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onOrderGPay,
                modifier = Modifier.fillMaxWidth().testTag("order_tshirt_btn"),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = VibeGreen, contentColor = Color.White)
            ) {
                Text("🛒 Order Size $selectedSize (₹500) via GPay", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Skip", color = VibeMuted)
            }
        }
    )
}

@Composable
fun GPayCheckoutModal(
    product: MerchProduct,
    onDismiss: () -> Unit,
    onConfirm: (MerchProduct) -> Unit
) {
    var name by remember { mutableStateOf("VIBE Member") }
    var phone by remember { mutableStateOf("9876543210") }
    var address by remember { mutableStateOf("Flat 402, Skyline Residency, Mumbai") }
    var upiId by remember { mutableStateOf("vibe.user@okhdfcbank") }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = VibeSurface,
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Instant GPay / UPI Checkout", color = VibeText, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                IconButton(onClick = onDismiss, modifier = Modifier.size(24.dp)) {
                    Icon(Icons.Default.Close, contentDescription = "Close", tint = VibeMuted)
                }
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Item Preview
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = VibeCard,
                    border = androidx.compose.foundation.BorderStroke(1.dp, VibeBorder)
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(product.emoji, fontSize = 28.sp)
                        Column(modifier = Modifier.weight(1f)) {
                            Text(product.name, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = VibeText)
                            Text("Total: ₹${product.price} (Inclusive of all taxes)", fontSize = 11.sp, color = VibeGreen)
                        }
                    }
                }

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Receiver Full Name") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = VibeAccent, unfocusedBorderColor = VibeBorder)
                )

                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text("Mobile Number (For Delivery Updates)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = VibeAccent, unfocusedBorderColor = VibeBorder)
                )

                OutlinedTextField(
                    value = address,
                    onValueChange = { address = it },
                    label = { Text("Delivery Address & Pincode") },
                    maxLines = 2,
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = VibeAccent, unfocusedBorderColor = VibeBorder)
                )

                OutlinedTextField(
                    value = upiId,
                    onValueChange = { upiId = it },
                    label = { Text("Google Pay UPI ID") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = VibeAccent, unfocusedBorderColor = VibeBorder)
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onConfirm(product) },
                modifier = Modifier.fillMaxWidth().testTag("confirm_gpay_pay_btn"),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1A73E8), contentColor = Color.White)
            ) {
                Text("⚡ Pay ₹${product.price} via Google Pay", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = null
    )
}

@Composable
fun CouponsModal(
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val coupons = VibeDataDefaults.COUPONS

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = VibeSurface,
        title = {
            Text("✨ VIBE Promo Coupons", color = VibeText, fontWeight = FontWeight.Bold)
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text("Copy code and apply at checkout or theatre watch party:", fontSize = 11.sp, color = VibeMuted)
                coupons.forEach { c ->
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        color = VibeCard,
                        border = androidx.compose.foundation.BorderStroke(1.dp, VibeBorder)
                    ) {
                        Row(
                            modifier = Modifier.padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(c.code, fontSize = 14.sp, fontWeight = FontWeight.Black, color = VibeGold)
                                Text(c.discount, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = VibeText)
                                Text(c.desc, fontSize = 10.sp, color = VibeMuted)
                            }
                            IconButton(
                                onClick = {
                                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                    clipboard.setPrimaryClip(ClipData.newPlainText("coupon", c.code))
                                    Toast.makeText(context, "Copied ${c.code}!", Toast.LENGTH_SHORT).show()
                                }
                            ) {
                                Icon(Icons.Default.ContentCopy, contentDescription = "Copy", tint = VibeAccent)
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = VibeAccent, contentColor = Color.White)
            ) {
                Text("Done")
            }
        }
    )
}

@Composable
fun ShareTrackModal(
    track: TrackItem,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = VibeSurface,
        title = {
            Text("Share Track 💌", color = VibeText, fontWeight = FontWeight.Bold)
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Send this vibe link to your GF / Bestie", fontSize = 12.sp, color = VibeMuted)
                Spacer(modifier = Modifier.height(14.dp))

                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = VibeCard,
                    border = androidx.compose.foundation.BorderStroke(1.dp, VibeAccent.copy(alpha = 0.5f))
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("🎵", fontSize = 36.sp)
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(track.title, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = VibeText)
                        Text(track.channel, fontSize = 11.sp, color = VibeMuted)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("via VIBE Music & Movies", fontSize = 9.sp, color = VibeAccent, fontWeight = FontWeight.Bold)
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val shareText = "🎧 Hey! Listen to this track with me: ${track.title} by ${track.channel} on VIBE: ${track.originalUrl}"
                    val sendIntent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_TEXT, shareText)
                        type = "text/plain"
                    }
                    context.startActivity(Intent.createChooser(sendIntent, "Share with GF / Bestie"))
                    onDismiss()
                },
                colors = ButtonDefaults.buttonColors(containerColor = VibeAccent, contentColor = Color.White)
            ) {
                Text("📤 Share via WhatsApp / SMS")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Close", color = VibeMuted)
            }
        }
    )
}
