package com.example.ui.tabs

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.VolumeDown
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.audio.AmbientSoundType
import com.example.model.DailyGoal
import com.example.model.DailyHabit
import com.example.model.DayPlannerBlock
import com.example.model.DayTask
import com.example.model.TaskCategory
import com.example.model.TaskPriority
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
import com.example.util.formatDuration

enum class FocusSubSection(val label: String, val emoji: String) {
    TIMER("Focus Timer", "🎯"),
    TASKS("Day Tasks", "📝"),
    PLANNER("Day Planner", "🗓️"),
    HABITS("Habits & Stats", "📊")
}

@Composable
fun StudyTab(
    viewModel: VibeAppViewModel,
    modifier: Modifier = Modifier
) {
    var activeSubSection by remember { mutableStateOf(FocusSubSection.TIMER) }

    val isTimerRunning by viewModel.isStudyTimerRunning.collectAsStateWithLifecycle()
    val secondsRemaining by viewModel.studySecondsRemaining.collectAsStateWithLifecycle()
    val totalMins by viewModel.totalStudyMinutes.collectAsStateWithLifecycle()
    val activeTask by viewModel.activeFocusTask.collectAsStateWithLifecycle()
    val presetMins by viewModel.focusPresetMinutes.collectAsStateWithLifecycle()

    val tasks by viewModel.tasks.collectAsStateWithLifecycle()
    val selectedCategory by viewModel.selectedTaskFilterCategory.collectAsStateWithLifecycle()
    val plannerBlocks by viewModel.plannerBlocks.collectAsStateWithLifecycle()
    val dailyGoals by viewModel.dailyGoals.collectAsStateWithLifecycle()
    val dailyHabits by viewModel.dailyHabits.collectAsStateWithLifecycle()

    val isAmbientPlaying by viewModel.isAmbientSoundPlaying.collectAsStateWithLifecycle()
    val ambientVolume by viewModel.ambientVolume.collectAsStateWithLifecycle()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 14.dp)
            .testTag("focus_tab_content"),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Header
        item {
            Column(modifier = Modifier.padding(top = 4.dp)) {
                Text(
                    text = "🎯 Focus, Tasks & Day Planner",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = VibeText
                )
                Text(
                    text = "Supercharge day-to-day tasks, time-blocked planning, habits & focus sprints",
                    fontSize = 11.sp,
                    color = VibeMuted
                )
            }
        }

        // Sub-Navigation Segmented Bar
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(VibeSurface)
                    .border(1.dp, VibeBorder, RoundedCornerShape(12.dp))
                    .padding(4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                FocusSubSection.values().forEach { sub ->
                    val isSelected = sub == activeSubSection
                    Surface(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .clickable { activeSubSection = sub }
                            .testTag("focus_sub_${sub.name.lowercase()}"),
                        shape = RoundedCornerShape(8.dp),
                        color = if (isSelected) VibeAccent else Color.Transparent
                    ) {
                        Column(
                            modifier = Modifier.padding(vertical = 8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(sub.emoji, fontSize = 14.sp)
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = sub.label,
                                fontSize = 10.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) Color.White else VibeMuted,
                                maxLines = 1
                            )
                        }
                    }
                }
            }
        }

        // Section Content
        when (activeSubSection) {
            FocusSubSection.TIMER -> {
                // Focus Room: Timer + Active Task Banner + Presets + Ambient Audio
                item {
                    FocusTimerSection(
                        viewModel = viewModel,
                        isTimerRunning = isTimerRunning,
                        secondsRemaining = secondsRemaining,
                        presetMins = presetMins,
                        activeTask = activeTask,
                        isAmbientPlaying = isAmbientPlaying,
                        ambientVolume = ambientVolume,
                        onSwitchToTasks = { activeSubSection = FocusSubSection.TASKS }
                    )
                }
            }
            FocusSubSection.TASKS -> {
                // Day-to-Day Tasks Manager
                item {
                    DayTasksSection(
                        tasks = tasks,
                        selectedCategory = selectedCategory,
                        activeTask = activeTask,
                        onCategorySelect = { viewModel.filterTasks(it) },
                        onToggleTask = { viewModel.toggleTask(it) },
                        onDeleteTask = { viewModel.deleteTask(it) },
                        onStartFocusOnTask = { task, mins ->
                            viewModel.startFocusForTask(task, mins)
                            activeSubSection = FocusSubSection.TIMER
                        },
                        onAddTask = { title, cat, prio, time, mins, notes ->
                            viewModel.addNewTask(title, cat, prio, time, mins, notes)
                        }
                    )
                }
            }
            FocusSubSection.PLANNER -> {
                // Day Planner & Time-Blocking
                item {
                    DayPlannerSection(
                        plannerBlocks = plannerBlocks,
                        dailyGoals = dailyGoals,
                        onToggleBlock = { viewModel.togglePlannerBlock(it) },
                        onAddBlock = { period, title, desc, emoji ->
                            viewModel.addNewPlannerBlock(period, title, desc, emoji)
                        },
                        onDeleteBlock = { viewModel.deletePlannerBlock(it) },
                        onToggleGoal = { viewModel.toggleGoal(it) },
                        onAddGoal = { viewModel.addNewGoal(it) },
                        onDeleteGoal = { viewModel.deleteGoal(it) }
                    )
                }
            }
            FocusSubSection.HABITS -> {
                // Habits & Productivity Stats
                item {
                    HabitsAndStatsSection(
                        totalMins = totalMins,
                        tasks = tasks,
                        dailyHabits = dailyHabits,
                        onToggleHabit = { viewModel.toggleHabit(it) },
                        onAddHabit = { title, emoji -> viewModel.addNewHabit(title, emoji) }
                    )
                }
            }
        }

        // Sponsored Partner Ad
        item {
            InFeedSponsoredAd(
                title = "Notion — Best Day Planner & Task Workspace",
                desc = "Daily schedules, habit tracking, and project wikis. Free Notion Plus with education & creator invite.",
                brand = "Notion Productivity Partner",
                onActionClick = { viewModel.triggerToast("Notion productivity workspace promo applied!") }
            )
        }

        item {
            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}

