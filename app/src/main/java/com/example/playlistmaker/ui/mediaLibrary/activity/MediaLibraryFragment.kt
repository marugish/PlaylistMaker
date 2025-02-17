package com.example.playlistmaker.ui.mediaLibrary.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentMediaLibraryBinding
import com.example.playlistmaker.ui.RootActivity
import com.google.android.material.tabs.TabLayoutMediator

class MediaLibraryFragment: Fragment() {
    companion object {
        private const val FAVORITES_KEY = "key_for_favorites"
        private const val PLAYLISTS_KEY = "key_for_playlists"
    }

    private lateinit var binding: FragmentMediaLibraryBinding
    private lateinit var tabMediator: TabLayoutMediator

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentMediaLibraryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as RootActivity).hideOrShowBottomNavigationView(View.VISIBLE)

        binding.viewPager.adapter = MediaLibraryViewPagerAdapter(
            fragmentManager = childFragmentManager,
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

    override fun onDestroyView() {
        super.onDestroyView()
        tabMediator.detach()
    }
}