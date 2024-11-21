package com.example.playlistmaker.data.player

import com.example.playlistmaker.util.PlayerStates

interface MediaPlayerInterface {
    fun prepare(url: String)
    fun start()
    fun pause()
    fun release()
    fun getCurrentPosition(): Int
    fun getCurrentPlayerState(): PlayerStates
    fun changeState(state: PlayerStates)
}