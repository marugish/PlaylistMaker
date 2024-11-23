package com.example.playlistmaker.data.search.storage

import android.content.SharedPreferences
import androidx.core.content.edit
import com.example.playlistmaker.data.search.Storage
import com.example.playlistmaker.data.search.dto.TrackDto
import com.google.gson.Gson

private const val THEME_SWITCH_KEY = "key_for_theme_switch"
private const val DEFAULT_SWITCH_THEME = false
private const val SEARCH_KEY = "key_for_search"

class SharedPrefsStorage(private val sharedPrefs: SharedPreferences, private val gson: Gson): Storage {

    override fun save(darkTheme: Boolean): Boolean {
        sharedPrefs.edit {
            putBoolean(THEME_SWITCH_KEY, darkTheme)
        }
        return true
    }

    override fun save(dto: List<TrackDto>): Boolean {
        val json = gson.toJson(dto)
        sharedPrefs.edit {
            putString(SEARCH_KEY, json)
        }
        return true
    }

    override fun getTheme(): Boolean {
        return sharedPrefs.getBoolean(THEME_SWITCH_KEY, DEFAULT_SWITCH_THEME)
    }

    override fun getHistoryTracks(): List<TrackDto> {
        val json = sharedPrefs.getString(SEARCH_KEY, null) ?: return emptyList()
        return gson.fromJson(json, Array<TrackDto>::class.java).toList()
    }

    override fun clearHistory() {
        sharedPrefs.edit {
            remove(SEARCH_KEY)
        }
    }

}