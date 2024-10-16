package com.example.playlistmaker.data.mapper

import com.example.playlistmaker.data.dto.TrackDto
import com.example.playlistmaker.domain.models.Track


object TracksMapper {
    fun mapToDomain(trackDto: List<TrackDto>): List<Track> {
        return trackDto.map { Track(it.trackName,
            it.artistName,
            it.trackTimeMillis,
            it.artworkUrl100,
            it.collectionName,
            it.releaseDate,
            it.primaryGenreName,
            it.country,
            it.previewUrl)
        }

    }
}