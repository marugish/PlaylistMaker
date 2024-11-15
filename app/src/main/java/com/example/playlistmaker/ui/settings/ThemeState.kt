package com.example.playlistmaker.ui.settings

sealed interface ThemeState {
    object Active : ThemeState
    object Deactive : ThemeState
}