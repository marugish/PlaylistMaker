package com.example.playlistmaker.ui.mediaLibrary.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.playlistmaker.ui.mediaLibrary.view_model.EditPlaylistViewModel
import com.example.playlistmaker.util.customToast
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import kotlin.random.Random

class EditPlaylistFragment: NewPlaylistFragment() {
    private val playlistId: Long? by lazy {
        arguments?.getLong("playlist_id")
    }
    private val editViewModel: EditPlaylistViewModel by viewModel { parametersOf(playlistId) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        editViewModel.playlistData.observe(viewLifecycleOwner) { playlist ->
            if (playlist != null) {
                binding.apply {
                    newPlaylistName.setText(playlist.playlistName)
                    newPlaylistDescription.setText(playlist.playlistDescription)
                    createNewPlaylist.text = "Сохранить"
                    toolbarNewPlaylist.title = "Редактировать"
                    Glide.with(requireContext())
                        .load(playlist.photoUrl)
                        .centerCrop()
                        .into(addPhoto)
                    newPlaylist = playlist
                }
            } else {
                customToast(requireContext(), layoutInflater, "Произошла ошибка. Плейлист не найден")
                findNavController().popBackStack()
            }
        }

        // Кнопка Назад toolbar
        binding.toolbarNewPlaylist.setOnClickListener {
            findNavController().popBackStack()
        }

        // Кнопка Back системная
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().popBackStack()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        // Кнопка Сохранить
        binding.createNewPlaylist.setOnClickListener {
            photoUri?.let { it ->
                val randomNumber = Random.nextInt(10000)
                saveImageToPrivateStorage(it, "${newPlaylist.playlistName}_$randomNumber")
            }
            editViewModel.updatePlaylistInDb(newPlaylist = newPlaylist)
            // возвращаемся на предыдущий экран
            // NEW
            val result = Bundle().apply {
                putSerializable("updatedPlaylist", newPlaylist)             }
            setFragmentResult("requestKey", result)
            // NEW
            findNavController().popBackStack()
        }
    }
}