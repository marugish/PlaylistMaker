package com.example.playlistmaker.data.settings

import com.example.playlistmaker.domain.settings.model.ThemeSettings

// данный интерфейс необходимо перенести в domain
interface SettingsRepository {
    fun getThemeSettings(): ThemeSettings
    fun updateThemeSetting(settings: ThemeSettings)
}