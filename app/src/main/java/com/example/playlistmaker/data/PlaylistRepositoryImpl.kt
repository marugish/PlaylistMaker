package com.example.playlistmaker.data

import com.example.playlistmaker.data.db.AppDatabase
import com.example.playlistmaker.data.db.converter.PlaylistDbConvertor
import com.example.playlistmaker.data.db.entity.PlaylistEntity
import com.example.playlistmaker.domain.db.PlaylistRepository
import com.example.playlistmaker.domain.db.model.Playlist
import com.example.playlistmaker.domain.search.model.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PlaylistRepositoryImpl(private val appDatabase: AppDatabase,
                             private val playlistDbConvertor: PlaylistDbConvertor
): PlaylistRepository {
    // Insert New Playlist
    override fun insertNewPlaylist(playlist: Playlist): Flow<Long> = flow {
        val playlistEntity = playlistDbConvertor.map(playlist)
        emit(appDatabase.playlistDao().insertNewPlaylist(playlistEntity))
    }

    // Get Playlists
    override fun getPlaylists(): Flow<List<Playlist>> = flow {
        val playlists = appDatabase.playlistDao().getPlaylists()
        emit(convertFromPlaylistEntityList(playlists))
    }

    // Save track in Playlist
    override suspend fun updatePlaylistInfoTracks(id: Long, trackIds: String, count: Int) {
        appDatabase.playlistDao().updatePlaylistTrackIdsAndCount(id, trackIds, count)
    }

    override suspend fun insertTrackInPlaylist(track: Track) {
        val trackInPlaylistEntity = playlistDbConvertor.map(track)
        appDatabase.trackInPlaylistDao().insertTrackInPlaylist(trackInPlaylistEntity)
    }

    override fun getPlaylistById(id: Long): Flow<Playlist> = flow {
        val playlist = appDatabase.playlistDao().getPlaylistById(id)
        emit(convertFromPlaylistEntity(playlist))
    }

    private fun convertFromPlaylistEntity(playlist: PlaylistEntity): Playlist {
        return playlistDbConvertor.map(playlist)
    }

    private fun convertFromPlaylistEntityList(playlists: List<PlaylistEntity>): List<Playlist> {
        return playlists.map { playlist -> playlistDbConvertor.map(playlist) }
    }
}