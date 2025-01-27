package com.example.playlistmaker.domain

import com.example.playlistmaker.domain.search.model.Track
import kotlinx.coroutines.flow.Flow

class FavoriteInteractorImpl(private val favoriteRepository: FavoriteRepository): FavoriteInteractor {
    override fun getFavoriteTracks(): Flow<List<Track>> {
        return favoriteRepository.getFavoriteTracks()
    }

    override fun getIdFavoriteTracks(): Flow<List<Long>> {
        return favoriteRepository.getIdFavoriteTracks()
    }

}