package com.example.playlistmaker.data.storage

import android.content.Context
import android.util.Log
import com.example.playlistmaker.SEARCH_KEY
import com.example.playlistmaker.THEME_SWITCH_KEY
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

    /*fun write(track: Track) {
        // Получаем 1 трек, а не весь список. Изменяем historyResults
        val position = historyResults.indexOf(track) // Проверяем, есть ли этот трек в списке
        if (position != -1)
            historyResults.removeAt(position) // удаляем найденный трек
        if (historyResults.size == 10) { // должно быть не больше 10 треков
            historyResults.removeLast()
        }
        historyResults.add(0, track) // теперь только добавляем в начало

        val json = gson.toJson(historyResults)
        sharedPreferences.edit()
            .putString(SEARCH_KEY, json)
            .apply()
    }*/

    /*fun clearHistory() {
        sharedPreferences.edit()
            .remove(SEARCH_KEY)
            .apply()
        historyResults.clear()
    }*/

    // Сделать проверки сохранения

    override fun save(dto: Any): Boolean {
        Log.d("sharedpref save", "заход в функцию без when")
        return when (dto) {
            is SwitchThemeDto -> {
                val success = sharedPreferences.edit().putBoolean(THEME_SWITCH_KEY, dto.darkTheme).apply()
                Log.d("sharedpref save", "$success")
                true
            }
            is TrackDto -> {

                true
            }
            is List<*> -> {
                val tracksDto = dto as List<TrackDto> // Приводим к List<TrackDto>


                //val json = gson.toJson(tracksDto)
                //sharedPreferences.edit().putString(TRACKS_KEY, json).apply()
                true
            }
            else -> {
                false
            }
        }
        /*return
        if (dto is TracksSearchRequest) {
            val resp = RetrofitItunesClient.itunesService.searchTrack(dto.request).execute()

            val body = resp.body() ?: NetworkResponse()

            body.apply { resultCode = resp.code() }
        } else {
            NetworkResponse().apply { resultCode = 400 }
        }*/
        //sharedPreferences.edit().putString(KEY_FIRST_NAME, user.firstName).apply()
        //sharedPreferences.edit().putString(KEY_LAST_NAME, user.lastName).apply()
        //return true
    }

    override fun getTheme(): SwitchThemeDto {   //User {
        val savedTheme = sharedPreferences.getBoolean(THEME_SWITCH_KEY, DEFAULT_SWITCH_THEME)
        return SwitchThemeDto(darkTheme = savedTheme)

        /*val savedTheme = sharedPref.getBoolean(THEME_SWITCH_KEY, false)
        //val firstName = sharedPreferences.getString(KEY_FIRST_NAME, DEFAULT_FIRST_NAME) ?: DEFAULT_FIRST_NAME
        //val lastName = sharedPreferences.getString(KEY_LAST_NAME, DEFAULT_LAST_NAME) ?: DEFAULT_LAST_NAME

        //return SwitchThemeDto(darkTheme = savedTheme)//savedTheme//User(firstName = firstName, lastName = lastName)*/
    }

    // !!!!!!!!! Перепроверить
    override fun getHistoryTracks(): List<TrackDto> {
        val json = sharedPreferences.getString(SEARCH_KEY, null) ?: return mutableListOf()
        return gson.fromJson(json, Array<TrackDto>::class.java).toMutableList()
    }


}