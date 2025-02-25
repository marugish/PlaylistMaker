package com.example.playlistmaker.data.sharing.impl

import android.content.Context
import com.example.playlistmaker.R
import com.example.playlistmaker.data.search.dto.TrackDto
import com.example.playlistmaker.data.sharing.StringProvider
import com.example.playlistmaker.domain.db.model.Playlist
import com.example.playlistmaker.domain.search.model.Track
import com.example.playlistmaker.util.getTrackCountMessage
import java.text.SimpleDateFormat
import java.util.Locale

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

    override fun getPlaylistMessage(playlist: Playlist, tracks: List<Track>): String {
        val messageBuilder = StringBuilder()
        messageBuilder.append("${playlist.playlistName}\n")
        messageBuilder.append("${playlist.playlistDescription}\n")
        messageBuilder.append("${getTrackCountMessage(playlist.trackCount)} \n\n")
        // Формируем список треков
        tracks.forEachIndexed { index, track ->
            val formattedTime = SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis.toLong())
            messageBuilder.append("${index + 1}. ${track.artistName} - ${track.trackName} (${formattedTime})\n")
        }
        return messageBuilder.toString()
    }



}