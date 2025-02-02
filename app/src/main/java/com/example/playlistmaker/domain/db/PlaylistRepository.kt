package com.example.playlistmaker.domain.db

import com.example.playlistmaker.domain.db.model.Playlist
import com.example.playlistmaker.domain.search.model.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistRepository {
    suspend fun insertNewPlaylist(playlist: Playlist)
    fun getPlaylists(): Flow<List<Playlist>>
}