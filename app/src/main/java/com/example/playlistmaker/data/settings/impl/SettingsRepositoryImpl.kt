package com.example.playlistmaker.data.settings.impl

import com.example.playlistmaker.data.Storage
import com.example.playlistmaker.data.settings.SettingsRepository
import com.example.playlistmaker.domain.settings.model.ThemeSettings

class SettingsRepositoryImpl(private val storage: Storage): SettingsRepository {
    override fun getThemeSettings(): ThemeSettings {
        TODO("Not yet implemented")
        //return storage.getTheme()
    }

    override fun updateThemeSetting(settings: ThemeSettings) {
        TODO("Not yet implemented")
        //return storage.save(saveThemeParam)
    }
}