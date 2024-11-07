package com.example.playlistmaker.data

import com.example.playlistmaker.PlayerStates

interface MediaPlayerInterface {
    fun prepare(url: String)
    fun start()
    fun pause()
    fun release()
    fun getCurrentPosition(): Int
    fun getCurrentPlayerState(): PlayerStates
    fun changeState(state: PlayerStates)
}