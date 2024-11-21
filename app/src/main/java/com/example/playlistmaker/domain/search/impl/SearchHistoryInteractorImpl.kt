package com.example.playlistmaker.domain.search.impl

import com.example.playlistmaker.domain.search.SearchHistoryInteractor
import com.example.playlistmaker.domain.search.StorageRepository
import com.example.playlistmaker.domain.search.model.Track

class SearchHistoryInteractorImpl(private val repository: StorageRepository):
    SearchHistoryInteractor {

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