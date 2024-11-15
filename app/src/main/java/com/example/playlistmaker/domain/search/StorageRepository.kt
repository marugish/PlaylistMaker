package com.example.playlistmaker.domain.search

import com.example.playlistmaker.domain.search.model.Track

interface StorageRepository {
    // Тёмная тема
    fun saveThemeParam(saveThemeParam: Boolean): Boolean
    fun getThemeParam(): Boolean
    // История поиска
    fun saveSearchHistoryToSharedPreferences(tracks: List<Track>): Boolean
    fun getSearchHistoryFromSharedPreferences(): List<Track>

    fun addSearchHistoryTrack(track: Track)
    fun getSearchHistoryTracks(): List<Track>
    fun clearHistory()
}