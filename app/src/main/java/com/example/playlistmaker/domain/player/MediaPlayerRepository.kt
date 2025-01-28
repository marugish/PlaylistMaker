package com.example.playlistmaker.domain.player

import com.example.playlistmaker.data.search.dto.TrackDto
import com.example.playlistmaker.domain.search.model.Track
import com.example.playlistmaker.util.PlayerStates

interface MediaPlayerRepository {
    fun prepare(url: String)
    fun start()
    fun pause()
    fun release()
    fun getCurrentPosition(): Int
    fun getCurrentPlayerState(): PlayerStates
    fun changeState(state: PlayerStates)

    // пока не уверена
    suspend fun insertFavoriteTrack(track: Track)
    suspend fun deleteFavoriteTrack(track: Track)
}