package com.example.playlistmaker.domain.db

import com.example.playlistmaker.domain.search.model.Track
import kotlinx.coroutines.flow.Flow

interface FavoriteRepository {
    fun getFavoriteTracks(): Flow<List<Track>>
    suspend fun insertFavoriteTrack(track: Track)
    suspend fun deleteFavoriteTrack(track: Track)
    fun getIdFavoriteTracks(): Flow<List<Long>>
}