package com.example.playlistmaker.data.search.impl

import com.example.playlistmaker.data.search.NetworkClient
import com.example.playlistmaker.util.SearchError
import com.example.playlistmaker.data.search.dto.TracksSearchRequest
import com.example.playlistmaker.data.search.dto.TracksSearchResponse
import com.example.playlistmaker.data.search.mapper.TracksMapper
import com.example.playlistmaker.domain.search.TracksRepository
import com.example.playlistmaker.util.Resource
import com.example.playlistmaker.domain.search.model.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TracksRepositoryImpl(
    private val networkClient: NetworkClient,
    private val trackMapper: TracksMapper
) : TracksRepository {
    override fun searchTracks(expression: String): Flow<Resource<List<Track>>> = flow {
        val response = networkClient.doRequest(TracksSearchRequest(expression))
        when (response.resultCode) {
            -1 -> {
                emit(Resource.Error(SearchError.NETWORK_ERROR))
            }
            200 -> {
                if ((response as TracksSearchResponse).results.isEmpty()) {
                    emit(Resource.Error(SearchError.NO_RESULTS))
                } else {
                    emit(Resource.Success(response.results.map(trackMapper::mapToDomain)))
                }
            }
            else -> {
                emit(Resource.Error(SearchError.NETWORK_ERROR))
            }
        }
    }
}