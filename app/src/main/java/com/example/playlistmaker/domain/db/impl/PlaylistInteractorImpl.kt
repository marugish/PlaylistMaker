package com.example.playlistmaker.domain.db.impl

import com.example.playlistmaker.domain.db.PlaylistInteractor
import com.example.playlistmaker.domain.db.PlaylistRepository
import com.example.playlistmaker.domain.db.model.Playlist
import com.example.playlistmaker.domain.search.model.Track
import kotlinx.coroutines.flow.Flow

class PlaylistInteractorImpl(private val playlistRepository: PlaylistRepository): PlaylistInteractor {
    override fun insertNewPlaylist(playlist: Playlist): Flow<Long> {
        return playlistRepository.insertNewPlaylist(playlist)
    }

    override fun getPlaylists(): Flow<List<Playlist>> {
        return playlistRepository.getPlaylists()
    }

    override suspend fun updatePlaylistInfoTracks(id: Long, trackIds: String, count: Int) {
        playlistRepository.updatePlaylistInfoTracks(id, trackIds, count)
    }

    override suspend fun insertTrackInPlaylist(track: Track) {
        playlistRepository.insertTrackInPlaylist(track)
    }

    override fun getPlaylistById(id: Long): Flow<Playlist> {
        return playlistRepository.getPlaylistById(id)
    }

    override fun getTracksInPlaylist(trackIds: List<Long>): Flow<List<Track>> {
        return playlistRepository.getTracksInPlaylist(trackIds)
    }

    override suspend fun updatePlaylistInfo(id: Long, name: String, description: String?, photo: String?) {
        playlistRepository.updatePlaylistInfo(id, name, description, photo)
    }

    override suspend fun deletePlaylistById(id: Long) {
        playlistRepository.deletePlaylistById(id)
    }

    override suspend fun insertRecord(idPlaylist: Long, idTrack: Long) {
        playlistRepository.insertRecord(idPlaylist, idTrack)
    }

    override suspend fun deleteRecord(idPlaylist: Long, idTrack: Long) {
        playlistRepository.deleteRecord(idPlaylist, idTrack)
    }

}