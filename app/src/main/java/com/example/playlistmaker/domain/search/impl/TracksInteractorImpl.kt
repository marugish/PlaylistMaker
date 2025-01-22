package com.example.playlistmaker.domain.search.impl

import com.example.playlistmaker.domain.search.TracksInteractor
import com.example.playlistmaker.domain.search.TracksRepository
import com.example.playlistmaker.domain.search.model.Track
import com.example.playlistmaker.util.Resource
import com.example.playlistmaker.util.SearchError
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.concurrent.Executors

class TracksInteractorImpl(private val repository: TracksRepository) : TracksInteractor {

    override fun searchTracks(expression: String) : Flow<Pair<List<Track>?, SearchError?>> {
        return repository.searchTracks(expression).map { resource ->
            when(resource) {
                is Resource.Success -> {
                    Pair(resource.data, null)
                }
                is Resource.Error -> {
                    Pair(null, resource.message)
                }
            }
        }
    }
}