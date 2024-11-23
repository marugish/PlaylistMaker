package com.example.playlistmaker.data.player.impl

import com.example.playlistmaker.data.player.MediaPlayerInterface
import com.example.playlistmaker.domain.player.MediaPlayerRepository
import com.example.playlistmaker.util.PlayerStates

class MediaPlayerRepositoryImpl(private val mediaPlayer: MediaPlayerInterface): MediaPlayerRepository {

    override fun prepare(url: String) {
        mediaPlayer.prepare(url)
    }

    override fun start() {
        mediaPlayer.start()
    }

    override fun pause() {
        mediaPlayer.pause()
    }

    override fun release() {
        mediaPlayer.release()
    }

    override fun getCurrentPosition(): Int {
        return mediaPlayer.getCurrentPosition()
    }

    override fun getCurrentPlayerState(): PlayerStates {
        return mediaPlayer.getCurrentPlayerState()
    }

    override fun changeState(state: PlayerStates) {
        mediaPlayer.changeState(state)
    }
}