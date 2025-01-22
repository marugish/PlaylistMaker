package com.example.playlistmaker.data.search

import com.example.playlistmaker.data.search.dto.NetworkResponse

interface NetworkClient {
    suspend fun doRequest(dto: Any): NetworkResponse
}