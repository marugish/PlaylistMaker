package com.example.playlistmaker.ui.player.activity

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.db.model.Playlist

class AddToPlaylistViewHolder(private val parentView: View): RecyclerView.ViewHolder(parentView) {
    private val playlistName: TextView = itemView.findViewById(R.id.playlist_name_text)
    private val trackCount: TextView = itemView.findViewById(R.id.track_count)
    private val playlistPhoto: ImageView = itemView.findViewById(R.id.playlist_image)

    fun bind(playlist: Playlist) {
        playlistName.text = playlist.playlistName

        Glide.with(parentView.context)
            .load(playlist.photoUrl)
            .centerCrop()
            .transform(RoundedCorners(8))
            .placeholder(R.drawable.placeholder)
            .into(playlistPhoto)

        val message = parentView.context.resources.getQuantityString(R.plurals.numberOfTracks,
            playlist.trackCount, playlist.trackCount)
        trackCount.text = message
    }
}