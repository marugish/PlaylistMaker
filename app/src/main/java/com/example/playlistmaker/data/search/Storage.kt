package com.example.playlistmaker.data.search

import com.example.playlistmaker.data.search.dto.TrackDto

interface Storage {
    fun save(darkTheme: Boolean): Boolean
    fun save(dto: List<TrackDto>): Boolean
    fun getTheme(): Boolean
    fun getHistoryTracks(): List<TrackDto>
    fun clearHistory()
}