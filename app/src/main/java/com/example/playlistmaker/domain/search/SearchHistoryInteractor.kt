package com.example.playlistmaker.domain.search

import com.example.playlistmaker.domain.search.model.Track

interface SearchHistoryInteractor {

    fun getSearchHistory(consumer: SearchHistoryConsumer)
    fun saveSearchHistory(track: Track)
    fun clearHistory()

    fun interface SearchHistoryConsumer {
        fun consume(results: List<Track>)
    }
}