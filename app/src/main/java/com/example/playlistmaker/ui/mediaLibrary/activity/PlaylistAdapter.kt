package com.example.playlistmaker.ui.mediaLibrary.activity

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.db.model.Playlist

class PlaylistAdapter(private var playlists: List<Playlist> = emptyList(),
                      private val clickListener: (Playlist) -> Unit): RecyclerView.Adapter<PlaylistViewHolder>() {

    fun setItems(items: List<Playlist>) {
        playlists = items
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.playlist_view, parent, false)
        return PlaylistViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        holder.bind(playlists[position])
        holder.itemView.setOnClickListener{
            clickListener(playlists[position])
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int {
        return playlists.size
    }
}
