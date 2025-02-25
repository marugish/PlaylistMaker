package com.example.playlistmaker.ui.mediaLibrary.activity

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistBinding
import com.example.playlistmaker.domain.db.model.Playlist
import com.example.playlistmaker.domain.search.model.Track
import com.example.playlistmaker.ui.RootActivity
import com.example.playlistmaker.ui.mediaLibrary.state.PlaylistState
import com.example.playlistmaker.ui.mediaLibrary.state.PlaylistsState
import com.example.playlistmaker.ui.mediaLibrary.view_model.PlaylistViewModel
import com.example.playlistmaker.ui.search.activity.TrackAdapter
import com.example.playlistmaker.util.customToast
import com.example.playlistmaker.util.getTrackCountMessage
import com.example.playlistmaker.util.getTrackCountMinutes
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.text.SimpleDateFormat
import java.util.Locale

class PlaylistFragment: Fragment() {
    private lateinit var binding: FragmentPlaylistBinding

    private val playlistId: Long? by lazy {
        arguments?.getLong("playlist")
    }
    private val viewModel: PlaylistViewModel by viewModel { parametersOf(playlistId) }

    private var tracksAdapter: TrackAdapter? = TrackAdapter(clickListener = {track -> showTrackPlayer(track) },
        longClickListener = {longClickedTrack -> deleteTrackInPlaylist(longClickedTrack)})

    private fun showTrackPlayer(track: Track) {
        if (viewModel.clickDebounce()) {
            val bundle = Bundle()
            bundle.putSerializable("track", track)
            findNavController().navigate(R.id.playFragment, bundle)
            (activity as RootActivity).hideOrShowBottomNavigationView(View.GONE)

            // !!!!!!!!!!!!!!!!!
            //favoritesViewModel.saveSearchHistory(track)
        }
    }

    private fun deleteTrackInPlaylist(track: Track): Boolean {
        Log.i("myPlaylist", "Long click to track")
        MaterialAlertDialogBuilder(requireContext(), R.style.CustomDialogStyle)
            .setMessage("Хотите удалить трек?")
            .setNegativeButton("Нет") { _, _ ->
                // ничего не делаем
            }.setPositiveButton("Да") { _, _ ->
                //findNavController().popBackStack()
                Log.i("myPlaylist", "Yes")
                viewModel.deleteTrackInPlaylist(track.trackId)
            }.show()
        return true // обработали долгое нажатие
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tracksRecycleView.layoutManager = LinearLayoutManager(requireContext())
        binding.tracksRecycleView.adapter = tracksAdapter

        binding.toolbarPlaylist.setNavigationOnClickListener {
            findNavController().popBackStack()
            (activity as RootActivity).hideOrShowBottomNavigationView(View.VISIBLE)
        }

        viewModel.observeState().observe(viewLifecycleOwner) {
            when(it) {
                is PlaylistState.Content -> showPlaylist(it.playlist, it.tracks)
                is PlaylistState.Empty -> showEmptyList()
            }
        }

        binding.share.setOnClickListener {
            viewModel.sharePlaylist()
        }

        viewModel.share.observe(viewLifecycleOwner) {
            if (it == false) {
                customToast(requireContext(), layoutInflater, "В этом плейлисте нет списка треков, которым можно поделиться")
            }
        }

        val bottomSheetBehavior = BottomSheetBehavior.from(binding.threeDotBottomSheet).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }

        bottomSheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        binding.overlay.visibility = View.GONE
                    }
                    else -> {
                        binding.overlay.visibility = View.VISIBLE

                        binding.sharePlaylist.setOnClickListener {
                            viewModel.sharePlaylist()
                        }

                        binding.editPlaylist.setOnClickListener {
                            Log.i("myPlaylist", "editPlaylist")
                            val bundle = Bundle()
                            playlistId?.let { it1 -> bundle.putLong("playlist_id", it1) }
                            findNavController().navigate(R.id.editPlaylistFragment, bundle)
                            //(activity as RootActivity).hideOrShowBottomNavigationView(View.GONE)
                        }

                        binding.deletePlaylist.setOnClickListener {
                            Log.i("myPlaylist", "deletePlaylist")
                            MaterialAlertDialogBuilder(requireContext(), R.style.CustomDialogStyle)
                                .setMessage("Хотите удалить плейлист?")
                                .setNegativeButton("Нет") { _, _ ->
                                    // ничего не делаем
                                }.setPositiveButton("Да") { _, _ ->
                                    viewModel.deletePlaylist()
                                    findNavController().popBackStack()
                                }.show()
                        }

                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
        })

        binding.threeDot.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
        }

        setFragmentResultListener("requestKey") { _, bundle ->
            val updatedPlaylist = bundle.getSerializable("updatedPlaylist") as? Playlist
            viewModel.getPlaylistById()

        }

    }

    private fun showEmptyList() {
        binding.apply {
            playlistTextView.text = requireContext().getString(R.string.playlist_name)
            descriptionTextView.text = requireContext().getString(R.string.new_playlist_description)
            imageView.setImageResource(R.drawable.not_found_placeholder)
            tracksRecycleView.visibility = View.GONE
            placeholderMessage.text = requireContext().getString(R.string.no_tracks_in_playlist)
            placeholderMessage.visibility = View.VISIBLE
        }
    }

    private fun showPlaylist(playlist: Playlist, tracks: List<Track>?) {
        binding.apply {
            playlistTextView.text = playlist.playlistName
            if (playlist.playlistDescription.isNullOrEmpty()) {
                descriptionTextView.visibility = View.GONE
            } else {
                descriptionTextView.text = playlist.playlistDescription
            }
            Glide.with(requireContext())
                .load(playlist.photoUrl)
                .centerCrop()
                .placeholder(R.drawable.placeholder)
                .into(imageView)

            // вычисление Общей продолжительности всех треков
            if (tracks != null) {
                val durationSumMillis = tracks.sumOf { it.trackTimeMillis.toLong() }
                val duration = SimpleDateFormat("mm", Locale.getDefault()).format(durationSumMillis)
                durationPlaylist.text = getTrackCountMinutes(duration.toInt())
            }

            // three dot
            playlistNameText.text = playlist.playlistName
            Glide.with(requireContext())
                .load(playlist.photoUrl)
                .centerCrop()
                .transform(RoundedCorners(2))
                .placeholder(R.drawable.placeholder)
                .into(binding.playlistImage)

            // количество треков
            val count = getTrackCountMessage(playlist.trackCount)
            trackCountPlaylist.text = count
            trackCount.text = count

            // отобразить список треков
            if (tracks.isNullOrEmpty()) {
                placeholderMessage.text = requireContext().getString(R.string.no_tracks_in_playlist)
                placeholderMessage.visibility = View.VISIBLE
                tracksRecycleView.visibility = View.GONE
            } else {
                tracksRecycleView.visibility = View.VISIBLE
                placeholderMessage.visibility = View.GONE
                tracksAdapter?.setItems(tracks)
            }
        }
    }
}