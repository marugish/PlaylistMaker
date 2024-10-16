package com.example.playlistmaker.domain.api

interface MediaPlayerInteractor {
    fun prepare(url: String)
    fun play()
    fun pause()
    fun release()
    fun getCurrentStateAndPosition(consumer: MediaPlayerConsumer)
    fun changeState(state: Int)

    interface MediaPlayerConsumer {
        fun consumeCurrentStateAndPosition(position: Int, state: Int)
    }
}