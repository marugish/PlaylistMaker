package com.example.playlistmaker

import android.app.Application
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.data.StorageRepositoryImpl
import com.example.playlistmaker.data.storage.SharedPrefsStorage
import com.example.playlistmaker.domain.GetSwitchThemeUseCase
import com.example.playlistmaker.domain.SaveSwitchThemeUseCase
import com.example.playlistmaker.domain.models.SwitchTheme



const val PLAYLIST_PREFERENCES = "playlist_preferences"
const val THEME_SWITCH_KEY = "key_for_theme_switch"
const val SEARCH_KEY = "key_for_search"

lateinit var sharedPref : SharedPreferences
class App : Application() {

    /*private lateinit var context: Context

    companion object {
        fun getContext(): Context = this@App.context
    }*/


    /*companion object {
        fun getContext(): Companion = this
    }*/

    private val repository by lazy(LazyThreadSafetyMode.NONE) {
        StorageRepositoryImpl(storage = SharedPrefsStorage(context = applicationContext))
    }
    private val getSwitchThemeUseCase by lazy(LazyThreadSafetyMode.NONE) {
        GetSwitchThemeUseCase(repository = repository)
    }
    private val saveSwitchThemeUseCase by lazy(LazyThreadSafetyMode.NONE) {
        SaveSwitchThemeUseCase(repository = repository)
    }

    override fun onCreate() {
        super.onCreate()

        // Чтение через UseCase
        val savedTheme: SwitchTheme = getSwitchThemeUseCase.execute()
        switchTheme(savedTheme.darkTheme)

        // чтение
        //sharedPref = this.getSharedPreferences(PLAYLIST_PREFERENCES, Context.MODE_PRIVATE)
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