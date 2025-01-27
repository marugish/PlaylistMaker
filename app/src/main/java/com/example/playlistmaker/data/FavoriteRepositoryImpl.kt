package com.example.playlistmaker.data

import com.example.playlistmaker.data.db.AppDatabase
import com.example.playlistmaker.data.db.TrackDbConvertor
import com.example.playlistmaker.data.db.entity.TrackEntity
import com.example.playlistmaker.domain.FavoriteRepository
import com.example.playlistmaker.domain.search.model.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FavoriteRepositoryImpl(private val appDatabase: AppDatabase,
                             private val trackDbConvertor: TrackDbConvertor): FavoriteRepository {
    override fun getFavoriteTracks(): Flow<List<Track>> = flow {
        val tracks = appDatabase.trackDao().getFavoriteTracks()
        emit(convertFromTrackEntity(tracks))
    }

    // пока не уверена в возвращаемом значении
    override fun getIdFavoriteTracks(): Flow<List<Long>> = flow {
        val tracks = appDatabase.trackDao().getIdFavoriteTracks()
        emit(tracks)
    }

    private fun convertFromTrackEntity(tracks: List<TrackEntity>): List<Track> {
        return tracks.map { track -> trackDbConvertor.map(track) }
    }


}