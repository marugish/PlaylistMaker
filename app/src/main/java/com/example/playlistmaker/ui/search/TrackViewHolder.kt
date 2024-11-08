package com.example.playlistmaker.ui.search

import android.annotation.SuppressLint
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.models.Track
import java.text.SimpleDateFormat
import java.util.Locale

class TrackViewHolder(private val parentView: View) : RecyclerView.ViewHolder(parentView) {
    private val trackName: TextView = parentView.findViewById(R.id.track_name_text)
    private val artistName: TextView = parentView.findViewById(R.id.artist_name_text)
    private val trackImage: ImageView = parentView.findViewById(R.id.track_image)

    @SuppressLint("SetTextI18n")
    fun bind(model: Track) {
        trackName.text = model.trackName
        val formattedTime = SimpleDateFormat("mm:ss", Locale.getDefault()).format(model.trackTimeMillis.toLong())
        artistName.text = model.artistName + "  •  " + formattedTime

        Glide.with(parentView.context)
            .load(model.artworkUrl100)
            .centerCrop()
            .transform(RoundedCorners(4)) // 2 совсем незаметно
            .placeholder(R.drawable.placeholder)
            .into(trackImage)
    }
}