package com.example.playlistmaker.util

sealed class Resource<T>(val data: T? = null, val message: SearchError? = null) {
    class Success<T>(data: T): Resource<T>(data)
    class Error<T>(message: SearchError, data: T? = null): Resource<T>(data, message)
}