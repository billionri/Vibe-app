package com.example.data.local

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "playlists")
data class PlaylistEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val description: String,
    val vibeTag: String,
    val gradientIndex: Int = 0,
    val emoji: String = "🎵",
    val isFavorite: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(
    tableName = "tracks",
    foreignKeys = [
        ForeignKey(
            entity = PlaylistEntity::class,
            parentColumns = ["id"],
            childColumns = ["playlistId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["playlistId"])]
)
data class TrackEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val playlistId: Long,
    val title: String,
    val artist: String,
    val albumOrYear: String = "",
    val durationSeconds: Int = 180,
    val vibeTag: String = "",
    val externalUrl: String = "",
    val rating: Float = 4.5f,
    val isFavorite: Boolean = false,
    val orderIndex: Int = 0
)

@Entity(tableName = "movies")
data class MovieEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val director: String = "",
    val releaseYear: Int = 2024,
    val genre: String = "Drama",
    val vibeTag: String = "Aesthetic",
    val watchStatus: String = "WANT_TO_WATCH", // WANT_TO_WATCH, WATCHING, WATCHED
    val userRating: Float = 0f, // 0.0 to 5.0
    val whereToWatch: String = "Netflix",
    val reviewOrNotes: String = "",
    val watchedDate: Long? = null,
    val pairedPlaylistId: Long? = null,
    val posterColorIndex: Int = 0,
    val isFavorite: Boolean = false,
    val rewatchCount: Int = 0,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "vibe_logs")
data class VibeLogEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val timestamp: Long = System.currentTimeMillis(),
    val vibeName: String,
    val mediaType: String, // "PLAYLIST", "TRACK", "MOVIE", "MOOD"
    val mediaTitle: String,
    val moodNote: String = "",
    val vibeEmoji: String = "✨"
)
