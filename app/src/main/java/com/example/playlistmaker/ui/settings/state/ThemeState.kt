package com.example.playlistmaker.ui.settings.state

sealed interface ThemeState {
    object Active : ThemeState
    object Deactive : ThemeState
}