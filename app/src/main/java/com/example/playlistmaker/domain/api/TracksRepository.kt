package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.models.Resource
import com.example.playlistmaker.domain.models.Track

interface TracksRepository {
    fun searchTracks(expression: String): Resource<List<Track>>
}