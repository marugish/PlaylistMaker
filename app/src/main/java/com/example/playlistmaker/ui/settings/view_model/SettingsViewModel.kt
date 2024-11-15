package com.example.playlistmaker.ui.settings.view_model

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.domain.settings.SettingsInteractor
import com.example.playlistmaker.domain.settings.model.ThemeSettings
import com.example.playlistmaker.ui.settings.state.ThemeState

class SettingsViewModel(private val context: Context,
                        private val settingsInteractor: SettingsInteractor
): ViewModel() {

    private val interactorSharing = Creator.provideSharingInteractor(context)
    private val stateLiveData = MutableLiveData<ThemeState>()
    fun observeThemeState(): LiveData<ThemeState> = stateLiveData

    init {
        getSwitchTheme()
    }

    private fun getSwitchTheme() {
        val theme = settingsInteractor.getThemeSettings()
        changeState(theme.darkTheme)
    }

    fun updateSwitchTheme(theme: Boolean) {
        settingsInteractor.updateThemeSetting(ThemeSettings(darkTheme = theme))
        changeState(theme)
    }

    private fun changeState(theme: Boolean) {
        if (theme) {
            stateLiveData.postValue(ThemeState.Active)
        } else {
            stateLiveData.postValue(ThemeState.Deactive)
        }
    }

    fun writeToSupport() {
        interactorSharing.openSupport()
    }

    fun userAgreement() {
        interactorSharing.openTerms()
    }

    fun shareLink() {
        interactorSharing.shareApp()
    }

    companion object {
        fun getViewModelFactory(context: Context): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val interactorSettings = Creator.provideSettingsInteractor()

                SettingsViewModel(
                    context = context,
                    settingsInteractor = interactorSettings
                )
            }
        }
    }
}