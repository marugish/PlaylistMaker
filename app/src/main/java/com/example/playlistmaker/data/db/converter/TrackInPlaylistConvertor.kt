package com.example.playlistmaker.data.db.converter

import com.example.playlistmaker.data.db.entity.TrackInPlaylistEntity
import com.example.playlistmaker.domain.search.model.Track

class TrackInPlaylistConvertor {
    fun map(track: TrackInPlaylistEntity): Track {
        return Track(
            track.trackId,
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