package com.example.playlistmaker

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class TrackAdapter(private val tracks: List<Track>, private val searchHistory: SearchHistory): RecyclerView.Adapter<TrackViewHolder> () {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.track_view, parent, false)
        return TrackViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(tracks[position])
        val context = holder.itemView.context
        holder.itemView.setOnClickListener {
            Toast.makeText(context, "Нажатие на элемент номер $position", Toast.LENGTH_LONG).show()
            // перепроверить логику
            searchHistory.write(tracks[position])
        }
    }

    override fun getItemCount(): Int {
        return tracks.size
    }

}