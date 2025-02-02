package com.example.playlistmaker.domain.db.model

data class Playlist(
    val playlistName: String = "",
    val playlistDescription: String? = null,
    val photoUrl: String? = null,
    val trackIds: String = "",
    val trackCount: Int = 0
)
