package com.example.ui

import android.os.CountDownTimer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.VibeDataDefaults
import com.example.model.Coupon
import com.example.model.DailyGoal
import com.example.model.DailyHabit
import com.example.model.DayPlannerBlock
import com.example.model.DayTask
import com.example.model.KaraokeRecording
import com.example.model.MerchProduct
import com.example.model.ReelItem
import com.example.model.StoreMovie
import com.example.model.StudySession
import com.example.model.TaskCategory
import com.example.model.TaskPriority
import com.example.model.TheatrePlan
import com.example.model.TrackItem
import com.example.model.VinylRecord
import com.example.audio.AmbientSoundPlayer
import com.example.audio.AmbientSoundType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

enum class VibeTab(val id: String, val label: String, val emoji: String) {
    MUSIC("music", "Music", "🎵"),
    MOVIES("movies", "Movies", "🎬"),
    KARAOKE("karaoke", "Karaoke", "🎤"),
    THEATRE("theatre", "Theatre", "🎭"),
    REELS("reels", "Shorts", "📱"),
    STORE("store", "Merch", "🛒"),
    SHOP("shop", "Vinyl", "🛍️")
}

class VibeAppViewModel : ViewModel() {

    // Current active tab
    private val _activeTab = MutableStateFlow(VibeTab.MUSIC)
    val activeTab: StateFlow<VibeTab> = _activeTab.asStateFlow()

    // Ads states
    private val _isTopAdVisible = MutableStateFlow(true)
    val isTopAdVisible: StateFlow<Boolean> = _isTopAdVisible.asStateFlow()

    private val _showSpeakerOffer = MutableStateFlow(false)
    val showSpeakerOffer: StateFlow<Boolean> = _showSpeakerOffer.asStateFlow()

    private val _showTshirtOffer = MutableStateFlow(false)
    val showTshirtOffer: StateFlow<Boolean> = _showTshirtOffer.asStateFlow()

    private val _checkoutProduct = MutableStateFlow<MerchProduct?>(null)
    val checkoutProduct: StateFlow<MerchProduct?> = _checkoutProduct.asStateFlow()

    private val _showToastMessage = MutableStateFlow<String?>(null)
    val showToastMessage: StateFlow<String?> = _showToastMessage.asStateFlow()

    // --- MUSIC STATE ---
    private val _tracks = MutableStateFlow<List<TrackItem>>(VibeDataDefaults.DEFAULT_TRACKS)
    val tracks: StateFlow<List<TrackItem>> = _tracks.asStateFlow()

    private val _currentTrack = MutableStateFlow<TrackItem?>(VibeDataDefaults.DEFAULT_TRACKS.firstOrNull())
    val currentTrack: StateFlow<TrackItem?> = _currentTrack.asStateFlow()

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying.asStateFlow()

    private val _isShuffle = MutableStateFlow(false)
    val isShuffle: StateFlow<Boolean> = _isShuffle.asStateFlow()

    private val _isRepeat = MutableStateFlow(false)
    val isRepeat: StateFlow<Boolean> = _isRepeat.asStateFlow()

    private val _isRadio = MutableStateFlow(false)
    val isRadio: StateFlow<Boolean> = _isRadio.asStateFlow()

    private val _activeLabelFilter = MutableStateFlow<String?>(null)
    val activeLabelFilter: StateFlow<String?> = _activeLabelFilter.asStateFlow()

    private val _trackSearchQuery = MutableStateFlow("")
    val trackSearchQuery: StateFlow<String> = _trackSearchQuery.asStateFlow()

    private val _shareTrack = MutableStateFlow<TrackItem?>(null)
    val shareTrack: StateFlow<TrackItem?> = _shareTrack.asStateFlow()

    // --- MOVIES STATE ---
    private val _movies = MutableStateFlow<List<StoreMovie>>(VibeDataDefaults.STORE_MOVIES)
    val movies: StateFlow<List<StoreMovie>> = _movies.asStateFlow()

    private val _activeUniverse = MutableStateFlow("ALL") // ALL, MARVEL, DC, ANIME, BOLLYWOOD, HOLLYWOOD
    val activeUniverse: StateFlow<String> = _activeUniverse.asStateFlow()

