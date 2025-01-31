package com.example.playlistmaker.ui.mediaLibrary.state

import com.example.playlistmaker.domain.search.model.Track

sealed interface FavoriteState {
    data object Loading : FavoriteState
    data class Content(val tracks: List<Track>) : FavoriteState
    data object Empty : FavoriteState
}