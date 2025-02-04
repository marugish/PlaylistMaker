package com.example.playlistmaker.domain.db

import com.example.playlistmaker.domain.db.model.Playlist
import com.example.playlistmaker.domain.search.model.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistInteractor {
    suspend fun insertNewPlaylist(playlist: Playlist)
    fun getPlaylists(): Flow<List<Playlist>>
    suspend fun updatePlaylistInfoTracks(id: Long, trackIds: String, count: Int)
    suspend fun insertTrackInPlaylist(track: Track)
    //fun tracksInPlaylist(id: Long): Flow<List<Long>>
}