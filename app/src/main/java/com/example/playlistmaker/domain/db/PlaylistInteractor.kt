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
    suspend fun updatePlaylistInfo(id: Long, name: String, description: String?, photo: String?)
    suspend fun deletePlaylistById(id: Long)
    suspend fun deleteTrackInfo(idTrack: Long)

    suspend fun insertRecord(idPlaylist: Long, idTrack: Long)
    suspend fun deleteRecord(idPlaylist: Long, idTrack: Long)
    fun findTrack(idTrack: Long): Flow<Int>
    suspend fun deleteRecordByPlaylistId(idPlaylist: Long)
}