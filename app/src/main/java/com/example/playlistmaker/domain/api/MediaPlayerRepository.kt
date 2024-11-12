package com.example.playlistmaker.domain.api

import com.example.playlistmaker.util.PlayerStates

interface MediaPlayerRepository {
    fun prepare(url: String)
    fun start()
    fun pause()
    fun release()
    fun getCurrentPosition(): Int
    fun getCurrentPlayerState(): PlayerStates
    fun changeState(state: PlayerStates)
}