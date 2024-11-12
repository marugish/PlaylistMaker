package com.example.playlistmaker.domain.api

import com.example.playlistmaker.util.PlayerStates

interface MediaPlayerInteractor {
    fun prepare(url: String)
    fun play()
    fun pause()
    fun release()
    fun getCurrentStateAndPosition(consumer: MediaPlayerConsumer)
    fun changeState(state: PlayerStates)

    fun interface MediaPlayerConsumer {
        fun consumeCurrentStateAndPosition(position: Int, state: PlayerStates)
    }
}