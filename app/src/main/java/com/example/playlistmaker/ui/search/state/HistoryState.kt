package com.example.playlistmaker.ui.search.state

import com.example.playlistmaker.domain.search.model.Track

sealed interface HistoryState {
    data object Clear: HistoryState
    data class Content(val tracks: List<Track>) : HistoryState
}