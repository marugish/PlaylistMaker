package com.example.playlistmaker

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.databinding.ActivityMainBinding
import com.example.playlistmaker.ui.mediaLibrary.activity.MediaLibraryActivity
import com.example.playlistmaker.ui.search.activity.SearchActivity
import com.example.playlistmaker.ui.settings.ThemeState
import com.example.playlistmaker.ui.settings.activity.SettingsActivity
import com.example.playlistmaker.ui.settings.view_model.SettingsViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: SettingsViewModel
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Инициализируем
        viewModel = ViewModelProvider(this, SettingsViewModel.getViewModelFactory())[SettingsViewModel::class.java]

        /*viewModel.observeThemeState().observe(this) {
            render(it)
        }*/



        binding.buttonSearch.setOnClickListener {
            startActivity(Intent(this, SearchActivity::class.java))
        }

        binding.buttonMediaLibrary.setOnClickListener {
            startActivity(Intent(this, MediaLibraryActivity::class.java))
        }

        binding.buttonSettings.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }

    }

    private fun render(themeState: ThemeState) {
        when (themeState) {
            is ThemeState.Active -> (applicationContext as App).switchTheme(true)
            is ThemeState.Deactive -> (applicationContext as App).switchTheme(false)
        }
    }
}