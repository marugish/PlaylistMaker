package com.example.playlistmaker.ui.player.activity

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.db.model.Playlist

class AddToPlaylistAdapter(private var playlists: List<Playlist> = emptyList(),
                           private val clickListener: (Playlist) -> Unit): RecyclerView.Adapter<AddToPlaylistViewHolder>(){
    fun setItems(items: List<Playlist>) {
        playlists = items
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddToPlaylistViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.add_to_playlist_view, parent, false)
        return AddToPlaylistViewHolder(view)
    }

    override fun onBindViewHolder(holder: AddToPlaylistViewHolder, position: Int) {
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