    private val _movieSearchQuery = MutableStateFlow("")
    val movieSearchQuery: StateFlow<String> = _movieSearchQuery.asStateFlow()

    private val _showTasteCard = MutableStateFlow(false)
    val showTasteCard: StateFlow<Boolean> = _showTasteCard.asStateFlow()

    private val _showCouponsModal = MutableStateFlow(false)
    val showCouponsModal: StateFlow<Boolean> = _showCouponsModal.asStateFlow()

    private val _selectedTrailerMovie = MutableStateFlow<StoreMovie?>(null)
    val selectedTrailerMovie: StateFlow<StoreMovie?> = _selectedTrailerMovie.asStateFlow()

    fun openTrailer(movie: StoreMovie) {
        _selectedTrailerMovie.value = movie
    }

    fun closeTrailer() {
        _selectedTrailerMovie.value = null
    }

    // --- KARAOKE STATE ---
    private val _isKaraokeRecording = MutableStateFlow(false)
    val isKaraokeRecording: StateFlow<Boolean> = _isKaraokeRecording.asStateFlow()

    private val _karaokeRecordings = MutableStateFlow<List<KaraokeRecording>>(
        listOf(
            KaraokeRecording("rec_1", "Starboy - Practice Take 1", 145),
            KaraokeRecording("rec_2", "Sweater Weather - Acoustic Cover", 182)
        )
    )
    val karaokeRecordings: StateFlow<List<KaraokeRecording>> = _karaokeRecordings.asStateFlow()

    private val _karaokeSearch = MutableStateFlow("")
    val karaokeSearch: StateFlow<String> = _karaokeSearch.asStateFlow()

    private val _selectedKaraokeTrack = MutableStateFlow<String?>("rkWJyMhIWLo")
    val selectedKaraokeTrack: StateFlow<String?> = _selectedKaraokeTrack.asStateFlow()

    // --- THEATRE / WATCH PARTY STATE ---
    private val _theatrePlans = MutableStateFlow<List<TheatrePlan>>(
        listOf(
            TheatrePlan("p1", "Avengers: Endgame Marathon", "Saturday · 8:00 PM", "Popcorn, Nachos & Coke", listOf("You", "Aman", "Rohan", "Sneha"), 800),
            TheatrePlan("p2", "Spirited Away Chill Night", "Sunday · 6:30 PM", "Hot Ramen & Boba", listOf("You", "Ananya"), 450)
        )
    )
    val theatrePlans: StateFlow<List<TheatrePlan>> = _theatrePlans.asStateFlow()

    // --- REELS STATE ---
    private val _reels = MutableStateFlow<List<ReelItem>>(VibeDataDefaults.SAMPLE_REELS)
    val reels: StateFlow<List<ReelItem>> = _reels.asStateFlow()

    // --- FOCUS, DAY-TO-DAY TASKS & PLANNING STATE ---
    private val _tasks = MutableStateFlow<List<DayTask>>(VibeDataDefaults.DEFAULT_TASKS)
    val tasks: StateFlow<List<DayTask>> = _tasks.asStateFlow()

    private val _activeFocusTask = MutableStateFlow<DayTask?>(VibeDataDefaults.DEFAULT_TASKS.firstOrNull { !it.isCompleted })
    val activeFocusTask: StateFlow<DayTask?> = _activeFocusTask.asStateFlow()

    private val _selectedTaskFilterCategory = MutableStateFlow(TaskCategory.ALL)
    val selectedTaskFilterCategory: StateFlow<TaskCategory> = _selectedTaskFilterCategory.asStateFlow()

    private val _plannerBlocks = MutableStateFlow<List<DayPlannerBlock>>(VibeDataDefaults.DEFAULT_PLANNER_BLOCKS)
    val plannerBlocks: StateFlow<List<DayPlannerBlock>> = _plannerBlocks.asStateFlow()

    private val _dailyGoals = MutableStateFlow<List<DailyGoal>>(VibeDataDefaults.DEFAULT_DAILY_GOALS)
    val dailyGoals: StateFlow<List<DailyGoal>> = _dailyGoals.asStateFlow()

