package com.example.playlistmaker.ui.mediaLibrary.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.databinding.ActivityMediaLibraryBinding
import com.example.playlistmaker.ui.mediaLibrary.view_model.MediaLibraryViewModel


class MediaLibraryActivity : ComponentActivity() {
    private lateinit var viewModel: MediaLibraryViewModel
    private lateinit var binding: ActivityMediaLibraryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMediaLibraryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[MediaLibraryViewModel::class.java]
    }
}