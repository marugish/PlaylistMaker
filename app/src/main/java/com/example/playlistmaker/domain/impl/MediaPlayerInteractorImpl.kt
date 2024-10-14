package com.example.playlistmaker.domain.impl

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

    //override fun getCurrentPosition(consumer: MediaPlayerInteractor.MediaPlayerConsumer) {
        //consumer.consumeCurrentPosition(repository.getCurrentPosition())
    //}

    //override fun getCurrentStatePlayer(consumer: MediaPlayerInteractor.MediaPlayerConsumer) {
        //consumer.consumeCurrentState(repository.getCurrentPlayerState())
    //}

    override fun getCurrentStateAndPosition(consumer: MediaPlayerInteractor.MediaPlayerConsumer) {
        //Log.d("player", "MediaPlayerInteractorImpl -> getCurrentStateAndPosition()")
        consumer.consumeCurrentStateAndPosition(repository.getCurrentPosition(),repository.getCurrentPlayerState())
    }

    override fun changeState(state: Int) {
        repository.changeState(state)
    }

}