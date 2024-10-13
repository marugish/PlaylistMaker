package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.domain.api.StorageRepository
import com.example.playlistmaker.domain.api.SwitchThemeInteractor
import com.example.playlistmaker.domain.models.SwitchTheme

class SwitchThemeInteractorImpl(private val repository: StorageRepository): SwitchThemeInteractor {

    override fun saveSwitchTheme(theme: Boolean) {
        repository.saveThemeParam(SwitchTheme(darkTheme = theme))
    }

    override fun getSwitchTheme(consumer: SwitchThemeInteractor.SwitchThemeConsumer) {
        consumer.consume(repository.getThemeParam())
    }

}