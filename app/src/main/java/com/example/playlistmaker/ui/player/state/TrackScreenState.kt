package com.example.playlistmaker.ui.player.state

import com.example.playlistmaker.domain.search.model.Track

sealed interface TrackScreenState {
    data object Loading : TrackScreenState
    data class Content(val track: Track) : TrackScreenState
    data object Empty : TrackScreenState
}




