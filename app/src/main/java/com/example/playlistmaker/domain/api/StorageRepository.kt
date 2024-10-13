package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.models.SwitchTheme
import com.example.playlistmaker.domain.models.Track

interface StorageRepository {
    // Тёмная тема
    fun saveThemeParam(saveThemeParam: SwitchTheme): Boolean
    fun getThemeParam(): SwitchTheme
    // История поиска
    fun saveSearchHistory(tracks: List<Track>): Boolean
    fun getSearchHistory(): List<Track>
}