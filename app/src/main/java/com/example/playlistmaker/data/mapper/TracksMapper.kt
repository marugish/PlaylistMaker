package com.example.playlistmaker.data.mapper

import com.example.playlistmaker.data.dto.TrackDto
import com.example.playlistmaker.domain.search.model.Track


object TracksMapper {
    fun mapToDomain(tracksDto: TrackDto): Track {
        return Track(
            trackName = tracksDto.trackName,
            artistName = tracksDto.artistName,
            trackTimeMillis = tracksDto.trackTimeMillis,
            artworkUrl100 = tracksDto.artworkUrl100,
            collectionName = tracksDto.collectionName,
            releaseDate = tracksDto.releaseDate,
            primaryGenreName = tracksDto.primaryGenreName,
            country = tracksDto.country,
            previewUrl = tracksDto.previewUrl
        )
    }

    fun mapToStorage(tracks: Track): TrackDto {
        return TrackDto(
            trackName = tracks.trackName,
            artistName = tracks.artistName,
            trackTimeMillis = tracks.trackTimeMillis,
            artworkUrl100 = tracks.artworkUrl100,
            collectionName = tracks.collectionName,
            releaseDate = tracks.releaseDate,
            primaryGenreName = tracks.primaryGenreName,
            country = tracks.country,
            previewUrl = tracks.previewUrl
        )
    }
}