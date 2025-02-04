package com.example.playlistmaker.data.db.converter

import com.example.playlistmaker.data.db.entity.PlaylistEntity
import com.example.playlistmaker.data.db.entity.TrackInPlaylistEntity
import com.example.playlistmaker.domain.db.model.Playlist
import com.example.playlistmaker.domain.search.model.Track

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
            id = playlist.playlistId,
            playlistName = playlist.playlistName,
            playlistDescription = playlist.playlistDescription,
            photoUrl = playlist.photoUrl,
            trackIds = playlist.trackIds,
            trackCount = playlist.trackCount
        )
    }

    fun map(track: Track): TrackInPlaylistEntity {
        return TrackInPlaylistEntity(
            trackId = track.trackId,
            trackName = track.trackName,
            artistName = track.artistName,
            trackTimeMillis = track.trackTimeMillis,
            artworkUrl100 = track.artworkUrl100,
            collectionName = track.collectionName,
            releaseDate = track.releaseDate,
            primaryGenreName = track.primaryGenreName,
            country = track.country,
            previewUrl = track.previewUrl
        )
    }
}