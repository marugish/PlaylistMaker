package com.example.playlistmaker.domain.db

import com.example.playlistmaker.domain.db.model.Playlist
import com.example.playlistmaker.domain.search.model.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistInteractor {
    fun insertNewPlaylist(playlist: Playlist): Flow<Long>
    fun getPlaylists(): Flow<List<Playlist>>
    suspend fun updatePlaylistInfoTracks(id: Long, trackIds: String, count: Int)
    suspend fun insertTrackInPlaylist(track: Track)
    fun getPlaylistById(id: Long): Flow<Playlist>
    fun getTracksInPlaylist(trackIds: List<Long>): Flow<List<Track>>
}