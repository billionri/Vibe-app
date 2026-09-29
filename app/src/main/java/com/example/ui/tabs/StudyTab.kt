package com.example.ui.tabs

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.VibeAppViewModel
import com.example.ui.components.InFeedSponsoredAd
import com.example.util.formatDuration
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
fun StudyTab(
    viewModel: VibeAppViewModel,
    modifier: Modifier = Modifier
) {
    val isTimerRunning by viewModel.isStudyTimerRunning.collectAsStateWithLifecycle()
    val secondsRemaining by viewModel.studySecondsRemaining.collectAsStateWithLifecycle()
    val totalMins by viewModel.totalStudyMinutes.collectAsStateWithLifecycle()

    var selectedMood by remember { mutableStateOf("Rainy Tokyo") }
    val studyAtmospheres = listOf(
        "Rainy Tokyo" to "🌧️",
        "Midnight Library" to "📖",
        "Cozy Coffee Shop" to "☕",
        "Deep Cosmos" to "🌌"
    )

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 14.dp)
            .testTag("study_tab_content"),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Column {
                Text(
                    text = "📚 Lo-Fi Study Room",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = VibeText
                )
                Text(
                    text = "Binaural beats, ambient focus soundscapes & Pomodoro timer",
                    fontSize = 11.sp,
                    color = VibeMuted
                )
            }
        }

        // Pomodoro Timer Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = VibeSurface),
                border = androidx.compose.foundation.BorderStroke(1.dp, VibeBorder)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = if (isTimerRunning) "FOCUS SESSION ACTIVE" else "25-MINUTE POMODORO",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isTimerRunning) VibeGreen else VibeGold
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Circular Progress & Clock
                    Box(contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(
                            progress = { secondsRemaining / (25f * 60f) },
                            modifier = Modifier.size(160.dp),
                            color = if (isTimerRunning) VibeGreen else VibeAccent,
                            trackColor = VibeBorder,
                            strokeWidth = 8.dp
                        )

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = formatDuration(secondsRemaining),
                                fontSize = 34.sp,
                                fontWeight = FontWeight.Black,
                                color = VibeText
                            )
                            Text(
                                text = "Atmosphere: $selectedMood",
                                fontSize = 10.sp,
                                color = VibeMuted
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Button(
                            onClick = { viewModel.toggleStudyTimer() },
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isTimerRunning) Color(0xFFEF4444) else VibeGreen,
                                contentColor = Color.White
                            ),
                            modifier = Modifier.testTag("study_timer_toggle")
                        ) {
                            Icon(
                                imageVector = if (isTimerRunning) Icons.Default.Pause else Icons.Default.PlayArrow,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.size(6.dp))
                            Text(if (isTimerRunning) "Pause Focus" else "Start Focus", fontWeight = FontWeight.Bold)
                        }

                        IconButton(
                            onClick = { viewModel.resetStudyTimer() },
                            modifier = Modifier
                                .size(40.dp)
                                .background(VibeCard, CircleShape)
                        ) {
                            Icon(Icons.Default.Refresh, contentDescription = "Reset", tint = VibeMuted)
                        }
                    }
                }
            }
        }

        // Atmosphere Selector
        item {
            Text("Select Study Atmosphere:", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = VibeMuted)
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                studyAtmospheres.forEach { (name, emoji) ->
                    val isSelected = selectedMood == name
                    Surface(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .clickable {
                                selectedMood = name
                                viewModel.triggerToast("Switched background to $name $emoji")
                            },
                        shape = RoundedCornerShape(12.dp),
                        color = if (isSelected) VibeAccent.copy(alpha = 0.2f) else VibeSurface,
                        border = androidx.compose.foundation.BorderStroke(1.dp, if (isSelected) VibeAccent else VibeBorder)
                    ) {
                        Column(
                            modifier = Modifier.padding(vertical = 10.dp, horizontal = 4.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(emoji, fontSize = 20.sp)
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(name, fontSize = 10.sp, color = if (isSelected) VibeAccent else VibeText, fontWeight = FontWeight.Bold, maxLines = 1)
                        }
                    }
                }
            }
        }

        // Focus Stats Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = VibeSurface),
                border = androidx.compose.foundation.BorderStroke(1.dp, VibeBorder)
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Lifetime Focus Time", fontSize = 11.sp, color = VibeMuted)
                        Text("$totalMins Minutes", fontSize = 22.sp, fontWeight = FontWeight.Black, color = VibeGold)
                    }
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = VibeGreen.copy(alpha = 0.15f)
                    ) {
                        Text(
                            text = "🔥 Streak: 4 Days",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = VibeGreen,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }
        }

        // Sponsored Ad
        item {
            InFeedSponsoredAd(
                title = "Notion — Best Student & Creator Workspace",
                desc = "Notes, tasks & wikis all in one app. Free Notion Plus trial with student ID.",
                brand = "Notion HQ Partner",
                onActionClick = { viewModel.triggerToast("Notion promotion applied!") }
            )
        }

        item {
            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}
