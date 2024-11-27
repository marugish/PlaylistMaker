package com.example.playlistmaker.ui.mediaLibrary.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityMediaLibraryBinding
import com.google.android.material.tabs.TabLayoutMediator

const val FAVORITES_KEY = "key_for_favorites"
const val PLAYLISTS_KEY = "key_for_playlists"

class MediaLibraryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMediaLibraryBinding

    private lateinit var tabMediator: TabLayoutMediator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMediaLibraryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbarMediaLibrary.setNavigationOnClickListener {
            finish()
        }

        binding.viewPager.adapter = MediaLibraryViewPagerAdapter(
            fragmentManager = supportFragmentManager,
            lifecycle = lifecycle,
            favorites = FAVORITES_KEY,
            playlists = PLAYLISTS_KEY
        )

        tabMediator = TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when(position) {
                0 -> tab.text = getString(R.string.favorites)
                1 -> tab.text = getString(R.string.playlists)
            }
        }
        tabMediator.attach()
    }

    override fun onDestroy() {
        super.onDestroy()
        tabMediator.detach()
    }
}
