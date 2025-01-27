package com.example.playlistmaker.domain

import com.example.playlistmaker.domain.search.model.Track
import kotlinx.coroutines.flow.Flow

interface FavoriteRepository {
    fun getFavoriteTracks(): Flow<List<Track>>
    fun getIdFavoriteTracks(): Flow<List<Long>>

}