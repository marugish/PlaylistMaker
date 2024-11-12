package com.example.playlistmaker.ui.player

import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.util.SearchError

sealed interface TrackScreenState {
    object Loading : TrackScreenState
    data class Content(val track: Track) : TrackScreenState
    data class Error(val errorMessage: SearchError) : TrackScreenState
    object Empty : TrackScreenState
}




