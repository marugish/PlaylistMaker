package com.example.playlistmaker.data.sharing.impl

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.example.playlistmaker.data.sharing.ExternalNavigator
import com.example.playlistmaker.domain.sharing.model.EmailData

class ExternalNavigatorImpl(val context: Context): ExternalNavigator {
    override fun shareLink(shareLink: String) {
        //Log.d("ExternalNavigator", "$context")
        //Log.d("ExternalNavigator", "$shareLink")
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        shareIntent.setType("text/plain")
        shareIntent.putExtra(Intent.EXTRA_TEXT, "URL")
        val new_intent = Intent.createChooser(shareIntent, "Share via")
        new_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(new_intent)

        /*val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.setType("text/plain")
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareLink)
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        if (shareIntent.resolveActivity(context.packageManager) != null)
            context.startActivity(Intent.createChooser(shareIntent, "Поделиться приложением"))
        else
            Log.e("ShareIntent", "No application available to share the link.")
        try {
            context.startActivity(Intent.createChooser(shareIntent, "Поделиться приложением"))
        } catch (e: ActivityNotFoundException) {
            Log.e("ExternalNavigator", "No activity found to handle the intent.", e)
        }*/

    }

    override fun openEmail(emailData: EmailData) {
        val shareIntent = Intent(Intent.ACTION_SENDTO)
        shareIntent.data = Uri.parse("mailto:")
        shareIntent.putExtra(Intent.EXTRA_EMAIL, emailData.email)
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, emailData.themeOfMessage)
        shareIntent.putExtra(Intent.EXTRA_TEXT, emailData.message)
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(shareIntent)
    }

    override fun openLink(link: String) {
        val openLink = Intent(Intent.ACTION_VIEW)
        openLink.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        openLink.data = Uri.parse(link)
        context.startActivity(openLink)
    }
}