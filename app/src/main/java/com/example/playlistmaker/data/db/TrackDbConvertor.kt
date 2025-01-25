package com.example.playlistmaker.data.db

import com.example.playlistmaker.data.search.dto.TrackDto
import com.example.playlistmaker.domain.search.model.Track

class TrackDbConvertor {
    fun map(track: TrackDto): TrackEntity {
        return TrackEntity(
            track.id,
            track.trackName,
            track.artistName,
            track.trackTimeMillis,
            track.artworkUrl100,
            track.collectionName,
            track.releaseDate,
            track.primaryGenreName,
            track.country,
            track.previewUrl
        )
    }

    fun map(track: TrackEntity): Track {
        return Track(
            track.trackName,
            track.artistName,
            track.trackTimeMillis,
            track.artworkUrl100,
            track.collectionName,
            track.releaseDate,
            track.primaryGenreName,
            track.country,
            track.previewUrl
        )
    }
}