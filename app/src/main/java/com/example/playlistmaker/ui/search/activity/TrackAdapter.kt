package com.example.playlistmaker.ui.search.activity

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.search.model.Track

class TrackAdapter(private var tracks: List<Track> = emptyList(),
                   private val clickListener: (Track) -> Unit,
                private val longClickListener: ((Track) -> Boolean)? = null
): RecyclerView.Adapter<TrackViewHolder> () {

    fun setItems(items: List<Track>) {
        tracks = items
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.track_view, parent, false)
        return TrackViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(tracks[position])
        holder.itemView.setOnClickListener {
            clickListener(tracks[position])
            notifyDataSetChanged()
        }
        // Долгое нажатие
        longClickListener?.let { longClick ->
            holder.itemView.setOnLongClickListener {
                longClick(tracks[position]) // Возвращаем результат longClick
            }
        }


    }

    override fun getItemCount(): Int {
        return tracks.size
    }

}