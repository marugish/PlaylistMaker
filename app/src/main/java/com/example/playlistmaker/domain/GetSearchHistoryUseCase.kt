package com.example.playlistmaker.domain

import com.example.playlistmaker.domain.api.StorageRepository
import com.example.playlistmaker.domain.models.Track

class GetSearchHistoryUseCase(private val repository: StorageRepository) {
    fun execute(): List<Track> {
        return repository.getSearchHistory()
    }
}

