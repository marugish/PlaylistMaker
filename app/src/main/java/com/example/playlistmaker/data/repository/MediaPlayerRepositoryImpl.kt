package com.example.playlistmaker.data.repository

import com.example.playlistmaker.data.player.MediaPlayer
import com.example.playlistmaker.domain.api.MediaPlayerRepository

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

    override fun getCurrentPlayerState(): Int {
        return mediaPlayer.getCurrentPlayerState()
    }

    override fun changeState(state: Int) {
        mediaPlayer.changeState(state)
    }
}