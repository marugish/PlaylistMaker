package com.example.playlistmaker.data.sharing.impl

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.example.playlistmaker.data.sharing.ExternalNavigator
import com.example.playlistmaker.domain.sharing.model.EmailData

class ExternalNavigatorImpl(val context: Context):ExternalNavigator {
    override fun shareLink(shareLink: String) {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.setType("text/plain")
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareLink)
        context.startActivity(Intent.createChooser(shareIntent, "Поделиться приложением"))
    }

    override fun openEmail(emailData: EmailData) {
        val shareIntent = Intent(Intent.ACTION_SENDTO)
        shareIntent.data = Uri.parse("mailto:")
        shareIntent.putExtra(Intent.EXTRA_EMAIL, emailData.email)
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, emailData.themeOfMessage)
        shareIntent.putExtra(Intent.EXTRA_TEXT, emailData.message)
        context.startActivity(shareIntent)
    }

    override fun openLink(link: String) {
        val openLink = Intent(Intent.ACTION_VIEW)
        openLink.data = Uri.parse(link)
        context.startActivity(openLink)
    }
}