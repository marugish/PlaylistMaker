package com.example.playlistmaker.domain.db.model

data class Playlist(
    val id: Long? = null,
    val playlistName: String = "",
    val playlistDescription: String? = null,
    val photoUrl: String? = null,
    val trackIds: String = "",
    var trackCount: Int = 0
)