    private val _dailyHabits = MutableStateFlow<List<DailyHabit>>(VibeDataDefaults.DEFAULT_DAILY_HABITS)
    val dailyHabits: StateFlow<List<DailyHabit>> = _dailyHabits.asStateFlow()

    private val _focusPresetMinutes = MutableStateFlow(25)
    val focusPresetMinutes: StateFlow<Int> = _focusPresetMinutes.asStateFlow()

    private val _focusAtmosphere = MutableStateFlow("Rainy Tokyo")
    val focusAtmosphere: StateFlow<String> = _focusAtmosphere.asStateFlow()

    private val ambientPlayer = AmbientSoundPlayer()

    private val _isAmbientSoundPlaying = MutableStateFlow(false)
    val isAmbientSoundPlaying: StateFlow<Boolean> = _isAmbientSoundPlaying.asStateFlow()

    private val _ambientVolume = MutableStateFlow(0.6f)
    val ambientVolume: StateFlow<Float> = _ambientVolume.asStateFlow()

    private val _isStudyTimerRunning = MutableStateFlow(false)
    val isStudyTimerRunning: StateFlow<Boolean> = _isStudyTimerRunning.asStateFlow()

    private val _studySecondsRemaining = MutableStateFlow(25 * 60)
    val studySecondsRemaining: StateFlow<Int> = _studySecondsRemaining.asStateFlow()

    private val _totalStudyMinutes = MutableStateFlow(125)
    val totalStudyMinutes: StateFlow<Int> = _totalStudyMinutes.asStateFlow()

    private var studyTimer: CountDownTimer? = null

    // --- STORE & SHOP ---
    val merchProducts: StateFlow<List<MerchProduct>> = MutableStateFlow(VibeDataDefaults.SHOP_PRODUCTS).asStateFlow()
    val vinylRecords: StateFlow<List<VinylRecord>> = MutableStateFlow(VibeDataDefaults.VINYL_RECORDS).asStateFlow()

    fun switchTab(tab: VibeTab) {
        _activeTab.value = tab
    }

    // Ads controls
    fun closeTopAd() {
        _isTopAdVisible.value = false
        triggerToast("Ad hidden. Upgrade to VIBE PRO for an ad-free experience!")
    }

    fun openSpeakerOffer() {
        _showSpeakerOffer.value = true
    }

    fun closeSpeakerOffer() {
        _showSpeakerOffer.value = false
    }

    fun openTshirtOffer() {
        _showTshirtOffer.value = true
    }

    fun closeTshirtOffer() {
        _showTshirtOffer.value = false
    }

    fun openCheckout(product: MerchProduct) {
        _checkoutProduct.value = product
    }

    fun closeCheckout() {
        _checkoutProduct.value = null
    }

    fun completeCheckout(product: MerchProduct) {
        _checkoutProduct.value = null
        _showSpeakerOffer.value = false
        _showTshirtOffer.value = false
        triggerToast("🎉 Order Placed for ${product.name}! GPay confirmation sent.")
    }

    // Music actions
    fun playTrack(track: TrackItem) {
        _currentTrack.value = track
        _isPlaying.value = true
    }

    fun togglePlay() {
        if (_currentTrack.value == null && _tracks.value.isNotEmpty()) {
            _currentTrack.value = _tracks.value.first()
        }
        _isPlaying.value = !_isPlaying.value
    }

    fun playNext() {
        val list = _tracks.value
        if (list.isEmpty()) return
        val currentIdx = list.indexOfFirst { it.id == _currentTrack.value?.id }
        val nextIdx = if (_isShuffle.value) {
            (list.indices).random()
        } else {
            (currentIdx + 1) % list.size
        }
        _currentTrack.value = list[nextIdx]
        _isPlaying.value = true
    }

    fun playPrev() {
        val list = _tracks.value
        if (list.isEmpty()) return
        val currentIdx = list.indexOfFirst { it.id == _currentTrack.value?.id }
        val prevIdx = if (currentIdx <= 0) list.size - 1 else currentIdx - 1
        _currentTrack.value = list[prevIdx]
        _isPlaying.value = true
    }

    fun toggleShuffle() {
        _isShuffle.value = !_isShuffle.value
        triggerToast(if (_isShuffle.value) "🔀 Shuffle On" else "Shuffle Off")
    }

