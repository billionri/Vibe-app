package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.audio.AmbientSoundPlayer
import com.example.audio.AmbientSoundType
import com.example.data.local.MovieEntity
import com.example.data.local.PlaylistEntity
import com.example.data.local.TrackEntity
import com.example.data.local.VibeDatabase
import com.example.data.local.VibeLogEntity
import com.example.data.repository.VibeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class VibeRouletteResult(
    val vibeTag: String,
    val matchedPlaylist: PlaylistEntity?,
    val matchedMovie: MovieEntity?,
    val quote: String
)

class VibeViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: VibeRepository
    private val ambientPlayer = AmbientSoundPlayer()

    // Ambient state
    private val _isAmbientPlaying = MutableStateFlow(false)
    val isAmbientPlaying: StateFlow<Boolean> = _isAmbientPlaying.asStateFlow()

    private val _currentAmbient = MutableStateFlow(AmbientSoundType.RAIN)
    val currentAmbient: StateFlow<AmbientSoundType> = _currentAmbient.asStateFlow()

    private val _ambientVolume = MutableStateFlow(0.6f)
    val ambientVolume: StateFlow<Float> = _ambientVolume.asStateFlow()

    // Navigation and screen state
    val allPlaylists: StateFlow<List<PlaylistEntity>>
    val allMovies: StateFlow<List<MovieEntity>>
    val allTracks: StateFlow<List<TrackEntity>>
    val vibeLogs: StateFlow<List<VibeLogEntity>>

    // Search and filter states for Playlists
    val playlistSearchQuery = MutableStateFlow("")
    val selectedPlaylistVibeFilter = MutableStateFlow("All")

    // Filtered playlists
    val filteredPlaylists: StateFlow<List<PlaylistEntity>>

    // Search and filter states for Movies
    val movieSearchQuery = MutableStateFlow("")
    val selectedMovieStatusFilter = MutableStateFlow("ALL") // ALL, WANT_TO_WATCH, WATCHING, WATCHED, FAVORITES
    val selectedMovieVibeFilter = MutableStateFlow("All")

    // Filtered movies
    val filteredMovies: StateFlow<List<MovieEntity>>

    // Selected playlist details
    private val _selectedPlaylistId = MutableStateFlow<Long?>(null)
    val selectedPlaylistId: StateFlow<Long?> = _selectedPlaylistId.asStateFlow()

    val currentPlaylistDetails: StateFlow<PlaylistEntity?>
    val currentPlaylistTracks: StateFlow<List<TrackEntity>>

    // Vibe Roulette
    private val _rouletteResult = MutableStateFlow<VibeRouletteResult?>(null)
    val rouletteResult: StateFlow<VibeRouletteResult?> = _rouletteResult.asStateFlow()

    init {
        val database = VibeDatabase.getDatabase(application, viewModelScope)
        repository = VibeRepository(database.vibeDao())

        viewModelScope.launch {
            repository.ensureSeededIfEmpty()
        }

        allPlaylists = repository.allPlaylists.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

        allMovies = repository.allMovies.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

        allTracks = repository.allTracks.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

        vibeLogs = repository.allVibeLogs.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

        filteredPlaylists = combine(allPlaylists, playlistSearchQuery, selectedPlaylistVibeFilter) { list, query, vibe ->
            list.filter { p ->
                val matchesQuery = query.isBlank() ||
                        p.title.contains(query, ignoreCase = true) ||
                        p.description.contains(query, ignoreCase = true) ||
                        p.vibeTag.contains(query, ignoreCase = true)
                val matchesVibe = vibe == "All" || p.vibeTag.equals(vibe, ignoreCase = true)
                matchesQuery && matchesVibe
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

        filteredMovies = combine(allMovies, movieSearchQuery, selectedMovieStatusFilter, selectedMovieVibeFilter) { list, query, status, vibe ->
            list.filter { m ->
                val matchesQuery = query.isBlank() ||
                        m.title.contains(query, ignoreCase = true) ||
                        m.director.contains(query, ignoreCase = true) ||
                        m.genre.contains(query, ignoreCase = true) ||
                        m.vibeTag.contains(query, ignoreCase = true)
                val matchesStatus = when (status) {
                    "ALL" -> true
                    "FAVORITES" -> m.isFavorite
                    else -> m.watchStatus == status
                }
                val matchesVibe = vibe == "All" || m.vibeTag.equals(vibe, ignoreCase = true)
                matchesQuery && matchesStatus && matchesVibe
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

        currentPlaylistDetails = _selectedPlaylistId.flatMapLatest { id ->
            if (id != null) repository.getPlaylistById(id) else flowOf(null)
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

        currentPlaylistTracks = _selectedPlaylistId.flatMapLatest { id ->
            if (id != null) repository.getTracksForPlaylist(id) else flowOf(emptyList())
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    }

    // Playlist Actions
    fun selectPlaylist(id: Long?) {
        _selectedPlaylistId.value = id
    }

    fun addPlaylist(title: String, description: String, vibeTag: String, emoji: String, gradientIndex: Int) {
        viewModelScope.launch {
            val playlist = PlaylistEntity(
                title = title.ifBlank { "Untitled Vibe" },
                description = description,
                vibeTag = vibeTag.ifBlank { "Chill" },
                emoji = emoji.ifBlank { "✨" },
                gradientIndex = gradientIndex
            )
            val newId = repository.insertPlaylist(playlist)
            // Log to vibe diary
            repository.insertVibeLog(
                VibeLogEntity(
                    vibeName = playlist.vibeTag,
                    mediaType = "PLAYLIST",
                    mediaTitle = playlist.title,
                    moodNote = "Created playlist with ${playlist.emoji} vibe",
                    vibeEmoji = playlist.emoji
                )
            )
        }
    }

    fun updatePlaylist(playlist: PlaylistEntity) {
        viewModelScope.launch {
            repository.updatePlaylist(playlist)
        }
    }

    fun togglePlaylistFavorite(playlist: PlaylistEntity) {
        viewModelScope.launch {
            repository.updatePlaylist(playlist.copy(isFavorite = !playlist.isFavorite))
        }
    }

    fun deletePlaylist(playlist: PlaylistEntity) {
        viewModelScope.launch {
            if (_selectedPlaylistId.value == playlist.id) {
                _selectedPlaylistId.value = null
            }
            repository.deletePlaylist(playlist)
        }
    }

    // Track Actions
    fun addTrack(
        playlistId: Long,
        title: String,
        artist: String,
        albumOrYear: String,
        durationSeconds: Int,
        vibeTag: String,
        externalUrl: String,
        rating: Float
    ) {
        viewModelScope.launch {
            val currentTracks = currentPlaylistTracks.value
            val track = TrackEntity(
                playlistId = playlistId,
                title = title.ifBlank { "Track" },
                artist = artist.ifBlank { "Unknown Artist" },
                albumOrYear = albumOrYear,
                durationSeconds = durationSeconds,
                vibeTag = vibeTag,
                externalUrl = externalUrl,
                rating = rating,
                orderIndex = currentTracks.size
            )
            repository.insertTrack(track)
        }
    }

    fun toggleTrackFavorite(track: TrackEntity) {
        viewModelScope.launch {
            repository.updateTrack(track.copy(isFavorite = !track.isFavorite))
        }
    }

    fun deleteTrack(track: TrackEntity) {
        viewModelScope.launch {
            repository.deleteTrack(track)
        }
    }

    // Movie Actions
    fun addMovie(
        title: String,
        director: String,
        releaseYear: Int,
        genre: String,
        vibeTag: String,
        watchStatus: String,
        userRating: Float,
        whereToWatch: String,
        reviewOrNotes: String,
        pairedPlaylistId: Long?
    ) {
        viewModelScope.launch {
            val movie = MovieEntity(
                title = title.ifBlank { "Untitled Movie" },
                director = director,
                releaseYear = releaseYear,
                genre = genre.ifBlank { "Drama" },
                vibeTag = vibeTag.ifBlank { "Aesthetic" },
                watchStatus = watchStatus,
                userRating = userRating,
                whereToWatch = whereToWatch.ifBlank { "Streaming" },
                reviewOrNotes = reviewOrNotes,
                pairedPlaylistId = pairedPlaylistId,
                posterColorIndex = (0..5).random(),
                watchedDate = if (watchStatus == "WATCHED") System.currentTimeMillis() else null
            )
            repository.insertMovie(movie)
            // Log to vibe diary
            repository.insertVibeLog(
                VibeLogEntity(
                    vibeName = movie.vibeTag,
                    mediaType = "MOVIE",
                    mediaTitle = movie.title,
                    moodNote = if (movie.watchStatus == "WATCHED") "Watched & rated ${movie.userRating}★" else "Added to watchlist",
                    vibeEmoji = "🎬"
                )
            )
        }
    }

    fun updateMovie(movie: MovieEntity) {
        viewModelScope.launch {
            repository.updateMovie(movie)
        }
    }

    fun updateMovieStatus(movie: MovieEntity, newStatus: String) {
        viewModelScope.launch {
            val updated = movie.copy(
                watchStatus = newStatus,
                watchedDate = if (newStatus == "WATCHED" && movie.watchedDate == null) System.currentTimeMillis() else movie.watchedDate,
                rewatchCount = if (newStatus == "WATCHED" && movie.watchStatus == "WATCHED") movie.rewatchCount + 1 else movie.rewatchCount
            )
            repository.updateMovie(updated)
            if (newStatus == "WATCHED") {
                repository.insertVibeLog(
                    VibeLogEntity(
                        vibeName = movie.vibeTag,
                        mediaType = "MOVIE",
                        mediaTitle = movie.title,
                        moodNote = "Watched! Vibe: ${movie.vibeTag}",
                        vibeEmoji = "🍿"
                    )
                )
            }
        }
    }

    fun toggleMovieFavorite(movie: MovieEntity) {
        viewModelScope.launch {
            repository.updateMovie(movie.copy(isFavorite = !movie.isFavorite))
        }
    }

    fun deleteMovie(movie: MovieEntity) {
        viewModelScope.launch {
            repository.deleteMovie(movie)
        }
    }

    // Vibe Diary Log
    fun addMoodLog(vibeName: String, moodNote: String, emoji: String) {
        viewModelScope.launch {
            repository.insertVibeLog(
                VibeLogEntity(
                    vibeName = vibeName,
                    mediaType = "MOOD",
                    mediaTitle = "Personal Vibe Check",
                    moodNote = moodNote,
                    vibeEmoji = emoji
                )
            )
        }
    }

    fun deleteVibeLog(log: VibeLogEntity) {
        viewModelScope.launch {
            repository.deleteVibeLog(log)
        }
    }

    // Roulette / Vibe Matcher
    fun spinVibeRoulette(selectedVibe: String) {
        val playlists = allPlaylists.value
        val movies = allMovies.value

        val matchedP = playlists.filter { it.vibeTag.equals(selectedVibe, ignoreCase = true) }
            .shuffled().firstOrNull() ?: playlists.shuffled().firstOrNull()

        val matchedM = movies.filter { it.vibeTag.equals(selectedVibe, ignoreCase = true) }
            .shuffled().firstOrNull() ?: movies.shuffled().firstOrNull()

        val quotes = mapOf(
            "Midnight Drive" to "Empty highways and neon reflections. The world belongs to you tonight.",
            "Cozy & Chill" to "Steam rises from your warm mug. Unplug, breathe deep, and let time slow down.",
            "Cinematic" to "Cue the sweeping strings and dramatic light. You are the main character today.",
            "Cyberpunk" to "High tech, low life. Synthetic beats beneath rain-slicked skyscrapers.",
            "Golden Hour" to "Warm amber rays painting the wall. Pure nostalgia in the evening air.",
            "Mind Bending" to "Question reality. Every frame holds a puzzle waiting to unravel.",
            "High Adrenaline" to "Feel your pulse racing. Turn the volume to 11 and dive in.",
            "Emotional Rollercoaster" to "Tears, laughter, and catharsis. Allow yourself to feel it all."
        )

        _rouletteResult.value = VibeRouletteResult(
            vibeTag = selectedVibe,
            matchedPlaylist = matchedP,
            matchedMovie = matchedM,
            quote = quotes[selectedVibe] ?: "Tune into your inner frequency."
        )
    }

    fun clearRoulette() {
        _rouletteResult.value = null
    }

    // Ambient Sound Player controls
    fun toggleAmbient() {
        if (_isAmbientPlaying.value) {
            ambientPlayer.stop()
            _isAmbientPlaying.value = false
        } else {
            ambientPlayer.play(_currentAmbient.value, viewModelScope)
            _isAmbientPlaying.value = true
        }
    }

    fun switchAmbientSound(sound: AmbientSoundType) {
        _currentAmbient.value = sound
        if (_isAmbientPlaying.value) {
            ambientPlayer.play(sound, viewModelScope)
        }
    }

    fun setAmbientVolume(vol: Float) {
        _ambientVolume.value = vol
        ambientPlayer.volume = vol
    }

    override fun onCleared() {
        super.onCleared()
        ambientPlayer.stop()
    }
}
