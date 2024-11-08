package com.example.playlistmaker.domain.api

import com.example.playlistmaker.SearchError
import com.example.playlistmaker.domain.models.Track

interface TracksInteractor {
    fun searchTracks(expression: String, consumer: TracksConsumer)

    fun interface TracksConsumer {
        fun consume(foundTracks: List<Track>?, errorMessage: SearchError?)
    }
}