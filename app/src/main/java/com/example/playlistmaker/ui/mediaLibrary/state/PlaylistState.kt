package com.example.playlistmaker.ui.mediaLibrary.state

import com.example.playlistmaker.domain.search.model.Track

sealed interface PlaylistState {
    data class Content(val track: Track) : PlaylistState
    data object Empty : PlaylistState
}