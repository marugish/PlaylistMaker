package com.example.playlistmaker.domain.api

interface MediaPlayerInteractor {
    fun prepare(url: String)
    fun play()
    fun pause()
    fun release()

    //fun getCurrentPosition(consumer: MediaPlayerConsumer)
    //fun getCurrentStatePlayer(consumer: MediaPlayerConsumer)
    fun getCurrentStateAndPosition(consumer: MediaPlayerConsumer)
    fun changeState(state: Int)

    // Consumer
    interface MediaPlayerConsumer {
        //fun consumeCurrentPosition(position: Int)
        //fun consumeCurrentState(state: Int)
        fun consumeCurrentStateAndPosition(position: Int, state: Int)
    }
}