// -------------------------------------------------------------
// SECTION 1: FOCUS TIMER COMPONENT
// -------------------------------------------------------------
@Composable
private fun FocusTimerSection(
    viewModel: VibeAppViewModel,
    isTimerRunning: Boolean,
    secondsRemaining: Int,
    presetMins: Int,
    activeTask: DayTask?,
    isAmbientPlaying: Boolean,
    ambientVolume: Float,
    onSwitchToTasks: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
        // Active Focus Task Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = VibeSurface),
            border = androidx.compose.foundation.BorderStroke(1.dp, if (activeTask != null) VibeAccent.copy(alpha = 0.5f) else VibeBorder)
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "CURRENT FOCUS TARGET",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (activeTask != null) VibeAccent else VibeMuted
                    )
                    TextButton(onClick = onSwitchToTasks) {
                        Text(if (activeTask != null) "Change Task" else "Select Task", fontSize = 11.sp, color = VibeAccent)
                    }
                }

                if (activeTask != null) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(activeTask.category.emoji, fontSize = 24.sp)
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = activeTask.title,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = VibeText,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(activeTask.category.displayName, fontSize = 10.sp, color = VibeMuted)
                                Text("•", fontSize = 10.sp, color = VibeMuted)
                                Text("${activeTask.completedMinutes}/${activeTask.estimatedMinutes}m logged", fontSize = 10.sp, color = VibeGold)
                            }
                        }

                        if (!activeTask.isCompleted) {
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = VibeGreen.copy(alpha = 0.15f),
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .clickable { viewModel.toggleTask(activeTask.id) }
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Icon(Icons.Default.Check, contentDescription = "Done", tint = VibeGreen, modifier = Modifier.size(12.dp))
                                    Text("Done", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = VibeGreen)
                                }
                            }
                        }
                    }
                } else {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .clickable(onClick = onSwitchToTasks)
                            .background(VibeCard)
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text("🎯", fontSize = 20.sp)
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Free Focus Sprint", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = VibeText)
                            Text("Link a day-to-day task to track focus progress", fontSize = 10.sp, color = VibeMuted)
                        }
                        Text("Link ➔", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = VibeAccent)
                    }
                }
            }
        }

        // Pomodoro / Sprint Clock Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = VibeSurface),
            border = androidx.compose.foundation.BorderStroke(1.dp, VibeBorder)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Preset Duration Chips
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    listOf(
                        15 to "15m Sprint",
                        25 to "25m Pomo",
                        50 to "50m Deep",
                        90 to "90m Flow"
                    ).forEach { (mins, label) ->
                        val isSelected = presetMins == mins
                        Surface(
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .clickable { viewModel.setFocusPreset(mins) },
                            shape = RoundedCornerShape(10.dp),
                            color = if (isSelected) VibeAccent.copy(alpha = 0.25f) else VibeCard,
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                if (isSelected) VibeAccent else Color.Transparent
                            )
                        ) {
                            Text(
                                text = label,
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) VibeAccent else VibeMuted,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Circular Progress Indicator & Time
                val totalSeconds = (presetMins * 60).coerceAtLeast(1)
                Box(contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(
                        progress = { secondsRemaining.toFloat() / totalSeconds.toFloat() },
                        modifier = Modifier.size(170.dp),
                        color = if (isTimerRunning) VibeGreen else VibeAccent,
                        trackColor = VibeBorder,
                        strokeWidth = 9.dp
                    )

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = formatDuration(secondsRemaining),
                            fontSize = 38.sp,
                            fontWeight = FontWeight.Black,
                            color = VibeText
                        )
                        Text(
                            text = if (isTimerRunning) "SESSION IN PROGRESS" else "READY TO FOCUS",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isTimerRunning) VibeGreen else VibeGold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Timer Controls
                Row(
                    horizontalArrangement = Arrangement.spacedBy(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = { viewModel.toggleStudyTimer() },
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isTimerRunning) Color(0xFFEF4444) else VibeGreen,
                            contentColor = Color.White
                        ),
                        modifier = Modifier
                            .height(48.dp)
                            .testTag("study_timer_toggle")
                    ) {
                        Icon(
                            imageVector = if (isTimerRunning) Icons.Default.Pause else Icons.Default.PlayArrow,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.size(8.dp))
                        Text(
                            text = if (isTimerRunning) "Pause Focus" else "Start Focus Sprint",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    IconButton(
                        onClick = { viewModel.resetStudyTimer() },
                        modifier = Modifier
                            .size(46.dp)
                            .background(VibeCard, CircleShape)
                    ) {
                        Icon(Icons.Default.Refresh, contentDescription = "Reset", tint = VibeMuted)
                    }
                }
            }
        }

        // Ambient Soundscapes & Focus Audio Synthesizer
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = VibeSurface),
            border = androidx.compose.foundation.BorderStroke(1.dp, VibeBorder)
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text("🎧", fontSize = 16.sp)
                        Text(
                            text = "Ambient Focus Soundscapes",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = VibeText
                        )
                    }

                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = if (isAmbientPlaying) VibeGreen.copy(alpha = 0.2f) else VibeCard,
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .clickable { viewModel.toggleAmbientSound() }
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = if (isAmbientPlaying) Icons.Default.VolumeUp else Icons.Default.VolumeDown,
                                contentDescription = null,
                                tint = if (isAmbientPlaying) VibeGreen else VibeMuted,
                                modifier = Modifier.size(14.dp)
                            )
                            Text(
                                text = if (isAmbientPlaying) "Active" else "Play Sound",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isAmbientPlaying) VibeGreen else VibeMuted
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Sound selection chips
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    AmbientSoundType.values().forEach { sound ->
                        Surface(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(10.dp))
                                .clickable { viewModel.toggleAmbientSound(sound) },
                            shape = RoundedCornerShape(10.dp),
                            color = VibeCard,
                            border = androidx.compose.foundation.BorderStroke(1.dp, VibeBorder)
                        ) {
                            Column(
                                modifier = Modifier.padding(vertical = 8.dp, horizontal = 2.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(sound.emoji, fontSize = 18.sp)
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = sound.name.replace("_", " ").lowercase().replaceFirstChar { it.uppercase() },
                                    fontSize = 9.sp,
                                    color = VibeText,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }
                }

                if (isAmbientPlaying) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text("Volume", fontSize = 10.sp, color = VibeMuted)
                        Slider(
                            value = ambientVolume,
                            onValueChange = { viewModel.setAmbientVolume(it) },
                            valueRange = 0f..1f,
                            colors = SliderDefaults.colors(
                                thumbColor = VibeAccent,
                                activeTrackColor = VibeAccent,
                                inactiveTrackColor = VibeBorder
                            ),
                            modifier = Modifier.weight(1f)
                        )
                        Text("${(ambientVolume * 100).toInt()}%", fontSize = 10.sp, color = VibeGold)
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------
// SECTION 2: DAY-TO-DAY TASKS COMPONENT
// -------------------------------------------------------------
@Composable
private fun DayTasksSection(
    tasks: List<DayTask>,
    selectedCategory: TaskCategory,
    activeTask: DayTask?,
    onCategorySelect: (TaskCategory) -> Unit,
    onToggleTask: (String) -> Unit,
    onDeleteTask: (String) -> Unit,
    onStartFocusOnTask: (DayTask, Int) -> Unit,
    onAddTask: (String, TaskCategory, TaskPriority, String, Int, String) -> Unit
) {
    var isAddCardExpanded by remember { mutableStateOf(false) }
    var newTitle by remember { mutableStateOf("") }
    var newCategory by remember { mutableStateOf(TaskCategory.DAY_TO_DAY) }
    var newPriority by remember { mutableStateOf(TaskPriority.MEDIUM) }
    var newTime by remember { mutableStateOf("Today") }
    var newMins by remember { mutableIntStateOf(25) }
    var newNotes by remember { mutableStateOf("") }

    val filteredTasks = if (selectedCategory == TaskCategory.ALL) {
        tasks
    } else {
        tasks.filter { it.category == selectedCategory }
    }

    val completedCount = tasks.count { it.isCompleted }
    val progressRatio = if (tasks.isNotEmpty()) completedCount.toFloat() / tasks.size.toFloat() else 0f

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        // Task Progress Card
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
                    Column {
                        Text("Day's Task Velocity", fontSize = 11.sp, color = VibeMuted)
                        Text("$completedCount of ${tasks.size} Completed", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = VibeText)
                    }
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = VibeAccent.copy(alpha = 0.15f)
                    ) {
                        Text(
                            text = "${(progressRatio * 100).toInt()}% Done",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = VibeAccent,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
                LinearProgressIndicator(
                    progress = { progressRatio },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp)),
                    color = VibeGreen,
                    trackColor = VibeBorder
                )
            }
        }

        // Category Filter Chips
        val scrollState = rememberScrollState()
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(scrollState),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            TaskCategory.values().forEach { cat ->
                val isSelected = cat == selectedCategory
                Surface(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .clickable { onCategorySelect(cat) },
                    shape = RoundedCornerShape(20.dp),
                    color = if (isSelected) VibeAccent else VibeSurface,
                    border = androidx.compose.foundation.BorderStroke(1.dp, if (isSelected) VibeAccent else VibeBorder)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(cat.emoji, fontSize = 12.sp)
                        Text(
                            text = cat.displayName,
                            fontSize = 11.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            color = if (isSelected) Color.White else VibeText
                        )
                    }
                }
            }
        }

        // Add Task Action Bar / Toggle Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = VibeSurface),
            border = androidx.compose.foundation.BorderStroke(1.dp, VibeBorder)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { isAddCardExpanded = !isAddCardExpanded },
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .background(VibeAccent.copy(alpha = 0.2f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null, tint = VibeAccent, modifier = Modifier.size(16.dp))
                        }
                        Text(
                            text = if (isAddCardExpanded) "Create New Task" else "Add Day-to-Day Task",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = VibeText
                        )
                    }
                    Icon(
                        imageVector = if (isAddCardExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                        contentDescription = null,
                        tint = VibeMuted
                    )
                }

                AnimatedVisibility(visible = isAddCardExpanded) {
                    Column(
                        modifier = Modifier.padding(top = 12.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedTextField(
                            value = newTitle,
                            onValueChange = { newTitle = it },
                            placeholder = { Text("Task description (e.g., File expense report)", fontSize = 12.sp) },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = VibeAccent,
                                unfocusedBorderColor = VibeBorder,
                                focusedTextColor = VibeText,
                                unfocusedTextColor = VibeText
                            )
                        )

                        // Category Selection Chips
                        Text("Category:", fontSize = 11.sp, color = VibeMuted, fontWeight = FontWeight.Bold)
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .horizontalScroll(rememberScrollState()),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            listOf(
                                TaskCategory.DAY_TO_DAY,
                                TaskCategory.WORK,
                                TaskCategory.PLANNING,
                                TaskCategory.STUDY,
                                TaskCategory.CHORES,
                                TaskCategory.HEALTH
                            ).forEach { cat ->
                                val isChosen = newCategory == cat
                                Surface(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .clickable { newCategory = cat },
                                    shape = RoundedCornerShape(8.dp),
                                    color = if (isChosen) VibeAccent.copy(alpha = 0.2f) else VibeCard,
                                    border = androidx.compose.foundation.BorderStroke(1.dp, if (isChosen) VibeAccent else VibeBorder)
                                ) {
                                    Text(
                                        "${cat.emoji} ${cat.displayName}",
                                        fontSize = 10.sp,
                                        color = if (isChosen) VibeAccent else VibeText,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                            }
                        }

                        // Priority & Est Time Row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text("Priority:", fontSize = 11.sp, color = VibeMuted, fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.height(4.dp))
                                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                    TaskPriority.values().forEach { prio ->
                                        val isChosen = newPriority == prio
                                        Surface(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(6.dp))
                                                .clickable { newPriority = prio },
                                            shape = RoundedCornerShape(6.dp),
                                            color = if (isChosen) Color(prio.colorHex).copy(alpha = 0.25f) else VibeCard,
                                            border = androidx.compose.foundation.BorderStroke(1.dp, if (isChosen) Color(prio.colorHex) else VibeBorder)
                                        ) {
                                            Text(
                                                prio.label,
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color(prio.colorHex),
                                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                            )
                                        }
                                    }
                                }
                            }

                            Column(modifier = Modifier.weight(1f)) {
                                Text("Est. Minutes:", fontSize = 11.sp, color = VibeMuted, fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.height(4.dp))
                                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                    listOf(15, 25, 50).forEach { mins ->
                                        val isChosen = newMins == mins
                                        Surface(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(6.dp))
                                                .clickable { newMins = mins },
                                            shape = RoundedCornerShape(6.dp),
                                            color = if (isChosen) VibeGold.copy(alpha = 0.25f) else VibeCard,
                                            border = androidx.compose.foundation.BorderStroke(1.dp, if (isChosen) VibeGold else VibeBorder)
                                        ) {
                                            Text(
                                                "${mins}m",
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = if (isChosen) VibeGold else VibeMuted,
                                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        // Submit Button
                        Button(
                            onClick = {
                                if (newTitle.isNotBlank()) {
                                    onAddTask(newTitle, newCategory, newPriority, newTime, newMins, newNotes)
                                    newTitle = ""
                                    newNotes = ""
                                    isAddCardExpanded = false
                                }
                            },
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = VibeAccent, contentColor = Color.White),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Save Task", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // Task Items List
        if (filteredTasks.isEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = VibeSurface),
                border = androidx.compose.foundation.BorderStroke(1.dp, VibeBorder)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("🎉", fontSize = 28.sp)
                    Spacer(modifier = Modifier.height(6.dp))
                    Text("No tasks in this category", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = VibeText)
                    Text("Add a day-to-day task above to keep your momentum going!", fontSize = 11.sp, color = VibeMuted)
                }
            }
        } else {
            filteredTasks.forEach { task ->
                TaskItemCard(
                    task = task,
                    isCurrentFocus = activeTask?.id == task.id,
                    onToggle = { onToggleTask(task.id) },
                    onDelete = { onDeleteTask(task.id) },
                    onStartFocus = { onStartFocusOnTask(task, task.estimatedMinutes) }
                )
            }
        }
    }
}

