package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.models.Track

interface SearchHistoryInteractor {
    // проверить
    fun saveSearchHistory(historyTracks: List<Track>)//(expression: String, consumer: TracksConsumer)
    fun getSearchHistory(consumer: SearchHistoryConsumer)

    interface SearchHistoryConsumer {
        fun consume(results: List<Track>)
    }
}