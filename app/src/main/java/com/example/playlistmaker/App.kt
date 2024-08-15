package com.example.playlistmaker

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate

const val PLAYLIST_PREFERENCES = "playlist_preferences"
const val THEME_SWITCH_KEY = "key_for_theme_switch"
const val SEARCH_KEY = "key_for_search"

lateinit var sharedPref : SharedPreferences
class App : Application() {

    override fun onCreate() {
        super.onCreate()

        sharedPref = this.getSharedPreferences(PLAYLIST_PREFERENCES, Context.MODE_PRIVATE)
        val savedTheme = sharedPref.getBoolean(THEME_SWITCH_KEY, false)
        switchTheme(savedTheme)
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