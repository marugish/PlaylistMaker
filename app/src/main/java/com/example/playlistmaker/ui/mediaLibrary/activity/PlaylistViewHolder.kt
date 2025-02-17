package com.example.playlistmaker.ui.mediaLibrary.activity

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.db.model.Playlist

class PlaylistViewHolder(private val parentView: View): RecyclerView.ViewHolder(parentView) {
    private val playlistName: TextView = itemView.findViewById(R.id.playlistName)
    private val trackCount: TextView = itemView.findViewById(R.id.trackCount)
    private val playlistPhoto: ImageView = itemView.findViewById(R.id.track_image)

    private fun getTrackCountMessage(trackCount: Int): String {
        return when {
            trackCount % 10 == 1 && trackCount % 100 != 11 -> "$trackCount трек"
            trackCount % 10 in 2..4 && (trackCount % 100 !in 12..14) -> "$trackCount трека"
            else -> "$trackCount треков"
        }
    }

    fun bind(playlist: Playlist) {
        playlistName.text = playlist.playlistName

        Glide.with(parentView.context)
            .load(playlist.photoUrl)
            .placeholder(R.drawable.placeholder)
            .into(playlistPhoto)

        trackCount.text = getTrackCountMessage(playlist.trackCount)
    }
}