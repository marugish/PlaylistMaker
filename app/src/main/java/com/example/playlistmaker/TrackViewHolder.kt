package com.example.playlistmaker

import android.annotation.SuppressLint
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

class TrackViewHolder(private val parentView: View) : RecyclerView.ViewHolder(parentView) {
    private val trackName: TextView
    private val artistName: TextView
    private val trackImage: ImageView

    init {
        trackName = parentView.findViewById(R.id.track_name_text)
        artistName = parentView.findViewById(R.id.artist_name_text)
        trackImage = parentView.findViewById(R.id.track_image)
    }

    @SuppressLint("SetTextI18n")
    fun bind(model: Track) {
        trackName.text = model.trackName
        artistName.text = model.artistName + "  •  " + model.trackTime

        Glide.with(parentView.context)
            .load(model.artworkUrl100)
            .centerCrop()
            .transform(RoundedCorners(4)) // 2 совсем незаметно
            .placeholder(R.drawable.placeholder)
            .into(trackImage)
    }
}