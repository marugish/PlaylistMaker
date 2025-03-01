package com.example.playlistmaker.ui.mediaLibrary.state

import com.example.playlistmaker.domain.db.model.Playlist

sealed interface PlaylistsState {
    data class Content(val playlists: List<Playlist>) : PlaylistsState
    data object Empty : PlaylistsState
}