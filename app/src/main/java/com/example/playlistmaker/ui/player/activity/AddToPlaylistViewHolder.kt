package com.example.playlistmaker.ui.player.activity

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.db.model.Playlist

class AddToPlaylistViewHolder(view: View): RecyclerView.ViewHolder(view) {
    private val playlistName: TextView = itemView.findViewById(R.id.playlist_name_text)
    private val trackCount: TextView = itemView.findViewById(R.id.track_count)
    private val playlistPhoto: ImageView = itemView.findViewById(R.id.playlist_image)

    fun bind(playlist: Playlist) {
        playlistName.text = playlist.playlistName
        // картинка, иначе заглушка
        // ...

        // добавить окончание трека
        trackCount.text = playlist.trackCount.toString()
    }
}