package com.example.playlistmaker.ui.search

import com.example.playlistmaker.domain.models.Track

sealed interface HistoryState {
    object Clear: HistoryState
    data class Content(val tracks: List<Track>) : HistoryState
}