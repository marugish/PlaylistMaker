package com.example.playlistmaker.data.sharing

interface StringProvider {
    fun getShareAppLink(): String
    fun getSupportEmail(): String
    fun getSupportEmailSubject(): String
    fun getSupportEmailMessage(): String
    fun getTermsLink(): String
    //fun getPlaylistMessage(tracks: List<Track>): String
}