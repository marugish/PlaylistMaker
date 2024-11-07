package com.example.playlistmaker.data.network

import com.example.playlistmaker.data.NetworkClient
import com.example.playlistmaker.data.dto.NetworkResponse
import com.example.playlistmaker.data.dto.TracksSearchRequest

class RetrofitNetworkClient: NetworkClient {

    override fun doRequest(dto: Any): NetworkResponse {
        return try {
            if (dto is TracksSearchRequest) {
                val resp = RetrofitItunesClient.itunesService.searchTrack(dto.request).execute()
                val body = resp.body() ?: NetworkResponse()
                body.apply { resultCode = resp.code() }
            } else {
                NetworkResponse(400)
            }
        } catch (ex: Exception) {
            NetworkResponse(400)
        }
    }
}