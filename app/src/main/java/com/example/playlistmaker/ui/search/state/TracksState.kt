package com.example.playlistmaker.ui.search.state

import com.example.playlistmaker.domain.search.model.Track
import com.example.playlistmaker.util.SearchError

sealed interface TracksState {
    object Loading : TracksState
    data class Content(val tracks: List<Track>) : TracksState
    data class Error(val errorMessage: SearchError) : TracksState
    data class Empty(val message: SearchError) : TracksState
}







