package com.example.playlistmaker.data.search.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.example.playlistmaker.data.search.NetworkClient
import com.example.playlistmaker.data.search.dto.NetworkResponse
import com.example.playlistmaker.data.search.dto.TracksSearchRequest

class RetrofitNetworkClient(private val context: Context): NetworkClient {

    override fun doRequest(dto: Any): NetworkResponse {
        return try {
            if (!isConnected()) {
                NetworkResponse(-1)
            }
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

    private fun isConnected(): Boolean {
        val connectivityManager = context.getSystemService(
            Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> return true
            }
        }
        return false
    }
}