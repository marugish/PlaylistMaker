package com.example.playlistmaker

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.creator.Creator

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        Creator.init(this)

        val getSwitchThemeInteractor = Creator.provideSettingsInteractor()//provideSwitchThemeInteractor()
        val theme = getSwitchThemeInteractor.getThemeSettings()
        switchTheme(theme.darkTheme)
    }

    fun switchTheme(isDark: Boolean) {
        AppCompatDelegate.setDefaultNightMode(
            if (isDark) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }

}