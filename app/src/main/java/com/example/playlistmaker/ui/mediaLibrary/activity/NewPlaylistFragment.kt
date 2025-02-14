package com.example.playlistmaker.ui.mediaLibrary.activity

import android.Manifest
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentNewPlaylistBinding
import com.example.playlistmaker.domain.db.model.Playlist
import com.example.playlistmaker.ui.mediaLibrary.view_model.NewPlaylistViewModel
import com.example.playlistmaker.util.customToast
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.io.FileOutputStream
import kotlin.random.Random


class NewPlaylistFragment : Fragment() {
    private lateinit var binding: FragmentNewPlaylistBinding

    private val viewModel by viewModel<NewPlaylistViewModel>()

    private lateinit var confirmDialog: MaterialAlertDialogBuilder

    private var newPlaylist: Playlist = Playlist()
    private var photoUri: Uri? = null

    private lateinit var pickMedia: ActivityResultLauncher<PickVisualMediaRequest>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = FragmentNewPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        confirmDialog = MaterialAlertDialogBuilder(requireContext(), R.style.CustomDialogStyle)
            .setTitle("Завершить создание плейлиста?")
            .setMessage("Все несохраненные данные будут потеряны")
            .setNeutralButton("Отмена") { _, _ ->
                // ничего не делаем
            }.setPositiveButton("Завершить") { _, _ ->
                findNavController().popBackStack()
            }

        binding.toolbarNewPlaylist.setOnClickListener {
            checkEnteredInfo()
        }

        // Регистрируем событие, которое вызывает Photo picker
        pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                Glide.with(this)
                    .load(uri)
                    .placeholder(R.drawable.add_photo)
                    .into(binding.addPhoto)

                photoUri = uri
            } else {
                Log.d("PhotoPicker", "No media selected")
            }
        }

        binding.addPhoto.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        binding.newPlaylistName.addTextChangedListener(
            afterTextChanged = { s ->
                val inputText = s.toString()
                binding.createNewPlaylist.isEnabled = inputText.isNotEmpty()
                newPlaylist = newPlaylist.copy(playlistName = inputText)
            }
        )

        binding.newPlaylistDescription.addTextChangedListener(
            afterTextChanged = { s ->
                newPlaylist = newPlaylist.copy(playlistDescription = s.toString())
            }
        )

        // Кнопка Back
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                checkEnteredInfo()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        binding.createNewPlaylist.setOnClickListener {
            // добавляем картинку в хранилище
            photoUri?.let { it ->
                val randomNumber = Random.nextInt(10000)
                saveImageToPrivateStorage(it, "${newPlaylist.playlistName}_$randomNumber")
            }
            // добавляем плейлист в БД
            viewModel.insertPlaylistToDb(newPlaylist = newPlaylist) // на результат добавления подписываемся
        }

        // Результат добавления плейлиста
        viewModel.idPlaylist.observe(viewLifecycleOwner) {
            if (it != -1L) {
                findNavController().popBackStack()
                customToast(requireContext(), layoutInflater,"Плейлист '${newPlaylist.playlistName}' создан")
            }
        }
    }

    // Необходимо проверить данные, были ли они заполнены
    private fun checkEnteredInfo() {
        if (newPlaylist.playlistName.isNotEmpty() ||
            newPlaylist.playlistDescription?.isNotEmpty() == true ||
            photoUri != null) {
            confirmDialog.show()
        } else {
            findNavController().popBackStack()
        }
    }

    private fun saveImageToPrivateStorage(uri: Uri, photoName: String) {
        val filePath = File(requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES), "PlaylistMaker")
        if (!filePath.exists()) {
            filePath.mkdirs()
        }
        newPlaylist = newPlaylist.copy(photoUrl = "$filePath/$photoName.jpg")
        val file = File(filePath, "$photoName.jpg")
        val inputStream = requireActivity().contentResolver.openInputStream(uri)
        val outputStream = FileOutputStream(file)
        BitmapFactory
            .decodeStream(inputStream)
            .compress(Bitmap.CompressFormat.JPEG, 30, outputStream)
    }

}