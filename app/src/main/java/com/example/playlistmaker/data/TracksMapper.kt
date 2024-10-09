package com.example.playlistmaker.data

import com.example.playlistmaker.data.dto.TrackDto
import com.example.playlistmaker.domain.models.Track


object TracksMapper {

    fun map(trackDto: ArrayList<TrackDto>): List<Track> {
        return trackDto.map { Track(it.trackName,
            it.artistName,
            it.trackTimeMillis,
            it.artworkUrl100,
            it.collectionName,
            it.releaseDate,
            it.primaryGenreName,
            it.country,
            it.previewUrl)
        } // тут не список получится скорее всего, необходимо проверить! Либо на вход что-то другое необходимо будет подать

    }

}