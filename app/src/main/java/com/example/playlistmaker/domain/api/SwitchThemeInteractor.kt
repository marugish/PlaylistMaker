package com.example.playlistmaker.domain.api

interface SwitchThemeInteractor {

    fun saveSwitchTheme(theme: Boolean)
    fun getSwitchTheme(consumer: SwitchThemeConsumer)

    fun interface SwitchThemeConsumer {
        fun consume(switchTheme: Boolean)
    }
}