@Composable
private fun TaskItemCard(
    task: DayTask,
    isCurrentFocus: Boolean,
    onToggle: () -> Unit,
    onDelete: () -> Unit,
    onStartFocus: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = VibeSurface),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (isCurrentFocus) VibeAccent else VibeBorder
        )
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Checkbox
                IconButton(onClick = onToggle, modifier = Modifier.size(28.dp)) {
                    Icon(
                        imageVector = if (task.isCompleted) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
                        contentDescription = "Toggle completion",
                        tint = if (task.isCompleted) VibeGreen else VibeMuted,
                        modifier = Modifier.size(22.dp)
                    )
                }

                // Title & Category
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = task.title,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (task.isCompleted) VibeMuted else VibeText,
                        textDecoration = if (task.isCompleted) TextDecoration.LineThrough else TextDecoration.None,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("${task.category.emoji} ${task.category.displayName}", fontSize = 10.sp, color = VibeMuted)
                        Text("•", fontSize = 10.sp, color = VibeMuted)
                        Text(
                            text = task.priority.label,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(task.priority.colorHex)
                        )
                        if (task.completedMinutes > 0) {
                            Text("•", fontSize = 10.sp, color = VibeMuted)
                            Text("${task.completedMinutes}m logged", fontSize = 10.sp, color = VibeGold)
                        }
                    }
                }

                // Focus Action Button
                if (!task.isCompleted) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = if (isCurrentFocus) VibeAccent else VibeCard,
                        border = androidx.compose.foundation.BorderStroke(1.dp, if (isCurrentFocus) VibeAccent else VibeBorder),
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .clickable(onClick = onStartFocus)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text("🎯", fontSize = 11.sp)
                            Text(
                                text = if (isCurrentFocus) "Focusing" else "Focus Now",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isCurrentFocus) Color.White else VibeText
                            )
                        }
                    }
                }

                // Delete
                IconButton(onClick = onDelete, modifier = Modifier.size(24.dp)) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete", tint = VibeMuted.copy(alpha = 0.6f), modifier = Modifier.size(16.dp))
                }
            }

            if (task.notes.isNotBlank()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = task.notes,
                    fontSize = 11.sp,
                    color = VibeMuted,
                    modifier = Modifier.padding(start = 36.dp)
                )
            }
        }
    }
}

