package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.PlayerStates
import com.example.playlistmaker.domain.api.MediaPlayerInteractor
import com.example.playlistmaker.domain.api.MediaPlayerRepository

class MediaPlayerInteractorImpl(private val repository: MediaPlayerRepository): MediaPlayerInteractor {
    override fun prepare(url: String) {
        repository.prepare(url)
    }

    override fun play() {
        repository.start()
    }

    override fun pause() {
        repository.pause()
    }

    override fun release() {
        repository.release()
    }

    override fun getCurrentStateAndPosition(consumer: MediaPlayerInteractor.MediaPlayerConsumer) {
        consumer.consumeCurrentStateAndPosition(repository.getCurrentPosition(),repository.getCurrentPlayerState())
    }

    override fun changeState(state: PlayerStates) {
        repository.changeState(state)
    }

}