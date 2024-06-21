package com.example.playlistmaker

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val buttonSearch = findViewById<Button>(R.id.button_search)
        val buttonMediaLibrary = findViewById<Button>(R.id.button_media_library)
        val buttonSettings = findViewById<Button>(R.id.button_settings)

        val buttonSearchClickListener: View.OnClickListener = object : View.OnClickListener {
            override fun onClick(v: View?) {
                v?.context?.startActivity(Intent(v.context, SearchActivity::class.java))
            }
        }
        buttonSearch.setOnClickListener(buttonSearchClickListener)

        buttonMediaLibrary.setOnClickListener {
            startActivity(Intent(this, MediaLibraryActivity::class.java))
        }

        buttonSettings.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }

    }
}