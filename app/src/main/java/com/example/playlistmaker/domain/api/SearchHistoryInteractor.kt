package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.models.Track

interface SearchHistoryInteractor {

    fun getSearchHistory(consumer: SearchHistoryConsumer)
    fun saveSearchHistory(track: Track)
    fun clearHistory()

    fun interface SearchHistoryConsumer {
        fun consume(results: List<Track>)
    }
}