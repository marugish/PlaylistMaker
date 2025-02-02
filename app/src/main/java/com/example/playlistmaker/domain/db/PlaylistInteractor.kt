package com.example.playlistmaker.domain.db

import com.example.playlistmaker.domain.db.model.Playlist
import kotlinx.coroutines.flow.Flow

interface PlaylistInteractor {
    suspend fun insertNewPlaylist(playlist: Playlist)
    fun getPlaylists(): Flow<List<Playlist>>
}