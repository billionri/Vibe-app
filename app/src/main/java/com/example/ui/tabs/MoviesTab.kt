package com.example.ui.tabs

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.model.StoreMovie
import com.example.ui.VibeAppViewModel
import com.example.ui.components.InFeedSponsoredAd
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
fun MoviesTab(
    viewModel: VibeAppViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val movies by viewModel.movies.collectAsStateWithLifecycle()
    val activeUniv by viewModel.activeUniverse.collectAsStateWithLifecycle()
    val searchQuery by viewModel.movieSearchQuery.collectAsStateWithLifecycle()

    var inputQuery by remember { mutableStateOf("") }

    val universeList = listOf(
        "ALL" to "🌟 All",
        "Marvel" to "⚡ Marvel",
        "DC" to "🦇 DC",
        "Anime" to "⚔️ Anime",
        "Bollywood" to "🔥 Bollywood",
        "Hollywood" to "🌌 Hollywood"
    )

    val filteredMovies = movies.filter { m ->
        val matchesUniv = activeUniv == "ALL" || m.collection.equals(activeUniv, ignoreCase = true)
        val matchesSearch = inputQuery.isBlank() ||
                m.title.contains(inputQuery, ignoreCase = true) ||
                m.genre.contains(inputQuery, ignoreCase = true) ||
                m.collection.contains(inputQuery, ignoreCase = true)
        matchesUniv && matchesSearch
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 14.dp)
            .testTag("movies_tab_content"),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Title & Actions row
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Movie Universe",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = VibeText
                    )
                    Text(
                        text = "Track watchlist, trailers, ratings & companion merch",
                        fontSize = 11.sp,
                        color = VibeMuted
                    )
                }

                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Button(
                        onClick = { viewModel.openCouponsModal() },
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = VibeGold, contentColor = Color.Black),
                        modifier = Modifier.testTag("get_coupons_btn")
                    ) {
                        Text("✨ Coupons", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = { viewModel.openTasteCard() },
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = VibeAccent, contentColor = Color.White),
                        modifier = Modifier.testTag("build_taste_btn")
                    ) {
                        Text("Taste Card", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Universe Filter Tabs
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                universeList.forEach { (key, label) ->
                    val isSelected = activeUniv == key
                    Surface(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .clickable { viewModel.setMovieUniverse(key) }
                            .testTag("univ_tab_$key"),
                        shape = RoundedCornerShape(12.dp),
                        color = if (isSelected) VibeAccent.copy(alpha = 0.25f) else VibeSurface,
                        border = androidx.compose.foundation.BorderStroke(1.dp, if (isSelected) VibeAccent else VibeBorder)
                    ) {
                        Text(
                            text = label,
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected) VibeAccent else VibeText,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp)
                        )
                    }
                }
            }
        }

        // Search Movie Input
        item {
            OutlinedTextField(
                value = inputQuery,
                onValueChange = { inputQuery = it },
                placeholder = { Text("Search title, superhero, director or genre...", color = VibeMuted, fontSize = 12.sp) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search", tint = VibeMuted) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth().testTag("movie_search_field"),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = VibeSurface,
                    unfocusedContainerColor = VibeSurface,
                    focusedBorderColor = VibeAccent,
                    unfocusedBorderColor = VibeBorder
                )
            )
        }

        // Sponsored in-feed ad
        item {
            InFeedSponsoredAd(
                title = "PVR Inox Movie Pass — Flat ₹99 Tuesdays",
                desc = "Unlimited movies every Tuesday across India. Promo code: VIBEMOVIE",
                brand = "PVR Cinemas Partner",
                onActionClick = { viewModel.triggerToast("PVR promo code VIBEMOVIE copied!") }
            )
        }

        // Movies List
        items(filteredMovies, key = { it.id }) { movie ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("movie_card_${movie.id}"),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = VibeSurface),
                border = androidx.compose.foundation.BorderStroke(1.dp, VibeBorder)
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(54.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(VibeCard),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(movie.emoji, fontSize = 26.sp)
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = movie.title,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = VibeText,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                                Icon(Icons.Default.Star, contentDescription = null, tint = VibeGold, modifier = Modifier.size(13.dp))
                                Text(movie.rating, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = VibeGold)
                            }
                        }

                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "${movie.genre} • ${movie.year} • ${movie.collection}",
                            fontSize = 11.sp,
                            color = VibeMuted
                        )

                        Spacer(modifier = Modifier.height(4.dp))
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Surface(
                                shape = RoundedCornerShape(4.dp),
                                color = VibeCard
                            ) {
                                Text(
                                    text = "🎁 ${movie.toy}",
                                    fontSize = 10.sp,
                                    color = VibeAccent,
                                    fontWeight = FontWeight.SemiBold,
                                    modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }

                    Column(horizontalAlignment = Alignment.End, verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        // Trailer Button
                        Surface(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .clickable {
                                    launchExternalLink(context, "https://www.youtube.com/watch?v=${movie.id}")
                                },
                            shape = RoundedCornerShape(8.dp),
                            color = VibeAccent.copy(alpha = 0.2f),
                            border = androidx.compose.foundation.BorderStroke(1.dp, VibeAccent)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(Icons.Default.PlayArrow, contentDescription = null, tint = VibeAccent, modifier = Modifier.size(12.dp))
                                Text("Trailer", fontSize = 10.sp, color = VibeAccent, fontWeight = FontWeight.Bold)
                            }
                        }

                        // Watched Status Toggle Button
                        Surface(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .clickable { viewModel.toggleMovieWatched(movie) }
                                .testTag("toggle_watched_${movie.id}"),
                            shape = RoundedCornerShape(8.dp),
                            color = if (movie.isWatched) VibeGreen.copy(alpha = 0.2f) else VibeCard,
                            border = androidx.compose.foundation.BorderStroke(1.dp, if (movie.isWatched) VibeGreen else VibeBorder)
                        ) {
                            Text(
                                text = if (movie.isWatched) "✓ Watched" else "+ Watchlist",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (movie.isWatched) VibeGreen else VibeMuted,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
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
