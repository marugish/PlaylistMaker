package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.models.SwitchTheme

interface SwitchThemeInteractor {
    fun saveSwitchTheme(theme: Boolean)//(expression: String, consumer: TracksConsumer)
    fun getSwitchTheme(consumer: SwitchThemeConsumer)

    interface SwitchThemeConsumer {
        fun consume(switchTheme: SwitchTheme)
    }
}