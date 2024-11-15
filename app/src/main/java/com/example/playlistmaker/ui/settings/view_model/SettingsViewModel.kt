package com.example.playlistmaker.ui.settings.view_model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.domain.settings.SettingsInteractor
import com.example.playlistmaker.domain.settings.model.ThemeSettings
import com.example.playlistmaker.domain.sharing.SharingInteractor
import com.example.playlistmaker.ui.settings.ThemeState

class SettingsViewModel(private val sharingInteractor: SharingInteractor,
                        private val settingsInteractor: SettingsInteractor
): ViewModel() {

    private val stateLiveData = MutableLiveData<ThemeState>()
    fun observeThemeState(): LiveData<ThemeState> = stateLiveData

    init {
        Log.d("mysettings", "init")
        getSwitchTheme()
    }

    fun getSwitchTheme() {

        val theme = settingsInteractor.getThemeSettings()
        Log.d("mysettings", "theme = ${theme.darkTheme}")
        if (theme.darkTheme) {
            stateLiveData.postValue(ThemeState.Active)
            Log.d("mysettings", "Active")
        } else {
            stateLiveData.postValue(ThemeState.Deactive)
            Log.d("mysettings", "Deactive")
        }

    }

    fun updateSwitchTheme(theme: Boolean) {
        settingsInteractor.updateThemeSetting(ThemeSettings(darkTheme = theme))
        if (theme) {
            stateLiveData.postValue(ThemeState.Active)
        } else {
            stateLiveData.postValue(ThemeState.Deactive)
        }


    }

    companion object {
        fun getViewModelFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val interactorSharing = Creator.provideSharingInteractor()
                val interactorSettings = Creator.provideSettingsInteractor()

                SettingsViewModel(
                    sharingInteractor = interactorSharing,
                    settingsInteractor = interactorSettings
                )
            }
        }
    }
}