    fun toggleRepeat() {
        _isRepeat.value = !_isRepeat.value
        triggerToast(if (_isRepeat.value) "🔁 Repeat Track On" else "Repeat Off")
    }

    fun toggleRadio() {
        _isRadio.value = !_isRadio.value
        triggerToast(if (_isRadio.value) "📻 VIBE Endless Radio Activated" else "Radio Mode Off")
    }

    fun setLabelFilter(label: String?) {
        _activeLabelFilter.value = if (_activeLabelFilter.value == label) null else label
    }

    fun setTrackSearch(query: String) {
        _trackSearchQuery.value = query
    }

    fun addTrackFromSearch(title: String, artist: String = "VIBE Artist") {
        if (title.isBlank()) return
        val id = "yt_${System.currentTimeMillis().toString().takeLast(6)}"
        val newTrack = TrackItem(
            id = id,
            title = title,
            channel = artist,
            labels = listOf(_activeLabelFilter.value ?: "Chill")
        )
        _tracks.value = listOf(newTrack) + _tracks.value
        _currentTrack.value = newTrack
        _isPlaying.value = true
        triggerToast("Added '$title' to your VIBE playlist!")
    }

    fun deleteTrack(track: TrackItem) {
        _tracks.value = _tracks.value.filter { it.id != track.id }
        if (_currentTrack.value?.id == track.id) {
            _currentTrack.value = _tracks.value.firstOrNull()
        }
        triggerToast("Removed track")
    }

    fun openShareTrack(track: TrackItem) {
        _shareTrack.value = track
    }

    fun closeShareTrack() {
        _shareTrack.value = null
    }

    // Movie actions
    fun setMovieUniverse(univ: String) {
        _activeUniverse.value = univ
    }

    fun setMovieSearch(query: String) {
        _movieSearchQuery.value = query
    }

    fun toggleMovieWatched(movie: StoreMovie) {
        _movies.value = _movies.value.map {
            if (it.id == movie.id) it.copy(isWatched = !it.isWatched) else it
        }
        val isNowWatched = _movies.value.find { it.id == movie.id }?.isWatched == true
        triggerToast(if (isNowWatched) "Marked as Watched! 🍿" else "Added to Watchlist 📌")
    }

    fun openTasteCard() {
        _showTasteCard.value = true
    }

    fun closeTasteCard() {
        _showTasteCard.value = false
    }

    fun openCouponsModal() {
        _showCouponsModal.value = true
    }

    fun closeCouponsModal() {
        _showCouponsModal.value = false
    }

    // Karaoke actions
    fun setKaraokeSearch(q: String) {
        _karaokeSearch.value = q
    }

    fun selectKaraokeTrack(videoId: String) {
        _selectedKaraokeTrack.value = videoId
    }

    fun toggleKaraokeRecording() {
        if (_isKaraokeRecording.value) {
            // Stop recording
            _isKaraokeRecording.value = false
            val newRec = KaraokeRecording(
                id = "rec_${System.currentTimeMillis()}",
                songTitle = "Karaoke Session ${karaokeRecordings.value.size + 1}",
                durationSec = (30..180).random()
            )
            _karaokeRecordings.value = listOf(newRec) + _karaokeRecordings.value
            triggerToast("🎉 Recording Saved to your Karaoke Library!")
        } else {
            // Start recording
            _isKaraokeRecording.value = true
            triggerToast("🎙️ Recording started! Sing along with the beat...")
        }
    }

    fun deleteKaraokeRecording(rec: KaraokeRecording) {
        _karaokeRecordings.value = _karaokeRecordings.value.filter { it.id != rec.id }
        triggerToast("Deleted recording")
    }

    // Theatre / Watch Party actions
    fun addTheatrePlan(movieTitle: String, dateTime: String, snacks: String, friendNames: List<String>, cost: Int) {
        if (movieTitle.isBlank()) return
        val newPlan = TheatrePlan(
            id = "plan_${System.currentTimeMillis()}",
            movieTitle = movieTitle,
            dateTime = dateTime.ifBlank { "This Weekend" },
            snacks = snacks.ifBlank { "Popcorn & Soda" },
            friends = if (friendNames.isEmpty()) listOf("You") else friendNames,
            totalCost = if (cost <= 0) 500 else cost
        )
        _theatrePlans.value = listOf(newPlan) + _theatrePlans.value
        triggerToast("🎬 Watch Party Plan Created! ₹${newPlan.costPerPerson} per person.")
    }

