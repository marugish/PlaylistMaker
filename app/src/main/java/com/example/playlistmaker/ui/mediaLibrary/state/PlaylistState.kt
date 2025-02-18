package com.example.playlistmaker.ui.mediaLibrary.state

import com.example.playlistmaker.domain.db.model.Playlist

sealed interface PlaylistState {
    data class Content(val playlist: Playlist) : PlaylistState
    // треки ...
    data object Empty : PlaylistState
}