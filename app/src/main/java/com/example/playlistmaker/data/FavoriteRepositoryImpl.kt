package com.example.playlistmaker.data

import com.example.playlistmaker.data.db.AppDatabase
import com.example.playlistmaker.data.db.converter.TrackDbConvertor
import com.example.playlistmaker.data.db.entity.TrackEntity
import com.example.playlistmaker.data.search.mapper.TracksMapper
import com.example.playlistmaker.domain.db.FavoriteRepository
import com.example.playlistmaker.domain.search.model.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FavoriteRepositoryImpl(private val appDatabase: AppDatabase,
                             private val trackDbConvertor: TrackDbConvertor
): FavoriteRepository {
    override fun getFavoriteTracks(): Flow<List<Track>> = flow {
        val tracks = appDatabase.trackDao().getFavoriteTracks()
        emit(convertFromTrackEntity(tracks))
    }

    // Insert Favorite Track
    override suspend fun insertFavoriteTrack(track: Track) {
        val trackEntity = trackDbConvertor.map(TracksMapper.mapToStorage(track))
        appDatabase.trackDao().insertFavoriteTrack(trackEntity)
    }

    // Delete Favorite Track
    override suspend fun deleteFavoriteTrack(track: Track) {
        val trackEntity = trackDbConvertor.map(TracksMapper.mapToStorage(track))
        appDatabase.trackDao().deleteFavoriteTrack(trackEntity)
    }

    override fun getIdFavoriteTracks(): Flow<List<Long>> = flow {
        val tracks = appDatabase.trackDao().getIdFavoriteTracks()
        emit(tracks)
    }

    private fun convertFromTrackEntity(tracks: List<TrackEntity>): List<Track> {
        return tracks.map { track -> trackDbConvertor.map(track) }
    }

}