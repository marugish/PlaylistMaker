package com.example.playlistmaker.data.db.converter

import com.example.playlistmaker.data.db.entity.PlaylistEntity
import com.example.playlistmaker.data.db.entity.TrackEntity
import com.example.playlistmaker.data.search.dto.TrackDto
import com.example.playlistmaker.domain.db.model.Playlist

class PlaylistDbConvertor {
    fun map(playlist: Playlist): PlaylistEntity {
        return PlaylistEntity(
            playlistId = 0,
            playlistName = playlist.playlistName,
            playlistDescription = playlist.playlistDescription,
            photoUrl = playlist.photoUrl,
            trackIds = playlist.trackIds,
            trackCount = playlist.trackCount
        )
    }

    fun map(playlist: PlaylistEntity): Playlist {
        return Playlist(
            playlistName = playlist.playlistName,
            playlistDescription = playlist.playlistDescription,
            photoUrl = playlist.photoUrl,
            trackIds = playlist.trackIds,
            trackCount = playlist.trackCount
        )
    }
}