package com.example.playlistmaker.data

import com.example.playlistmaker.data.dto.SwitchThemeDto
import com.example.playlistmaker.data.dto.TrackDto

interface Storage {
    fun save(dto: Any): Boolean
    fun getTheme(): SwitchThemeDto  //Boolean//User
    fun getHistoryTracks(): List<TrackDto>
}