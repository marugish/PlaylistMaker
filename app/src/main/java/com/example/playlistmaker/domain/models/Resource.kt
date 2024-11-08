package com.example.playlistmaker.domain.models

import com.example.playlistmaker.SearchError

sealed class Resource<T>(val data: T? = null, val message: SearchError? = null) {
    class Success<T>(data: T): Resource<T>(data)
    class Error<T>(message: SearchError, data: T? = null): Resource<T>(data, message)
}