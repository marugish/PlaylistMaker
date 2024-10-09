package com.example.playlistmaker.data.network

import com.example.playlistmaker.data.NetworkClient
import com.example.playlistmaker.data.dto.NetworkResponse
import com.example.playlistmaker.data.dto.TracksSearchRequest

class RetrofitNetworkClient: NetworkClient {

    // тут на вход получаем dto
    // сделать try {} catch(ex: Exception) {}

    override fun doRequest(dto: Any): NetworkResponse {
        return if (dto is TracksSearchRequest) {
            val resp = RetrofitItunesClient.itunesService.searchTrack(dto.request).execute()

            val body = resp.body() ?: NetworkResponse()

            body.apply { resultCode = resp.code() }
        } else {
            NetworkResponse().apply { resultCode = 400 }
        }
    }
}