    fun deleteTheatrePlan(plan: TheatrePlan) {
        _theatrePlans.value = _theatrePlans.value.filter { it.id != plan.id }
        triggerToast("Removed plan")
    }

    // --- FOCUS, DAY TASKS & PLANNER ACTIONS ---
    fun setFocusPreset(mins: Int) {
        _focusPresetMinutes.value = mins
        if (!_isStudyTimerRunning.value) {
            _studySecondsRemaining.value = mins * 60
        }
        triggerToast("Timer set to $mins mins")
    }

    fun selectTaskToFocus(task: DayTask?) {
        _activeFocusTask.value = task
        if (task != null) {
            triggerToast("🎯 Focusing on: ${task.title}")
        }
    }

    fun startFocusForTask(task: DayTask, mins: Int = 25) {
        _activeFocusTask.value = task
        _focusPresetMinutes.value = mins
        _studySecondsRemaining.value = mins * 60
        if (!_isStudyTimerRunning.value) {
            toggleStudyTimer()
        }
        triggerToast("🚀 Started $mins-min sprint on: ${task.title}")
    }

    fun toggleTask(taskId: String) {
        var completedNow = false
        _tasks.value = _tasks.value.map { task ->
            if (task.id == taskId) {
                val newState = !task.isCompleted
                completedNow = newState
                task.copy(isCompleted = newState)
            } else {
                task
            }
        }
        triggerToast(if (completedNow) "✅ Task completed! Keep going." else "Task marked as active")
    }

    fun addNewTask(
        title: String,
        category: TaskCategory = TaskCategory.DAY_TO_DAY,
        priority: TaskPriority = TaskPriority.MEDIUM,
        scheduledTime: String = "Today",
        estimatedMins: Int = 25,
        notes: String = ""
    ) {
        if (title.isBlank()) return
        val newTask = DayTask(
            id = "task_${System.currentTimeMillis()}",
            title = title.trim(),
            category = category,
            priority = priority,
            scheduledTime = scheduledTime.ifBlank { "Today" },
            estimatedMinutes = if (estimatedMins <= 0) 25 else estimatedMins,
            notes = notes.trim()
        )
        _tasks.value = listOf(newTask) + _tasks.value
        triggerToast("📝 Added task to ${category.displayName}")
    }

    fun deleteTask(taskId: String) {
        _tasks.value = _tasks.value.filter { it.id != taskId }
        if (_activeFocusTask.value?.id == taskId) {
            _activeFocusTask.value = null
        }
        triggerToast("Task deleted")
    }

    fun filterTasks(category: TaskCategory) {
        _selectedTaskFilterCategory.value = category
    }

    // Daily Goals
    fun toggleGoal(goalId: String) {
        var achievedNow = false
        _dailyGoals.value = _dailyGoals.value.map { goal ->
            if (goal.id == goalId) {
                val newState = !goal.isAchieved
                achievedNow = newState
                goal.copy(isAchieved = newState)
            } else {
                goal
            }
        }
        triggerToast(if (achievedNow) "🌟 Daily priority achieved!" else "Priority unchecked")
    }

    fun addNewGoal(title: String) {
        if (title.isBlank()) return
        val newGoal = DailyGoal(
            id = "goal_${System.currentTimeMillis()}",
            title = title.trim()
        )
        _dailyGoals.value = _dailyGoals.value + newGoal
        triggerToast("🎯 Added daily focus intention")
    }

    fun deleteGoal(goalId: String) {
        _dailyGoals.value = _dailyGoals.value.filter { it.id != goalId }
    }

    // Habits
    fun toggleHabit(habitId: String) {
        _dailyHabits.value = _dailyHabits.value.map { habit ->
            if (habit.id == habitId) {
                val newDone = !habit.isDoneToday
                val newStreak = if (newDone) habit.streak + 1 else (habit.streak - 1).coerceAtLeast(0)
                habit.copy(isDoneToday = newDone, streak = newStreak)
            } else {
                habit
            }
        }
        triggerToast("🔥 Habit streak updated!")
    }

