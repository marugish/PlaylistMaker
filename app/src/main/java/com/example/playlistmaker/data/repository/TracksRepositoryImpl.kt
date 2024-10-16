package com.example.playlistmaker.data.repository

import com.example.playlistmaker.data.NetworkClient
import com.example.playlistmaker.data.dto.TracksSearchRequest
import com.example.playlistmaker.data.dto.TracksSearchResponse
import com.example.playlistmaker.data.mapper.TracksMapper
import com.example.playlistmaker.domain.api.TracksRepository
import com.example.playlistmaker.domain.models.Resource
import com.example.playlistmaker.domain.models.Track

class TracksRepositoryImpl(private val networkClient: NetworkClient) : TracksRepository {
    override fun searchTracks(expression: String): Resource<List<Track>> {
        val response = networkClient.doRequest(TracksSearchRequest(expression))
        return if (response.resultCode == 200) {
            if ((response as TracksSearchResponse).results.isEmpty()) {
                Resource.Error("Ничего не найдено")
            } else {
                Resource.Success(TracksMapper.mapToDomain(response.results))
            }
        } else {
            Resource.Error("Произошла сетевая ошибка")
        }
    }
}



