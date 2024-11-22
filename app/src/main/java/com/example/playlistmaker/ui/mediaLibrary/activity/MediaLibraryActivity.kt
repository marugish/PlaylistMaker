package com.example.playlistmaker.ui.mediaLibrary.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import com.example.playlistmaker.databinding.ActivityMediaLibraryBinding
import com.example.playlistmaker.ui.mediaLibrary.view_model.MediaLibraryViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class MediaLibraryActivity : ComponentActivity() {
    private val viewModel by viewModel<MediaLibraryViewModel>()
    //private lateinit var viewModel: MediaLibraryViewModel
    private lateinit var binding: ActivityMediaLibraryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMediaLibraryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //viewModel = ViewModelProvider(this)[MediaLibraryViewModel::class.java]
    }
}