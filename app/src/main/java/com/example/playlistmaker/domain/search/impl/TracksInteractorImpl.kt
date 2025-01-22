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
    //private val executor = Executors.newCachedThreadPool()

    override fun searchTracks(expression: String) : Flow<Pair<List<Track>?, SearchError?>> {//, consumer: TracksInteractor.TracksConsumer) {
        //executor.execute {
    return repository.searchTracks(expression).map { result ->
        when(result) {
            is Resource.Success -> {
                Pair(result.data, null)//consumer.consume(resource.data, null)
            }
            is Resource.Error -> {
                Pair(null, result.message)//consumer.consume(null, resource.message)
            }
        }
    }
            /*when(val resource = repository.searchTracks(expression)) {
                is Resource.Success -> { consumer.consume(resource.data, null) }
                is Resource.Error -> { consumer.consume(null, resource.message) }
            }*/
        //}
    }
}