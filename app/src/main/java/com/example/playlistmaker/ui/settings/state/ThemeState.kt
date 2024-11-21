package com.example.playlistmaker.ui.settings.state

sealed interface ThemeState {
    data object Active : ThemeState
    data object Deactive : ThemeState
}