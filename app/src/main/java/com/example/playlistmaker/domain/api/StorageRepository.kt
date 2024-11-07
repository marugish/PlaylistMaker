package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.models.Track

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