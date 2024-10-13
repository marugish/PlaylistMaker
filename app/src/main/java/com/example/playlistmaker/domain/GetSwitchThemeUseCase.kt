package com.example.playlistmaker.domain

import com.example.playlistmaker.domain.api.StorageRepository
import com.example.playlistmaker.domain.models.SwitchTheme

class GetSwitchThemeUseCase(private val repository: StorageRepository) {
    fun execute(): SwitchTheme {
        return repository.getThemeParam()
    }
}