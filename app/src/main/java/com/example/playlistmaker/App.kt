package com.example.playlistmaker

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.creator.Creator

class App : Application() {
    //private lateinit var viewModel: SettingsViewModel

    override fun onCreate() {
        super.onCreate()

        Creator.init(this)

        //viewModel = ViewModelProvider(this, SettingsViewModel.getViewModelFactory())[SettingsViewModel::class.java]

        val getSwitchThemeInteractor = Creator.provideSwitchThemeInteractor()
        getSwitchThemeInteractor.getSwitchTheme { switchTheme -> switchTheme(switchTheme) }
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