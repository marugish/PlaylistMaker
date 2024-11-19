package com.example.playlistmaker.data.settings

import com.example.playlistmaker.data.search.Storage
import com.example.playlistmaker.domain.settings.SettingsRepository
import com.example.playlistmaker.domain.settings.model.ThemeSettings

class SettingsRepositoryImpl(private val storage: Storage): SettingsRepository {
    override fun getThemeSettings(): ThemeSettings {
        return ThemeSettings(storage.getTheme())
    }

    override fun updateThemeSetting(settings: ThemeSettings) {
        storage.save(settings.darkTheme)
    }
}