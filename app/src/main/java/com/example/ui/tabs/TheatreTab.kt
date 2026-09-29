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
import androidx.compose.material.icons.filled.Delete
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.VibeAppViewModel
import com.example.ui.components.InFeedSponsoredAd
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
fun TheatreTab(
    viewModel: VibeAppViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val plans by viewModel.theatrePlans.collectAsStateWithLifecycle()

    var movieTitle by remember { mutableStateOf("") }
    var dateTime by remember { mutableStateOf("Friday · 8:30 PM") }
    var snacks by remember { mutableStateOf("Gourmet Cheese Popcorn & Coke") }
    var friendsInput by remember { mutableStateOf("You, Rahul, Sneha, Rohan") }
    var costInput by remember { mutableStateOf("600") }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 14.dp)
            .testTag("theatre_tab_content"),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Column {
                Text(
                    text = "🎭 Movie Night — Plan & Share",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = VibeText
                )
                Text(
                    text = "Organize watch parties, split snack & ticket bills, and invite friends",
                    fontSize = 11.sp,
                    color = VibeMuted
                )
            }
        }

        // Create Plan Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = VibeSurface),
                border = androidx.compose.foundation.BorderStroke(1.dp, VibeBorder)
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "Create Watch Plan",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = VibeGold
                    )

                    OutlinedTextField(
                        value = movieTitle,
                        onValueChange = { movieTitle = it },
                        label = { Text("Movie / Series Title") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth().testTag("th_movie_input"),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = VibeAccent, unfocusedBorderColor = VibeBorder)
                    )

                    OutlinedTextField(
                        value = dateTime,
                        onValueChange = { dateTime = it },
                        label = { Text("Date & Time") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth().testTag("th_date_input"),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = VibeAccent, unfocusedBorderColor = VibeBorder)
                    )

                    OutlinedTextField(
                        value = snacks,
                        onValueChange = { snacks = it },
                        label = { Text("Snacks / Drinks") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = VibeAccent, unfocusedBorderColor = VibeBorder)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = friendsInput,
                            onValueChange = { friendsInput = it },
                            label = { Text("Friends (comma separated)") },
                            singleLine = true,
                            modifier = Modifier.weight(1.3f),
                            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = VibeAccent, unfocusedBorderColor = VibeBorder)
                        )

                        OutlinedTextField(
                            value = costInput,
                            onValueChange = { costInput = it.filter { c -> c.isDigit() } },
                            label = { Text("Cost (₹)") },
                            singleLine = true,
                            modifier = Modifier.weight(0.7f),
                            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = VibeAccent, unfocusedBorderColor = VibeBorder)
                        )
                    }

                    Button(
                        onClick = {
                            if (movieTitle.isNotBlank()) {
                                val friendsList = friendsInput.split(",").map { it.trim() }.filter { it.isNotBlank() }
                                val cost = costInput.toIntOrNull() ?: 500
                                viewModel.addTheatrePlan(movieTitle, dateTime, snacks, friendsList, cost)
                                movieTitle = ""
                            }
                        },
                        modifier = Modifier.fillMaxWidth().testTag("th_add_plan_btn"),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = VibeAccent, contentColor = Color.White)
                    ) {
                        Text("Add Plan & Split Bill", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Sponsored Ad
        item {
            InFeedSponsoredAd(
                title = "Swiggy Instamart — Late Night Snack Delivery",
                desc = "Popcorn, Nachos & Cold Drinks delivered in 10 mins. Use code VIBESNACK for ₹100 off!",
                brand = "Swiggy India Partner",
                onActionClick = { viewModel.triggerToast("Swiggy coupon VIBESNACK copied!") }
            )
        }

        // Watch Plans List Header
        item {
            Text(
                text = "Upcoming Movie Nights (${plans.size})",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = VibeText
            )
        }

        items(plans, key = { it.id }) { plan ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = VibeSurface),
                border = androidx.compose.foundation.BorderStroke(1.dp, VibeBorder)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = plan.movieTitle,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = VibeText
                        )
                        IconButton(
                            onClick = { viewModel.deleteTheatrePlan(plan) },
                            modifier = Modifier.size(24.dp)
                        ) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete", tint = VibeMuted, modifier = Modifier.size(16.dp))
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))
                    Text("⏰ ${plan.dateTime}", fontSize = 12.sp, color = VibeGold)
                    Text("🍿 ${plan.snacks}", fontSize = 12.sp, color = VibeMuted)

                    Spacer(modifier = Modifier.height(8.dp))
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = VibeCard
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text("👥 ${plan.friends.joinToString(", ")}", fontSize = 11.sp, color = VibeText, modifier = Modifier.weight(1f))
                            Text("₹${plan.costPerPerson} / person", fontSize = 12.sp, fontWeight = FontWeight.Black, color = VibeGreen)
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Button(
                        onClick = {
                            val shareMessage = "🍿 Hey! You're invited to Movie Night:\n" +
                                    "🎬 Film: ${plan.movieTitle}\n" +
                                    "⏰ When: ${plan.dateTime}\n" +
                                    "🥤 Snacks: ${plan.snacks}\n" +
                                    "💰 Cost split: ₹${plan.costPerPerson} per person\n" +
                                    "Planned via VIBE App!"
                            val sendIntent = Intent().apply {
                                action = Intent.ACTION_SEND
                                putExtra(Intent.EXTRA_TEXT, shareMessage)
                                type = "text/plain"
                            }
                            context.startActivity(Intent.createChooser(sendIntent, "Share Watch Party Plan"))
                        },
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF25D366), contentColor = Color.White),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Share Invite on WhatsApp", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}
