package com.example.playlistmaker.data.player

import android.media.MediaPlayer
import com.example.playlistmaker.data.MediaPlayerInterface

class MediaPlayer: MediaPlayerInterface {
    private val mediaPlayer = MediaPlayer()

    private companion object {
        const val STATE_DEFAULT = 0
        const val STATE_PREPARED = 1
        const val STATE_PLAYING = 2
        const val STATE_PAUSED = 3
        const val STATE_COMPLETED = 4
    }
    private var playerState = STATE_DEFAULT

    override fun prepare(url: String) {
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playerState = STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            playerState = STATE_COMPLETED
        }
    }

    override fun start() {
        mediaPlayer.start()
        playerState = STATE_PLAYING
    }

    override fun pause() {
        mediaPlayer.pause()
        playerState = STATE_PAUSED
    }

    override fun release() {
        mediaPlayer.release()
        // нужен ли тут какой-то статус?
    }

    override fun getCurrentPosition(): Int {
        return mediaPlayer.currentPosition
    }

    override fun getCurrentPlayerState(): Int {
        return playerState
    }

    // Посмотреть
    override fun changeState(state: Int) {
        if (playerState == STATE_COMPLETED && state == STATE_PREPARED) {
            playerState = state
        }
    }
}