package com.example.playlistmaker.data.search.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.example.playlistmaker.data.search.NetworkClient
import com.example.playlistmaker.data.search.dto.NetworkResponse
import com.example.playlistmaker.data.search.dto.TracksSearchRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RetrofitNetworkClient(private val itunesApiService: ItunesApi, private val context: Context): NetworkClient {

    override suspend fun doRequest(dto: Any): NetworkResponse {
        if (!isConnected()) {
            return NetworkResponse(-1)
        }

        if (dto !is TracksSearchRequest) {
            return NetworkResponse( 400)
        }

        return withContext(Dispatchers.IO) {
            try {
                val response = itunesApiService.searchTrack(dto.request)
                response.apply { resultCode = 200 }
            } catch (e: Throwable) {
                NetworkResponse(500)
            }
        }
        /*return try {
                if (!isConnected()) {
                    NetworkResponse(-1)
                }
                if (dto is TracksSearchRequest) {
                    val resp = itunesApiService.searchTrack(dto.request).execute()//RetrofitItunesClient.itunesService.searchTrack(dto.request).execute()
                    val body = resp.body() ?: NetworkResponse()
                    body.apply { resultCode = resp.code() }
                } else {
                    NetworkResponse(400)
                }
            } catch (ex: Exception) {
                NetworkResponse(400)
            }*/


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