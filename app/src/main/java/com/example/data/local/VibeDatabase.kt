package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        PlaylistEntity::class,
        TrackEntity::class,
        MovieEntity::class,
        VibeLogEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class VibeDatabase : RoomDatabase() {

    abstract fun vibeDao(): VibeDao

    companion object {
        @Volatile
        private var INSTANCE: VibeDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): VibeDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    VibeDatabase::class.java,
                    "vibe_database.db"
                )
                    .addCallback(VibeDatabaseCallback(scope))
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private class VibeDatabaseCallback(
            private val scope: CoroutineScope
        ) : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        populateInitialData(database.vibeDao())
                    }
                }
            }
        }

        suspend fun populateInitialData(dao: VibeDao) {
            // Seed Playlists
            val p1Id = dao.insertPlaylist(
                PlaylistEntity(
                    title = "Midnight Neon Drive",
                    description = "Synthwave, retrowave, and nocturnal city highway sounds for 2 AM contemplation.",
                    vibeTag = "Midnight Drive",
                    gradientIndex = 0,
                    emoji = "🌃",
                    isFavorite = true
                )
            )
            val p2Id = dao.insertPlaylist(
                PlaylistEntity(
                    title = "Rainy Café Lo-Fi",
                    description = "Warm coffee sips, gentle window rain, vinyl crackle, and acoustic Rhodes keys.",
                    vibeTag = "Cozy & Chill",
                    gradientIndex = 1,
                    emoji = "☕",
                    isFavorite = true
                )
            )
            val p3Id = dao.insertPlaylist(
                PlaylistEntity(
                    title = "Main Character Energy",
                    description = "Cinematic indie euphoria, sweeping reverb, and indie-pop anthems.",
                    vibeTag = "Cinematic",
                    gradientIndex = 2,
                    emoji = "✨",
                    isFavorite = false
                )
            )
            val p4Id = dao.insertPlaylist(
                PlaylistEntity(
                    title = "Cyberpunk 2077 Night",
                    description = "Heavy basslines, dark electro, dystopian gritty industrial pulses.",
                    vibeTag = "Cyberpunk",
                    gradientIndex = 3,
                    emoji = "⚡",
                    isFavorite = false
                )
            )
            val p5Id = dao.insertPlaylist(
                PlaylistEntity(
                    title = "Golden Hour Sunset",
                    description = "Warm dream pop, acoustic soul, and bittersweet golden sunlight.",
                    vibeTag = "Golden Hour",
                    gradientIndex = 4,
                    emoji = "🌅",
                    isFavorite = true
                )
            )

            // Seed Tracks
            dao.insertTracks(
                listOf(
                    TrackEntity(
                        playlistId = p1Id,
                        title = "Resonance",
                        artist = "HOME",
                        albumOrYear = "Odyssey (2014)",
                        durationSeconds = 212,
                        vibeTag = "Synthwave",
                        rating = 5.0f,
                        isFavorite = true,
                        orderIndex = 0
                    ),
                    TrackEntity(
                        playlistId = p1Id,
                        title = "Nightcall",
                        artist = "Kavinsky",
                        albumOrYear = "Drive OST (2010)",
                        durationSeconds = 259,
                        vibeTag = "Electro-Noir",
                        rating = 4.8f,
                        isFavorite = true,
                        orderIndex = 1
                    ),
                    TrackEntity(
                        playlistId = p1Id,
                        title = "After Dark",
                        artist = "Mr.Kitty",
                        albumOrYear = "Time (2014)",
                        durationSeconds = 257,
                        vibeTag = "Dark Wave",
                        rating = 4.7f,
                        isFavorite = false,
                        orderIndex = 2
                    ),
                    TrackEntity(
                        playlistId = p1Id,
                        title = "Days of Thunder",
                        artist = "The Midnight",
                        albumOrYear = "Days of Thunder (2016)",
                        durationSeconds = 328,
                        vibeTag = "Retrowave",
                        rating = 4.9f,
                        isFavorite = true,
                        orderIndex = 3
                    ),
                    // Rainy Cafe
                    TrackEntity(
                        playlistId = p2Id,
                        title = "Coffee Breath",
                        artist = "Sofia Mills",
                        albumOrYear = "Single (2019)",
                        durationSeconds = 176,
                        vibeTag = "Acoustic Cozy",
                        rating = 4.6f,
                        isFavorite = true,
                        orderIndex = 0
                    ),
                    TrackEntity(
                        playlistId = p2Id,
                        title = "Ylang Ylang",
                        artist = "FKJ",
                        albumOrYear = "Ylang Ylang EP (2019)",
                        durationSeconds = 213,
                        vibeTag = "Piano Lo-Fi",
                        rating = 5.0f,
                        isFavorite = true,
                        orderIndex = 1
                    ),
                    TrackEntity(
                        playlistId = p2Id,
                        title = "Get You The Moon",
                        artist = "Kina ft. Snow",
                        albumOrYear = "Single (2018)",
                        durationSeconds = 180,
                        vibeTag = "Sad Lo-Fi",
                        rating = 4.5f,
                        isFavorite = false,
                        orderIndex = 2
                    ),
                    // Main character
                    TrackEntity(
                        playlistId = p3Id,
                        title = "Midnight City",
                        artist = "M83",
                        albumOrYear = "Hurry Up, We're Dreaming (2011)",
                        durationSeconds = 243,
                        vibeTag = "Euphoric Synth",
                        rating = 5.0f,
                        isFavorite = true,
                        orderIndex = 0
                    ),
                    TrackEntity(
                        playlistId = p3Id,
                        title = "Space Song",
                        artist = "Beach House",
                        albumOrYear = "Depression Cherry (2015)",
                        durationSeconds = 320,
                        vibeTag = "Dream Pop",
                        rating = 4.9f,
                        isFavorite = true,
                        orderIndex = 1
                    ),
                    TrackEntity(
                        playlistId = p3Id,
                        title = "Heroes",
                        artist = "David Bowie",
                        albumOrYear = "\"Heroes\" (1977)",
                        durationSeconds = 367,
                        vibeTag = "Art Rock",
                        rating = 5.0f,
                        isFavorite = true,
                        orderIndex = 2
                    )
                )
            )

            // Seed Movies
            dao.insertMovies(
                listOf(
                    MovieEntity(
                        title = "Blade Runner 2049",
                        director = "Denis Villeneuve",
                        releaseYear = 2017,
                        genre = "Sci-Fi / Neo-Noir",
                        vibeTag = "Mind Bending",
                        watchStatus = "WATCHED",
                        userRating = 5.0f,
                        whereToWatch = "HBO Max",
                        reviewOrNotes = "Stunning holographic cinematography and crushing sound design. Peak atmospheric sci-fi.",
                        watchedDate = System.currentTimeMillis() - 86400000L * 5,
                        pairedPlaylistId = p1Id,
                        posterColorIndex = 0,
                        isFavorite = true,
                        rewatchCount = 3
                    ),
                    MovieEntity(
                        title = "Drive",
                        director = "Nicolas Winding Refn",
                        releaseYear = 2011,
                        genre = "Neo-Noir / Crime",
                        vibeTag = "Midnight Drive",
                        watchStatus = "WATCHED",
                        userRating = 4.8f,
                        whereToWatch = "Prime Video",
                        reviewOrNotes = "The quintessential night drive vibe film. Incredible soundtrack.",
                        watchedDate = System.currentTimeMillis() - 86400000L * 12,
                        pairedPlaylistId = p1Id,
                        posterColorIndex = 1,
                        isFavorite = true,
                        rewatchCount = 2
                    ),
                    MovieEntity(
                        title = "Spirited Away",
                        director = "Hayao Miyazaki",
                        releaseYear = 2001,
                        genre = "Animation / Fantasy",
                        vibeTag = "Cozy & Chill",
                        watchStatus = "WATCHED",
                        userRating = 5.0f,
                        whereToWatch = "Netflix",
                        reviewOrNotes = "Joe Hisaishi's piano soundtrack feels like returning to a childhood dream.",
                        watchedDate = System.currentTimeMillis() - 86400000L * 25,
                        pairedPlaylistId = p2Id,
                        posterColorIndex = 2,
                        isFavorite = true,
                        rewatchCount = 5
                    ),
                    MovieEntity(
                        title = "Past Lives",
                        director = "Celine Song",
                        releaseYear = 2023,
                        genre = "Romance / Drama",
                        vibeTag = "Emotional Rollercoaster",
                        watchStatus = "WATCHED",
                        userRating = 4.9f,
                        whereToWatch = "Showtime / Paramount+",
                        reviewOrNotes = "Poignant exploration of In-Yun, nostalgia, and choices left unmade.",
                        watchedDate = System.currentTimeMillis() - 86400000L * 40,
                        pairedPlaylistId = p5Id,
                        posterColorIndex = 3,
                        isFavorite = true,
                        rewatchCount = 1
                    ),
                    MovieEntity(
                        title = "Dune: Part Two",
                        director = "Denis Villeneuve",
                        releaseYear = 2024,
                        genre = "Sci-Fi / Epic",
                        vibeTag = "High Adrenaline",
                        watchStatus = "WATCHED",
                        userRating = 4.9f,
                        whereToWatch = "HBO Max",
                        reviewOrNotes = "Epic sand dunes, visceral sound mixing, unmatched scale.",
                        watchedDate = System.currentTimeMillis() - 86400000L * 60,
                        pairedPlaylistId = p4Id,
                        posterColorIndex = 4,
                        isFavorite = false,
                        rewatchCount = 1
                    ),
                    MovieEntity(
                        title = "Severance",
                        director = "Ben Stiller",
                        releaseYear = 2022,
                        genre = "Psychological Thriller",
                        vibeTag = "Mind Bending",
                        watchStatus = "WATCHING",
                        userRating = 4.8f,
                        whereToWatch = "Apple TV+",
                        reviewOrNotes = "Eerie liminal office spaces and nail-biting tension.",
                        watchedDate = null,
                        pairedPlaylistId = null,
                        posterColorIndex = 0,
                        isFavorite = true,
                        rewatchCount = 0
                    ),
                    MovieEntity(
                        title = "Amélie",
                        director = "Jean-Pierre Jeunet",
                        releaseYear = 2001,
                        genre = "Romantic Comedy / Whimsical",
                        vibeTag = "Aesthetic",
                        watchStatus = "WANT_TO_WATCH",
                        userRating = 4.6f,
                        whereToWatch = "Criterion Channel",
                        reviewOrNotes = "Vibrant Parisian color palette and Yann Tiersen accordion score.",
                        watchedDate = null,
                        pairedPlaylistId = p2Id,
                        posterColorIndex = 1,
                        isFavorite = false,
                        rewatchCount = 0
                    ),
                    MovieEntity(
                        title = "Interstellar",
                        director = "Christopher Nolan",
                        releaseYear = 2014,
                        genre = "Sci-Fi / Adventure",
                        vibeTag = "Cinematic",
                        watchStatus = "WATCHED",
                        userRating = 5.0f,
                        whereToWatch = "Paramount+",
                        reviewOrNotes = "The church organ soundtrack alone transports you across galaxies.",
                        watchedDate = System.currentTimeMillis() - 86400000L * 100,
                        pairedPlaylistId = p3Id,
                        posterColorIndex = 2,
                        isFavorite = true,
                        rewatchCount = 4
                    )
                )
            )

            // Seed Vibe Logs
            dao.insertVibeLog(
                VibeLogEntity(
                    vibeName = "Midnight Drive",
                    mediaType = "PLAYLIST",
                    mediaTitle = "Midnight Neon Drive",
                    moodNote = "Cruise down the quiet empty highway with windows slightly cracked.",
                    vibeEmoji = "🌃"
                )
            )
            dao.insertVibeLog(
                VibeLogEntity(
                    vibeName = "Mind Bending",
                    mediaType = "MOVIE",
                    mediaTitle = "Blade Runner 2049",
                    moodNote = "Watched in dark room with headphones. Pure immersion.",
                    vibeEmoji = "🔮"
                )
            )
        }
    }
}
