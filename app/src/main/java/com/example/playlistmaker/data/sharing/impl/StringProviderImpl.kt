package com.example.playlistmaker.data.sharing.impl

import android.content.Context
import com.example.playlistmaker.R
import com.example.playlistmaker.data.sharing.StringProvider

class StringProviderImpl(private val context: Context): StringProvider {
    override fun getShareAppLink(): String {
        return context.getString(R.string.share_link)
    }

    override fun getSupportEmail(): String {
        return context.getString(R.string.email)
    }

    override fun getSupportEmailSubject(): String {
        return context.getString(R.string.theme_of_message)
    }

    override fun getSupportEmailMessage(): String {
        return context.getString(R.string.message)
    }

    override fun getTermsLink(): String {
        return context.getString(R.string.address)
    }

    /*override fun getPlaylistMessage(tracks: List<Track>): String {
        TODO("Not yet implemented")
    }*/


}