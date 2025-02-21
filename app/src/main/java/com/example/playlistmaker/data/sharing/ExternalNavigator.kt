package com.example.playlistmaker.data.sharing

import com.example.playlistmaker.domain.sharing.model.EmailData

interface ExternalNavigator {
    fun shareLink(shareLink: String)
    fun openEmail(emailData: EmailData)
    fun openLink(link: String)
    //fun sharePlaylist(message: String)
}