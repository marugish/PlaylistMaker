package com.example.playlistmaker.data

import com.example.playlistmaker.data.dto.SwitchThemeDto

interface Storage {
    fun save(dto: Any): Boolean
    fun getTheme(): SwitchThemeDto  //Boolean//User
}