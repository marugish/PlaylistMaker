package com.example.playlistmaker.ui.player.state

import com.example.playlistmaker.domain.search.model.Track
import com.example.playlistmaker.util.SearchError

sealed interface TrackScreenState {
    data object Loading : TrackScreenState
    data class Content(val track: Track) : TrackScreenState
    data class Error(val errorMessage: SearchError) : TrackScreenState
    data object Empty : TrackScreenState
}




