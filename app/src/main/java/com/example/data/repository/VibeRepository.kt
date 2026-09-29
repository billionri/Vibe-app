package com.example.data.repository

import com.example.data.local.MovieEntity
import com.example.data.local.PlaylistEntity
import com.example.data.local.TrackEntity
import com.example.data.local.VibeDao
import com.example.data.local.VibeDatabase
import com.example.data.local.VibeLogEntity
import kotlinx.coroutines.flow.Flow

class VibeRepository(private val dao: VibeDao) {

    val allPlaylists: Flow<List<PlaylistEntity>> = dao.getAllPlaylists()
    val allMovies: Flow<List<MovieEntity>> = dao.getAllMovies()
    val allTracks: Flow<List<TrackEntity>> = dao.getAllTracks()
    val favoriteTracks: Flow<List<TrackEntity>> = dao.getFavoriteTracks()
    val favoriteMovies: Flow<List<MovieEntity>> = dao.getFavoriteMovies()
    val allVibeLogs: Flow<List<VibeLogEntity>> = dao.getAllVibeLogs()

    suspend fun ensureSeededIfEmpty() {
        if (dao.getPlaylistCount() == 0) {
            VibeDatabase.populateInitialData(dao)
        }
    }

    fun getPlaylistById(id: Long): Flow<PlaylistEntity?> = dao.getPlaylistById(id)
    fun getTracksForPlaylist(playlistId: Long): Flow<List<TrackEntity>> = dao.getTracksForPlaylist(playlistId)
    fun getMoviesByStatus(status: String): Flow<List<MovieEntity>> = dao.getMoviesByStatus(status)
    fun searchPlaylists(query: String): Flow<List<PlaylistEntity>> = dao.searchPlaylists(query)
    fun searchMovies(query: String): Flow<List<MovieEntity>> = dao.searchMovies(query)

    suspend fun insertPlaylist(playlist: PlaylistEntity): Long = dao.insertPlaylist(playlist)
    suspend fun updatePlaylist(playlist: PlaylistEntity) = dao.updatePlaylist(playlist)
    suspend fun deletePlaylist(playlist: PlaylistEntity) = dao.deletePlaylist(playlist)

    suspend fun insertTrack(track: TrackEntity): Long = dao.insertTrack(track)
    suspend fun updateTrack(track: TrackEntity) = dao.updateTrack(track)
    suspend fun deleteTrack(track: TrackEntity) = dao.deleteTrack(track)

    suspend fun insertMovie(movie: MovieEntity): Long = dao.insertMovie(movie)
    suspend fun updateMovie(movie: MovieEntity) = dao.updateMovie(movie)
    suspend fun deleteMovie(movie: MovieEntity) = dao.deleteMovie(movie)

    suspend fun insertVibeLog(vibeLog: VibeLogEntity): Long = dao.insertVibeLog(vibeLog)
    suspend fun deleteVibeLog(vibeLog: VibeLogEntity) = dao.deleteVibeLog(vibeLog)
}
