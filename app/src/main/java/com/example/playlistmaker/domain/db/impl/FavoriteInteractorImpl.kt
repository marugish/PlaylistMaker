package com.example.playlistmaker.domain.db.impl

import com.example.playlistmaker.domain.db.FavoriteInteractor
import com.example.playlistmaker.domain.db.FavoriteRepository
import com.example.playlistmaker.domain.search.model.Track
import kotlinx.coroutines.flow.Flow

class FavoriteInteractorImpl(private val favoriteRepository: FavoriteRepository): FavoriteInteractor {

    override fun getFavoriteTracks(): Flow<List<Track>> {
        return favoriteRepository.getFavoriteTracks()
    }

    override suspend fun insertFavoriteTrack(track: Track) {
        favoriteRepository.insertFavoriteTrack(track)
    }

    override suspend fun deleteFavoriteTrack(track: Track) {
        favoriteRepository.deleteFavoriteTrack(track)
    }

    override fun getIdFavoriteTracks(): Flow<List<Long>> {
        return favoriteRepository.getIdFavoriteTracks()
    }

}