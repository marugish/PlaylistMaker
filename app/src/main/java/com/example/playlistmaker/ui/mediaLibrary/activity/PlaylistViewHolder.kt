package com.example.playlistmaker.ui.mediaLibrary.activity

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.db.model.Playlist

class PlaylistViewHolder(view: View): RecyclerView.ViewHolder(view) {
    private val playlistName: TextView = itemView.findViewById(R.id.playlistName)
    private val trackCount: TextView = itemView.findViewById(R.id.trackCount)
    // картинка

    fun bind(playlist: Playlist) {
        playlistName.text = playlist.playlistName
        // картинка, иначе заглушка
        // ...

        // добавить окончание трека
        trackCount.text = playlist.trackCount.toString()




    }
}