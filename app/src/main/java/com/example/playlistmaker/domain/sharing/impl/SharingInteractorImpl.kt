package com.example.playlistmaker.domain.sharing.impl

import com.example.playlistmaker.data.sharing.ExternalNavigator
import com.example.playlistmaker.data.sharing.StringProvider
import com.example.playlistmaker.domain.sharing.SharingInteractor
import com.example.playlistmaker.domain.sharing.model.EmailData

class SharingInteractorImpl(
    private val externalNavigator: ExternalNavigator,
    private val stringProvider: StringProvider
) : SharingInteractor {

    override fun shareApp() {
        externalNavigator.shareLink(stringProvider.getShareAppLink())
    }

    override fun openTerms() {
        externalNavigator.openLink(stringProvider.getTermsLink())
    }

    override fun openSupport() {
        externalNavigator.openEmail(getSupportEmailData())
    }

    /*override fun sharePlaylist() {
        // возможно нужно получить строку
    }*/

    private fun getSupportEmailData(): EmailData {
        return EmailData(
            stringProvider.getSupportEmail(),
            stringProvider.getSupportEmailSubject(),
            stringProvider.getSupportEmailMessage()
        )
    }

}