// -------------------------------------------------------------
// SECTION 3: DAY PLANNER & TIME-BLOCKING COMPONENT
// -------------------------------------------------------------
@Composable
private fun DayPlannerSection(
    plannerBlocks: List<DayPlannerBlock>,
    dailyGoals: List<DailyGoal>,
    onToggleBlock: (String) -> Unit,
    onAddBlock: (String, String, String, String) -> Unit,
    onDeleteBlock: (String) -> Unit,
    onToggleGoal: (String) -> Unit,
    onAddGoal: (String) -> Unit,
    onDeleteGoal: (String) -> Unit
) {
    var newGoalText by remember { mutableStateOf("") }
    var isAddBlockExpanded by remember { mutableStateOf(false) }
    var newBlockPeriod by remember { mutableStateOf("Custom Time") }
    var newBlockTitle by remember { mutableStateOf("") }
    var newBlockDesc by remember { mutableStateOf("") }
    var newBlockEmoji by remember { mutableStateOf("🗓️") }

    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
        // Daily Top 3 Intentions / Goals Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = VibeSurface),
            border = androidx.compose.foundation.BorderStroke(1.dp, VibeBorder)
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text("🌟", fontSize = 16.sp)
                        Text(
                            text = "Today's Top 3 Priorities",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = VibeText
                        )
                    }
                    Text(
                        text = "${dailyGoals.count { it.isAchieved }}/${dailyGoals.size} Done",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = VibeGold
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                dailyGoals.forEach { goal ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        IconButton(onClick = { onToggleGoal(goal.id) }, modifier = Modifier.size(24.dp)) {
                            Icon(
                                imageVector = if (goal.isAchieved) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
                                contentDescription = null,
                                tint = if (goal.isAchieved) VibeGreen else VibeMuted,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Text(
                            text = goal.title,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = if (goal.isAchieved) VibeMuted else VibeText,
                            textDecoration = if (goal.isAchieved) TextDecoration.LineThrough else TextDecoration.None,
                            modifier = Modifier.weight(1f)
                        )
                        IconButton(onClick = { onDeleteGoal(goal.id) }, modifier = Modifier.size(20.dp)) {
                            Icon(Icons.Default.Close, contentDescription = "Delete", tint = VibeMuted.copy(alpha = 0.5f), modifier = Modifier.size(14.dp))
                        }
                    }
                }

                // Quick Add Goal Input
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    OutlinedTextField(
                        value = newGoalText,
                        onValueChange = { newGoalText = it },
                        placeholder = { Text("Add daily intention...", fontSize = 11.sp) },
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = VibeAccent,
                            unfocusedBorderColor = VibeBorder,
                            focusedTextColor = VibeText,
                            unfocusedTextColor = VibeText
                        )
                    )
                    Button(
                        onClick = {
                            if (newGoalText.isNotBlank()) {
                                onAddGoal(newGoalText)
                                newGoalText = ""
                            }
                        },
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = VibeAccent, contentColor = Color.White)
                    ) {
                        Text("Add", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Time-Blocked Daily Schedule Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text("Time-Blocked Day Schedule", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = VibeText)
                Text("Morning, afternoon & evening focus blocks", fontSize = 11.sp, color = VibeMuted)
            }
            TextButton(onClick = { isAddBlockExpanded = !isAddBlockExpanded }) {
                Text("+ Add Block", fontSize = 11.sp, color = VibeAccent, fontWeight = FontWeight.Bold)
            }
        }

        AnimatedVisibility(visible = isAddBlockExpanded) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = VibeSurface),
                border = androidx.compose.foundation.BorderStroke(1.dp, VibeBorder)
            ) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("New Time-Blocked Block", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = VibeText)
                    OutlinedTextField(
                        value = newBlockPeriod,
                        onValueChange = { newBlockPeriod = it },
                        placeholder = { Text("e.g. Afternoon (2PM - 5PM)", fontSize = 11.sp) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = newBlockTitle,
                        onValueChange = { newBlockTitle = it },
                        placeholder = { Text("Block Title (e.g. Project Sprint)", fontSize = 11.sp) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = newBlockDesc,
                        onValueChange = { newBlockDesc = it },
                        placeholder = { Text("Key focus items & deliverable", fontSize = 11.sp) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    Button(
                        onClick = {
                            if (newBlockTitle.isNotBlank()) {
                                onAddBlock(newBlockPeriod, newBlockTitle, newBlockDesc, newBlockEmoji)
                                newBlockTitle = ""
                                newBlockDesc = ""
                                isAddBlockExpanded = false
                            }
                        },
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = VibeAccent),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Save Time Block", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Time Blocks Cards
        plannerBlocks.forEach { block ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = VibeSurface),
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    if (block.isCompleted) VibeGreen.copy(alpha = 0.5f) else VibeBorder
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(block.iconEmoji, fontSize = 26.sp)

                    Column(modifier = Modifier.weight(1f)) {
                        Text(block.period, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = VibeAccent)
                        Text(
                            text = block.title,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (block.isCompleted) VibeMuted else VibeText,
                            textDecoration = if (block.isCompleted) TextDecoration.LineThrough else TextDecoration.None
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = block.tasksDescription,
                            fontSize = 11.sp,
                            color = VibeMuted,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    IconButton(onClick = { onToggleBlock(block.id) }, modifier = Modifier.size(32.dp)) {
                        Icon(
                            imageVector = if (block.isCompleted) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
                            contentDescription = "Toggle block completion",
                            tint = if (block.isCompleted) VibeGreen else VibeMuted,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------
// SECTION 4: HABITS & PRODUCTIVITY STATS COMPONENT
// -------------------------------------------------------------
@Composable
private fun HabitsAndStatsSection(
    totalMins: Int,
    tasks: List<DayTask>,
    dailyHabits: List<DailyHabit>,
    onToggleHabit: (String) -> Unit,
    onAddHabit: (String, String) -> Unit
) {
    var newHabitTitle by remember { mutableStateOf("") }
    var newHabitEmoji by remember { mutableStateOf("⚡") }
    var isAddHabitOpen by remember { mutableStateOf(false) }

    val completedTasksCount = tasks.count { it.isCompleted }
    val doneHabitsCount = dailyHabits.count { it.isDoneToday }

    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
        // Stats Overview Grid
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Lifetime Focus Mins
            Card(
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = VibeSurface),
                border = androidx.compose.foundation.BorderStroke(1.dp, VibeBorder)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text("Total Focus Logged", fontSize = 10.sp, color = VibeMuted)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("$totalMins min", fontSize = 22.sp, fontWeight = FontWeight.Black, color = VibeGold)
                    Text("Across all sprints", fontSize = 9.sp, color = VibeMuted)
                }
            }

            // Tasks Done Ratio
            Card(
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = VibeSurface),
                border = androidx.compose.foundation.BorderStroke(1.dp, VibeBorder)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text("Tasks Completed", fontSize = 10.sp, color = VibeMuted)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("$completedTasksCount / ${tasks.size}", fontSize = 22.sp, fontWeight = FontWeight.Black, color = VibeGreen)
                    Text("Day-to-day items", fontSize = 9.sp, color = VibeMuted)
                }
            }
        }

        // Daily Habits Checklist
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = VibeSurface),
            border = androidx.compose.foundation.BorderStroke(1.dp, VibeBorder)
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Daily Habit Streaks", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = VibeText)
                        Text("$doneHabitsCount of ${dailyHabits.size} completed today", fontSize = 10.sp, color = VibeMuted)
                    }
                    TextButton(onClick = { isAddHabitOpen = !isAddHabitOpen }) {
                        Text("+ New Habit", fontSize = 11.sp, color = VibeAccent, fontWeight = FontWeight.Bold)
                    }
                }

                AnimatedVisibility(visible = isAddHabitOpen) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        OutlinedTextField(
                            value = newHabitTitle,
                            onValueChange = { newHabitTitle = it },
                            placeholder = { Text("e.g. 10m Meditation", fontSize = 11.sp) },
                            modifier = Modifier.weight(1f),
                            singleLine = true
                        )
                        Button(
                            onClick = {
                                if (newHabitTitle.isNotBlank()) {
                                    onAddHabit(newHabitTitle, newHabitEmoji)
                                    newHabitTitle = ""
                                    isAddHabitOpen = false
                                }
                            },
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = VibeAccent)
                        ) {
                            Text("Save", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                dailyHabits.forEach { habit ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(habit.emoji, fontSize = 20.sp)
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = habit.title,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                color = if (habit.isDoneToday) VibeMuted else VibeText,
                                textDecoration = if (habit.isDoneToday) TextDecoration.LineThrough else TextDecoration.None
                            )
                            Text(
                                text = "🔥 Streak: ${habit.streak} Days",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = VibeGold
                            )
                        }

                        IconButton(onClick = { onToggleHabit(habit.id) }, modifier = Modifier.size(28.dp)) {
                            Icon(
                                imageVector = if (habit.isDoneToday) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
                                contentDescription = null,
                                tint = if (habit.isDoneToday) VibeGreen else VibeMuted,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }
        }

        // Productivity Architecture Summary Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = VibeSurface),
            border = androidx.compose.foundation.BorderStroke(1.dp, VibeBorder)
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text("🧠 High-Performance Daily Workflow", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = VibeGold)
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "1. Morning: Define your Top 3 Priorities in Day Planner.\n" +
                           "2. Execution: Pick a Day Task and hit 'Focus Now' for a 25-50m sprint.\n" +
                           "3. Flow: Use ambient binaural soundscapes to block distractions.\n" +
                           "4. Evening: Review wins and check off habit streaks.",
                    fontSize = 11.sp,
                    color = VibeMuted,
                    lineHeight = 18.sp
                )
            }
        }
    }
}
