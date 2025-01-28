package com.example.playlistmaker.domain.player.impl

import com.example.playlistmaker.domain.player.MediaPlayerInteractor
import com.example.playlistmaker.domain.player.MediaPlayerRepository
import com.example.playlistmaker.domain.search.model.Track
import com.example.playlistmaker.util.PlayerStates

class MediaPlayerInteractorImpl(private val repository: MediaPlayerRepository):
    MediaPlayerInteractor {
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

    override suspend fun insertFavoriteTrack(track: Track) {
        repository.insertFavoriteTrack(track)
    }

    override suspend fun deleteFavoriteTrack(track: Track) {
        repository.deleteFavoriteTrack(track)
    }

}