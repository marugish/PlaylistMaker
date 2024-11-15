package com.example.playlistmaker.ui.search.state

import com.example.playlistmaker.domain.search.model.Track

sealed interface HistoryState {
    object Clear: HistoryState
    data class Content(val tracks: List<Track>) : HistoryState
}