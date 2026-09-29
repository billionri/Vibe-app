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
    val category: TaskCategory = TaskCategory.DAY_TO_DAY,
    val minutesCompleted: Int,
    val timestamp: Long = System.currentTimeMillis()
)

enum class TaskCategory(val displayName: String, val emoji: String) {
    ALL("All", "⚡"),
    DAY_TO_DAY("Daily Tasks", "☀️"),
    WORK("Work & Projects", "💼"),
    PLANNING("Planning", "🗓️"),
    STUDY("Study & Learn", "📚"),
    CHORES("Chores & Home", "🧹"),
    HEALTH("Health & Habits", "🌱"),
    CREATIVE("Creative", "🎨")
}

enum class TaskPriority(val label: String, val colorHex: Long) {
    HIGH("High", 0xFFEF4444),
    MEDIUM("Med", 0xFFF59E0B),
    LOW("Low", 0xFF10B981)
}

data class DayTask(
    val id: String,
    val title: String,
    val category: TaskCategory = TaskCategory.DAY_TO_DAY,
    val priority: TaskPriority = TaskPriority.MEDIUM,
    val isCompleted: Boolean = false,
    val scheduledTime: String = "Today",
    val estimatedMinutes: Int = 25,
    val completedMinutes: Int = 0,
    val notes: String = "",
    val createdAt: Long = System.currentTimeMillis()
)

data class DayPlannerBlock(
    val id: String,
    val period: String,
    val title: String,
    val tasksDescription: String,
    val isCompleted: Boolean = false,
    val iconEmoji: String = "🌅"
)

data class DailyGoal(
    val id: String,
    val title: String,
    val isAchieved: Boolean = false
)

data class DailyHabit(
    val id: String,
    val title: String,
    val emoji: String,
    val streak: Int = 3,
    val isDoneToday: Boolean = false
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
