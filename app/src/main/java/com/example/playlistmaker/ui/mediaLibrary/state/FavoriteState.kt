package com.example.playlistmaker.ui.mediaLibrary.state

import com.example.playlistmaker.domain.search.model.Track

sealed interface FavoriteState {
    data class Content(val track: Track) : FavoriteState
    data object Empty : FavoriteState
}