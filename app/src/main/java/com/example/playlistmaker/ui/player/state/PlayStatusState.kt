package com.example.playlistmaker.ui.player.state

sealed interface PlayStatusState {
    data object Pause: PlayStatusState
    data object Error: PlayStatusState
    data object Start: PlayStatusState
    data object ToZero: PlayStatusState
    data class PlayState(val time: Int) : PlayStatusState
}