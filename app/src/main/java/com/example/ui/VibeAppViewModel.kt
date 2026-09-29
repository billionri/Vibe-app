package com.example.ui

import android.os.CountDownTimer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.VibeDataDefaults
import com.example.model.Coupon
import com.example.model.KaraokeRecording
import com.example.model.MerchProduct
import com.example.model.ReelItem
import com.example.model.StoreMovie
import com.example.model.StudySession
import com.example.model.TheatrePlan
import com.example.model.TrackItem
import com.example.model.VinylRecord
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
    STUDY("study", "Lo-Fi", "📚"),
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

    // --- LO-FI STUDY ROOM STATE ---
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

    // Study Room Timer
    fun toggleStudyTimer() {
        if (_isStudyTimerRunning.value) {
            studyTimer?.cancel()
            _isStudyTimerRunning.value = false
        } else {
            _isStudyTimerRunning.value = true
            val millis = _studySecondsRemaining.value * 1000L
            studyTimer = object : CountDownTimer(millis, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    _studySecondsRemaining.value = (millisUntilFinished / 1000).toInt()
                }

                override fun onFinish() {
                    _isStudyTimerRunning.value = false
                    _totalStudyMinutes.value += 25
                    _studySecondsRemaining.value = 25 * 60
                    triggerToast("✨ Great focus session completed! +25 mins added.")
                }
            }.start()
        }
    }

    fun resetStudyTimer() {
        studyTimer?.cancel()
        _isStudyTimerRunning.value = false
        _studySecondsRemaining.value = 25 * 60
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
    }
}
