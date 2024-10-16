package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.domain.api.SearchHistoryInteractor
import com.example.playlistmaker.domain.api.StorageRepository
import com.example.playlistmaker.domain.models.Track

class SearchHistoryInteractorImpl(private val repository: StorageRepository): SearchHistoryInteractor {

    override fun saveSearchHistory(track: Track) {
        repository.addSearchHistoryTrack(track = track)
    }

    override fun getSearchHistory(consumer: SearchHistoryInteractor.SearchHistoryConsumer) {
        consumer.consume(repository.getSearchHistoryTracks())
    }

    override fun clearHistory() {
        repository.clearHistory()
    }

}