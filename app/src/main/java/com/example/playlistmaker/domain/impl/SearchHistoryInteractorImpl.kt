package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.domain.api.SearchHistoryInteractor
import com.example.playlistmaker.domain.api.StorageRepository
import com.example.playlistmaker.domain.models.Track

class SearchHistoryInteractorImpl(private val repository: StorageRepository): SearchHistoryInteractor {
    // проверить
    override fun saveSearchHistory(historyTracks: List<Track>) {

    }
    //(expression: String, consumer: TracksConsumer)

    override fun getSearchHistory(consumer: SearchHistoryInteractor.SearchHistoryConsumer) {
        consumer.consume(repository.getSearchHistory())
    }

}