package com.example.playlistmaker.domain.search

import com.example.playlistmaker.util.SearchError
import com.example.playlistmaker.domain.search.model.Track
import kotlinx.coroutines.flow.Flow

interface TracksInteractor {
    fun searchTracks(expression: String) : Flow<Pair<List<Track>?, SearchError?>>
}