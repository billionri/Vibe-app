package com.example.model

data class TrackItem(
    val id: String,
    val title: String,
    val channel: String = "VIBE Music",
    val thumb: String = "https://img.youtube.com/vi/$id/mqdefault.jpg",
    val originalUrl: String = "https://www.youtube.com/watch?v=$id",
    val labels: List<String> = emptyList(),
    val durationSec: Int = 210,
    val addedAt: Long = System.currentTimeMillis()
)

data class StoreMovie(
    val id: String,
    val title: String,
    val year: String,
    val genre: String,
    val collection: String, // "Marvel", "DC", "Anime", "Bollywood", "Hollywood", "Sci-Fi"
    val emoji: String,
    val rating: String,
    val toy: String,
    val isWatched: Boolean = false
)

data class KaraokeRecording(
    val id: String,
    val songTitle: String,
    val durationSec: Int,
    val timestamp: Long = System.currentTimeMillis()
)

data class TheatrePlan(
    val id: String,
    val movieTitle: String,
    val dateTime: String,
    val snacks: String = "Popcorn & Cola",
    val friends: List<String> = listOf("You", "Rahul", "Priya"),
    val totalCost: Int = 600
) {
    val costPerPerson: Int
        get() = if (friends.isNotEmpty()) totalCost / friends.size else totalCost
}

data class ReelItem(
    val id: String,
    val title: String,
    val creator: String,
    val sound: String,
    val likes: String,
    val emoji: String
)

data class StudySession(
    val id: String,
    val title: String,
    val minutesCompleted: Int,
    val timestamp: Long = System.currentTimeMillis()
)

data class MerchProduct(
    val id: String,
    val name: String,
    val desc: String,
    val price: Int,
    val originalPrice: Int,
    val emoji: String,
    val category: String, // "audio", "clothing", "fitness", "accessories"
    val badge: String? = null
)

data class VinylRecord(
    val id: String,
    val album: String,
    val artist: String,
    val year: String,
    val price: Int,
    val emoji: String,
    val genre: String
)

data class Coupon(
    val code: String,
    val discount: String,
    val desc: String
)
