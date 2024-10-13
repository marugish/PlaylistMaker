package com.example.playlistmaker

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.domain.api.SwitchThemeInteractor
import com.example.playlistmaker.domain.models.SwitchTheme


const val PLAYLIST_PREFERENCES = "playlist_preferences"
const val THEME_SWITCH_KEY = "key_for_theme_switch"
const val SEARCH_KEY = "key_for_search"

lateinit var sharedPref : SharedPreferences
class App : Application() {

    companion object {
        private lateinit var instance: App

        fun getContext(): Context {
            return instance
        }
    }

    /*private lateinit var context: Context*/

    /*companion object {
        fun getContext(): Companion = this
    }*/


    /*companion object {
        fun getContext(): Companion = this
    }*/

    /*private val repository by lazy(LazyThreadSafetyMode.NONE) {
        StorageRepositoryImpl(storage = SharedPrefsStorage(context = applicationContext))
    }
    private val getSwitchThemeUseCase by lazy(LazyThreadSafetyMode.NONE) {
        GetSwitchThemeUseCase(repository = repository)
    }
    private val saveSwitchThemeUseCase by lazy(LazyThreadSafetyMode.NONE) {
        SaveSwitchThemeUseCase(repository = repository)
    }*/

    override fun onCreate() {
        super.onCreate()

        instance = this

        val getSwitchThemeInteractor = Creator.provideSwitchThemeInteractor()
        // Чтение через UseCase
        //val savedTheme: SwitchTheme = getSwitchThemeUseCase.execute()
        //switchTheme(savedTheme.darkTheme)

        // Чтение через Interactor
        getSwitchThemeInteractor.getSwitchTheme(
            consumer = object : SwitchThemeInteractor.SwitchThemeConsumer {
                override fun consume(switchTheme: SwitchTheme) {
                    switchTheme(switchTheme.darkTheme)
                }
            })


        // чтение старое
        sharedPref = this.getSharedPreferences(PLAYLIST_PREFERENCES, Context.MODE_PRIVATE)
        //val savedTheme = sharedPref.getBoolean(THEME_SWITCH_KEY, false)
        //switchTheme(savedTheme)
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