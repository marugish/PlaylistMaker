package com.example.playlistmaker.data

import android.util.Log
import com.example.playlistmaker.data.db.AppDatabase
import com.example.playlistmaker.data.db.converter.PlaylistDbConvertor
import com.example.playlistmaker.data.db.converter.TrackInPlaylistConvertor
import com.example.playlistmaker.data.db.entity.IntermediateEntity
import com.example.playlistmaker.data.db.entity.PlaylistEntity
import com.example.playlistmaker.data.db.entity.TrackInPlaylistEntity
import com.example.playlistmaker.domain.db.PlaylistRepository
import com.example.playlistmaker.domain.db.model.Playlist
import com.example.playlistmaker.domain.search.model.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class PlaylistRepositoryImpl(private val appDatabase: AppDatabase,
                             private val playlistDbConvertor: PlaylistDbConvertor,
                             private val trackInPlaylistConvertor: TrackInPlaylistConvertor
): PlaylistRepository {
    // Insert New Playlist
    override suspend fun insertNewPlaylist(playlist: Playlist): Long {
        val playlistEntity = playlistDbConvertor.map(playlist)
        return appDatabase.playlistDao().insertNewPlaylist(playlistEntity)
    }

    // Get Playlists
    override fun getPlaylists(): Flow<List<Playlist>> {
        return appDatabase.playlistDao().getPlaylists()
            .map { playlists -> convertFromPlaylistEntityList(playlists) }
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
        val entity = appDatabase.playlistDao().getPlaylistById(id)
        emit(convertFromPlaylistEntity(entity))
    }

    override fun getTracksInPlaylist(trackIds: List<Long>): Flow<List<Track>> {
        return appDatabase.trackInPlaylistDao().getTracks()
            .map { allTracks ->
                val filteredTracks = allTracks.filter { track -> trackIds.contains(track.trackId) }
                val orderedTracks = trackIds.mapNotNull { trackId ->
                    filteredTracks.find { track -> track.trackId == trackId }
                }
                convertFromTrackInPlaylistEntity(orderedTracks)
            }
    }

    override suspend fun updatePlaylistInfo(id: Long, name: String, description: String?, photo: String?) {
        appDatabase.playlistDao().updatePlaylistInfo(id, name, description, photo)
    }

    override suspend fun deletePlaylistById(id: Long) {
        appDatabase.playlistDao().deletePlaylistById(id)
    }

    override suspend fun insertRecord(idPlaylist: Long, idTrack: Long) {
        val intermediateEntity = IntermediateEntity(playlistId = idPlaylist, trackId = idTrack)
        appDatabase.intermediateDao().insertRecord(intermediateEntity)
    }

    override suspend fun deleteRecord(idPlaylist: Long, idTrack: Long) {
        val intermediateEntity = IntermediateEntity(playlistId = idPlaylist, trackId = idTrack)
        appDatabase.intermediateDao().deleteRecord(intermediateEntity)
    }

    override suspend fun deleteTrackInfoIfNotPresent(trackId: Long) {
        val count = appDatabase.intermediateDao().findTrack(trackId).first() // Получаем первое значение
        if (count == 0) {
            appDatabase.trackInPlaylistDao().deleteRecord(trackId)
        }
    }

    override suspend fun deleteRecordByPlaylistId(idPlaylist: Long) {
        appDatabase.intermediateDao().deleteRecordByPlaylistId(idPlaylist)
    }

    private fun convertFromTrackInPlaylistEntity(tracks: List<TrackInPlaylistEntity>): List<Track> {
        return tracks.map { track ->  trackInPlaylistConvertor.map(track) }
    }

    private fun convertFromPlaylistEntity(playlist: PlaylistEntity): Playlist {
        return playlistDbConvertor.map(playlist)
    }

    private fun convertFromPlaylistEntityList(playlists: List<PlaylistEntity>): List<Playlist> {
        return playlists.map { playlist -> playlistDbConvertor.map(playlist) }
    }
}