package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.models.Track

interface TracksRepository {
    // сделать через Resource
    fun searchTracks(expression: String): List<Track>
}