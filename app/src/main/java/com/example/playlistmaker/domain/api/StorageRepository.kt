package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.models.SwitchTheme
import com.example.playlistmaker.domain.models.Track

interface StorageRepository {
    // Тёмная тема
    fun saveThemeParam(saveThemeParam: SwitchTheme): Boolean
    fun getThemeParam(): SwitchTheme
    // История поиска
    fun saveSearchHistoryToSharedPreferences(tracks: List<Track>): Boolean
    fun getSearchHistoryFromSharedPreferences(): List<Track>

    fun addSearchHistoryTrack(track: Track)
    fun getSearchHistoryTracks(): List<Track>
    fun clearHistory()
}