package com.example.playlistmaker.data.storage

import android.content.Context
import com.example.playlistmaker.data.Storage
import com.example.playlistmaker.data.dto.SwitchThemeDto
import com.example.playlistmaker.data.dto.TrackDto
import com.google.gson.Gson


private const val PLAYLIST_PREFERENCES = "playlist_preferences"
private const val THEME_SWITCH_KEY = "key_for_theme_switch"
private const val DEFAULT_SWITCH_THEME = false
private const val SEARCH_KEY = "key_for_search"

class SharedPrefsStorage(context: Context): Storage {
    private val sharedPreferences = context.getSharedPreferences(PLAYLIST_PREFERENCES, Context.MODE_PRIVATE)
    private val gson: Gson = Gson()

    override fun save(dto: Any): Boolean {
        return when (dto) {
            is SwitchThemeDto -> {
                sharedPreferences.edit().putBoolean(THEME_SWITCH_KEY, dto.darkTheme).apply()
                true
            }
            is List<*> -> {
                val json = gson.toJson(dto)
                sharedPreferences.edit().putString(SEARCH_KEY, json).apply()
                true
            }
            else -> {
                false
            }
        }
    }

    override fun getTheme(): SwitchThemeDto {
        val savedTheme = sharedPreferences.getBoolean(THEME_SWITCH_KEY, DEFAULT_SWITCH_THEME)
        return SwitchThemeDto(darkTheme = savedTheme)
    }

    override fun getHistoryTracks(): List<TrackDto> {
        val json = sharedPreferences.getString(SEARCH_KEY, null) ?: return mutableListOf()
        return gson.fromJson(json, Array<TrackDto>::class.java).toMutableList()
    }

    override fun clearHistory() {
        sharedPreferences.edit().remove(SEARCH_KEY).apply()
    }

}