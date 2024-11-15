package com.example.playlistmaker.data.settings.impl

import com.example.playlistmaker.data.Storage
import com.example.playlistmaker.data.settings.SettingsRepository
import com.example.playlistmaker.domain.settings.model.ThemeSettings

class SettingsRepositoryImpl(private val storage: Storage): SettingsRepository {
    override fun getThemeSettings(): ThemeSettings {
        return ThemeSettings(storage.getTheme())
        // Необходимо исправить, вызов класса должен быть отдельно вроде
    }

    override fun updateThemeSetting(settings: ThemeSettings) {
        storage.save(settings.darkTheme)
    }
}