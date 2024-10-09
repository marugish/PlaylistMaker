package com.example.playlistmaker.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.SearchHistory
import com.example.playlistmaker.domain.models.Track

class TrackAdapter(private val tracks: List<Track>,
                   private val clickListener: (Track) -> Unit,
                   private val searchHistory: SearchHistory
): RecyclerView.Adapter<TrackViewHolder> () {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.track_view, parent, false)
        return TrackViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(tracks[position])
        holder.itemView.setOnClickListener {
            clickListener(tracks[position])
            searchHistory.write(tracks[position])
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int {
        return tracks.size
    }

}