package com.example.playlistmaker.ui.player

sealed interface PlayStatus {
    object Prepared: PlayStatus
    object Pause: PlayStatus
    object Error: PlayStatus
    object Start: PlayStatus
    object ToZero: PlayStatus
    data class Play(val time: Int) : PlayStatus
}