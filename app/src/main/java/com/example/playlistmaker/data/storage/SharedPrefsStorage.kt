package com.example.playlistmaker.data.storage

import android.content.Context
import com.example.playlistmaker.THEME_SWITCH_KEY
import com.example.playlistmaker.data.Storage
import com.example.playlistmaker.data.dto.SwitchThemeDto


private const val KEY_FIRST_NAME = "firstName"
private const val KEY_LAST_NAME = "lastName"
private const val DEFAULT_FIRST_NAME = "Default first name"
private const val DEFAULT_LAST_NAME = "Default last name"

private const val PLAYLIST_PREFERENCES = "playlist_preferences"
private const val THEME_SWITCH_KEY = "key_for_theme_switch"
private const val DEFAULT_SWITCH_THEME = false

class SharedPrefsStorage(context: Context): Storage {
    private val sharedPreferences = context.getSharedPreferences(PLAYLIST_PREFERENCES, Context.MODE_PRIVATE)

    // Сделать проверки сохранения

    override fun save(dto: Any): Boolean {
        return when (dto) {
            is SwitchThemeDto -> {
                sharedPreferences.edit().putBoolean(THEME_SWITCH_KEY, dto.darkTheme).apply()
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


        //val savedTheme = sharedPref.getBoolean(THEME_SWITCH_KEY, false)
        //val firstName = sharedPreferences.getString(KEY_FIRST_NAME, DEFAULT_FIRST_NAME) ?: DEFAULT_FIRST_NAME
        //val lastName = sharedPreferences.getString(KEY_LAST_NAME, DEFAULT_LAST_NAME) ?: DEFAULT_LAST_NAME

        //return SwitchThemeDto(darkTheme = savedTheme)//savedTheme//User(firstName = firstName, lastName = lastName)
    }
}