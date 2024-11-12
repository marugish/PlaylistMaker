package com.example.playlistmaker.ui.search

import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.util.SearchError

sealed interface TracksState {
    object Loading : TracksState
    data class Content(val tracks: List<Track>) : TracksState
    data class Error(val errorMessage: SearchError) : TracksState
    data class Empty(val message: SearchError) : TracksState
}







