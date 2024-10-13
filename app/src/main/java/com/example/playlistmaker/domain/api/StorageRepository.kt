package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.models.SwitchTheme

interface StorageRepository {
    fun saveThemeParam(saveThemeParam: SwitchTheme): Boolean
    fun getThemeParam(): SwitchTheme
}