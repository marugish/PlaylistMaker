package com.example.playlistmaker

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.domain.api.SwitchThemeInteractor
import com.example.playlistmaker.domain.models.SwitchTheme

class App : Application() {

    companion object {
        private lateinit var instance: App

        fun getContext(): Context {
            return instance
        }
    }

    override fun onCreate() {
        super.onCreate()

        instance = this

        val getSwitchThemeInteractor = Creator.provideSwitchThemeInteractor()
        getSwitchThemeInteractor.getSwitchTheme(
            consumer = object : SwitchThemeInteractor.SwitchThemeConsumer {
                override fun consume(switchTheme: SwitchTheme) {
                    switchTheme(switchTheme.darkTheme)
                }
            })
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
}