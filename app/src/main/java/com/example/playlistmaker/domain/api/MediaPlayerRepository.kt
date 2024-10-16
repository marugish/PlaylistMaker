package com.example.playlistmaker.domain.api

interface MediaPlayerRepository {
    fun prepare(url: String)
    fun start()
    fun pause()
    fun release()
    fun getCurrentPosition(): Int
    fun getCurrentPlayerState(): Int
    fun changeState(state: Int)
}