package com.example.playlistmaker.domain.search

import com.example.playlistmaker.util.SearchError
import com.example.playlistmaker.domain.search.model.Track

interface TracksInteractor {
    fun searchTracks(expression: String, consumer: TracksConsumer)

    fun interface TracksConsumer {
        fun consume(foundTracks: List<Track>?, errorMessage: SearchError?)
    }
}