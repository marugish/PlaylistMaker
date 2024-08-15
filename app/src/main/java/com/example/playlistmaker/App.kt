package com.example.playlistmaker

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate

const val PLAYLIST_PREFERENCES = "playlist_preferences"
const val THEME_SWITCH_KEY = "key_for_theme_switch"
const val SEARCH_KEY = "key_for_search"

lateinit var sharedPref : SharedPreferences
class App : Application() {

    var darkTheme = false
    override fun onCreate() {
        super.onCreate()

        // ПЕРЕПРОВЕРИТЬ - с неустановленным изначально theme_key
        // зачем мне нужен параметр darkTheme?
        sharedPref = this.getSharedPreferences(PLAYLIST_PREFERENCES, Context.MODE_PRIVATE)
        val savedTheme = sharedPref.getBoolean(THEME_SWITCH_KEY, false)
        //sharedPref.edit().putBoolean("theme_key", savedTheme).apply()
        switchTheme(savedTheme)
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
}