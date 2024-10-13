package com.example.playlistmaker.domain

import com.example.playlistmaker.domain.api.StorageRepository
import com.example.playlistmaker.domain.models.SwitchTheme

class SaveSwitchThemeUseCase(private val repository: StorageRepository) {
    fun execute(darkTheme: Boolean): Boolean {
        return repository.saveThemeParam(SwitchTheme(darkTheme = darkTheme))
    }
}