    fun addNewHabit(title: String, emoji: String = "⚡") {
        if (title.isBlank()) return
        val newHabit = DailyHabit(
            id = "habit_${System.currentTimeMillis()}",
            title = title.trim(),
            emoji = emoji.ifBlank { "⚡" },
            streak = 1,
            isDoneToday = false
        )
        _dailyHabits.value = _dailyHabits.value + newHabit
        triggerToast("🌱 New daily habit added")
    }

    // Day Planner Time-Blocks
    fun togglePlannerBlock(blockId: String) {
        _plannerBlocks.value = _plannerBlocks.value.map { block ->
            if (block.id == blockId) {
                block.copy(isCompleted = !block.isCompleted)
            } else {
                block
            }
        }
        triggerToast("Schedule block updated")
    }

    fun addNewPlannerBlock(period: String, title: String, desc: String, emoji: String) {
        if (title.isBlank()) return
        val newBlock = DayPlannerBlock(
            id = "block_${System.currentTimeMillis()}",
            period = period.ifBlank { "Custom Block" },
            title = title.trim(),
            tasksDescription = desc.trim(),
            iconEmoji = emoji.ifBlank { "🗓️" }
        )
        _plannerBlocks.value = _plannerBlocks.value + newBlock
        triggerToast("🗓️ Added schedule block to day plan")
    }

    fun deletePlannerBlock(blockId: String) {
        _plannerBlocks.value = _plannerBlocks.value.filter { it.id != blockId }
        triggerToast("Schedule block removed")
    }

    // Ambient Soundscape
    fun toggleAmbientSound(soundType: AmbientSoundType? = null) {
        if (_isAmbientSoundPlaying.value && (soundType == null || ambientPlayer.currentSound == soundType)) {
            ambientPlayer.stop()
            _isAmbientSoundPlaying.value = false
            triggerToast("Ambient sound paused")
        } else {
            val targetSound = soundType ?: AmbientSoundType.RAIN
            ambientPlayer.play(targetSound, viewModelScope)
            _isAmbientSoundPlaying.value = true
            _focusAtmosphere.value = targetSound.title
            triggerToast("Playing ${targetSound.emoji} ${targetSound.title}")
        }
    }

    fun setAmbientVolume(vol: Float) {
        _ambientVolume.value = vol
        ambientPlayer.volume = vol
    }

    // Focus Timer
    fun toggleStudyTimer() {
        if (_isStudyTimerRunning.value) {
            studyTimer?.cancel()
            _isStudyTimerRunning.value = false
        } else {
            _isStudyTimerRunning.value = true
            val millis = _studySecondsRemaining.value * 1000L
            val sessionMinutes = _focusPresetMinutes.value

            studyTimer = object : CountDownTimer(millis, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    _studySecondsRemaining.value = (millisUntilFinished / 1000).toInt()
                }

                override fun onFinish() {
                    _isStudyTimerRunning.value = false
                    _totalStudyMinutes.value += sessionMinutes
                    _studySecondsRemaining.value = sessionMinutes * 60

                    // If active focus task, log time
                    _activeFocusTask.value?.let { activeTask ->
                        _tasks.value = _tasks.value.map { t ->
                            if (t.id == activeTask.id) {
                                t.copy(completedMinutes = t.completedMinutes + sessionMinutes)
                            } else {
                                t
                            }
                        }
                    }

                    triggerToast("🎉 Focus sprint finished! +$sessionMinutes mins logged.")
                }
            }.start()
        }
    }

    fun resetStudyTimer() {
        studyTimer?.cancel()
        _isStudyTimerRunning.value = false
        _studySecondsRemaining.value = _focusPresetMinutes.value * 60
    }

    fun triggerToast(msg: String) {
        _showToastMessage.value = msg
    }

    fun clearToast() {
        _showToastMessage.value = null
    }

    override fun onCleared() {
        super.onCleared()
        studyTimer?.cancel()
        ambientPlayer.stop()
    }
}
