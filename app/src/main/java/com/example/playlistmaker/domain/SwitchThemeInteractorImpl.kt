package com.example.playlistmaker.domain

import com.example.playlistmaker.domain.search.StorageRepository

// старая реализация
class SwitchThemeInteractorImpl(private val repository: StorageRepository): SwitchThemeInteractor {

    override fun saveSwitchTheme(theme: Boolean) {
        repository.saveThemeParam(theme)
    }

    override fun getSwitchTheme(consumer: SwitchThemeInteractor.SwitchThemeConsumer) {
        consumer.consume(repository.getThemeParam())
    }

}