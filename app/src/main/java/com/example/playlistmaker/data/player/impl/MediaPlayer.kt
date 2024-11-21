package com.example.playlistmaker.data.player.impl

import android.media.MediaPlayer
import com.example.playlistmaker.data.player.MediaPlayerInterface
import com.example.playlistmaker.util.PlayerStates

class MediaPlayer: MediaPlayerInterface {
    private val mediaPlayer = MediaPlayer()
    private var playerState = PlayerStates.DEFAULT

    override fun prepare(url: String) {
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playerState = PlayerStates.PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            playerState = PlayerStates.COMPLETED
        }
    }

    override fun start() {
        mediaPlayer.start()
        playerState = PlayerStates.PLAYING
    }

    override fun pause() {
        mediaPlayer.pause()
        playerState = PlayerStates.PAUSED
    }

    override fun release() {
        mediaPlayer.release()
    }

    override fun getCurrentPosition(): Int {
        return mediaPlayer.currentPosition
    }

    override fun getCurrentPlayerState(): PlayerStates {
        return playerState
    }

    override fun changeState(state: PlayerStates) {
        if (playerState == PlayerStates.COMPLETED && state == PlayerStates.PREPARED) {
            playerState = state
        }
    }
}