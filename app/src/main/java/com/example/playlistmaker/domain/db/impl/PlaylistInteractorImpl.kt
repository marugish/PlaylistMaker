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

}