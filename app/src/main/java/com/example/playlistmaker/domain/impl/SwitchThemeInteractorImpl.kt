package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.domain.api.StorageRepository
import com.example.playlistmaker.domain.api.SwitchThemeInteractor

class SwitchThemeInteractorImpl(private val repository: StorageRepository): SwitchThemeInteractor {

    override fun saveSwitchTheme(theme: Boolean) {
        repository.saveThemeParam(theme)
    }

    override fun getSwitchTheme(consumer: SwitchThemeInteractor.SwitchThemeConsumer) {
        consumer.consume(repository.getThemeParam())
    }

}