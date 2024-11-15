package com.example.playlistmaker.data.repository

import com.example.playlistmaker.util.PlayerStates
import com.example.playlistmaker.data.player.MediaPlayer
import com.example.playlistmaker.domain.player.MediaPlayerRepository

class MediaPlayerRepositoryImpl(private val mediaPlayer: MediaPlayer): MediaPlayerRepository {

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