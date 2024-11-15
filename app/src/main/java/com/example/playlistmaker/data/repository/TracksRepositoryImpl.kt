package com.example.playlistmaker.data.repository

import com.example.playlistmaker.util.SearchError
import com.example.playlistmaker.data.NetworkClient
import com.example.playlistmaker.data.dto.TracksSearchRequest
import com.example.playlistmaker.data.dto.TracksSearchResponse
import com.example.playlistmaker.data.mapper.TracksMapper
import com.example.playlistmaker.domain.search.TracksRepository
import com.example.playlistmaker.util.Resource
import com.example.playlistmaker.domain.search.model.Track

class TracksRepositoryImpl(
    private val networkClient: NetworkClient,
    private val trackMapper: TracksMapper
) : TracksRepository {
    override fun searchTracks(expression: String): Resource<List<Track>> {
        val response = networkClient.doRequest(TracksSearchRequest(expression))
        return when (response.resultCode) {
            -1 -> {
                Resource.Error(SearchError.NETWORK_ERROR)
            }
            200 -> {
                if ((response as TracksSearchResponse).results.isEmpty()) {
                    Resource.Error(SearchError.NO_RESULTS)
                } else {
                    Resource.Success(response.results.map(trackMapper::mapToDomain))
                }
            }
            else -> {
                Resource.Error(SearchError.NETWORK_ERROR)
            }
        }
    }
}