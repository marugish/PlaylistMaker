package com.example.playlistmaker

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class PlayActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play)

        val toolbarBack = findViewById<Toolbar>(R.id.toolbar_play)
        toolbarBack.setNavigationOnClickListener {
            finish()
        }

        val track = intent.getSerializableExtra("track") as Track?

        val artistName = findViewById<TextView>(R.id.singerTextView)
        val trackName = findViewById<TextView>(R.id.trackTextView)
        val trackDuration = findViewById<TextView>(R.id.durationEditText)
        val trackImage = findViewById<ImageView>(R.id.trackImageView)
        val trackAlbum = findViewById<TextView>(R.id.albumEditText)
        val trackYear = findViewById<TextView>(R.id.yearEditText)
        val trackGenre = findViewById<TextView>(R.id.genreEditText)
        val trackCountry = findViewById<TextView>(R.id.countryText)

        if (track != null) {
            trackName.text = track.trackName
            artistName.text = track.artistName
            val formattedTime = SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis.toLong())
            trackDuration.text = formattedTime
            fun getCoverArtwork() = track.artworkUrl100.replaceAfterLast('/',"512x512bb.jpg")
            Glide.with(this)
                .load(getCoverArtwork())
                .centerCrop()
                .transform(RoundedCorners(8))
                .placeholder(R.drawable.placeholder)
                .into(trackImage)
            trackAlbum.text = track.collectionName
            val formatter = SimpleDateFormat("yyyy", Locale.getDefault())
            val date: Date? = formatter.parse(track.releaseDate)
            val year = date?.let { formatter.format(it) } // Форматирование Date в год
            trackYear.text = year
            trackGenre.text = track.primaryGenreName
            trackCountry.text = track.country
        }
    }
}