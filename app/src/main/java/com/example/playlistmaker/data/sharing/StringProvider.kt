package com.example.playlistmaker.data.sharing

import com.example.playlistmaker.domain.db.model.Playlist
import com.example.playlistmaker.domain.search.model.Track

interface StringProvider {
    fun getShareAppLink(): String
    fun getSupportEmail(): String
    fun getSupportEmailSubject(): String
    fun getSupportEmailMessage(): String
    fun getTermsLink(): String
    fun getPlaylistMessage(playlist: Playlist, tracks: List<Track>): String
}