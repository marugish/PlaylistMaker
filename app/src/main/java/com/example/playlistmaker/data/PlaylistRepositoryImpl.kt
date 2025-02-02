package com.example.playlistmaker.data

import com.example.playlistmaker.data.db.AppDatabase
import com.example.playlistmaker.data.db.converter.PlaylistDbConvertor
import com.example.playlistmaker.data.db.entity.PlaylistEntity
import com.example.playlistmaker.data.db.entity.TrackEntity
import com.example.playlistmaker.data.search.mapper.TracksMapper
import com.example.playlistmaker.domain.db.PlaylistRepository
import com.example.playlistmaker.domain.db.model.Playlist
import com.example.playlistmaker.domain.search.model.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PlaylistRepositoryImpl(private val appDatabase: AppDatabase,
                             private val playlistDbConvertor: PlaylistDbConvertor
): PlaylistRepository {
    // Insert New Playlist
    override suspend fun insertNewPlaylist(playlist: Playlist) {
        val playlistEntity = playlistDbConvertor.map(playlist)
        appDatabase.playlistDao().insertNewPlaylist(playlistEntity)
    }

    // Get Playlists
    override fun getPlaylists(): Flow<List<Playlist>> = flow {
        val playlists = appDatabase.playlistDao().getPlaylists()
        emit(convertFromPlaylistEntity(playlists))
    }

    private fun convertFromPlaylistEntity(playlists: List<PlaylistEntity>): List<Playlist> {
        return playlists.map { playlist -> playlistDbConvertor.map(playlist) }
    }
}