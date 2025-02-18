package com.example.playlistmaker.ui.mediaLibrary.state

import com.example.playlistmaker.domain.db.model.Playlist
import com.example.playlistmaker.domain.search.model.Track

sealed interface PlaylistState {
    data class Content(val playlist: Playlist, val tracks: List<Track>?) : PlaylistState
    // треки ...
    data object Empty : PlaylistState
}