package com.example.playlistmaker

enum class SearchError(private val error: String) {
    NO_RESULTS("Ничего не найдено"),
    NETWORK_ERROR("Произошла сетевая ошибка"),
}