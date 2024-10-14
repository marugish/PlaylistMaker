package com.example.playlistmaker.data

interface MediaPlayerInterface {
    fun prepare(url: String)
    fun start()
    fun pause()
    fun release()
    fun getCurrentPosition(): Int
    fun getCurrentPlayerState(): Int
    fun changeState(state: Int)
}