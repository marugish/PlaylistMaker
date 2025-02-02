package com.example.playlistmaker.ui.mediaLibrary.activity

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentNewPlaylistBinding
import com.example.playlistmaker.domain.db.model.Playlist
import com.example.playlistmaker.ui.RootActivity
import com.example.playlistmaker.ui.mediaLibrary.view_model.NewPlaylistViewModel
import com.example.playlistmaker.ui.search.view_model.SearchViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class NewPlaylistFragment : Fragment() {
    private lateinit var binding: FragmentNewPlaylistBinding

    private val viewModel by viewModel<NewPlaylistViewModel>()

    private lateinit var confirmDialog: MaterialAlertDialogBuilder

    // Необходимо создать экземпляр класса как раз, чтобы его передать
    private var newPlaylist: Playlist = Playlist()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentNewPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        confirmDialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle("Завершить создание плейлиста?")
            .setMessage("Все несохраненные данные будут потеряны")
            .setNeutralButton("Отмена") { dialog, which ->
                // ничего не делаем
            }.setPositiveButton("Завершить") { dialog, which ->
                findNavController().navigateUp()
                //customToast("название плейлиста")
                (activity as RootActivity).hideOrShowBottomNavigationView(View.VISIBLE) // ????????????
            }

        binding.toolbarNewPlaylist.setOnClickListener {
            // необходимо проверить данные, были ли они заполнены
            // ...

            confirmDialog.show()


        }

        // Регистрируем событие, которое вызывает Photo picker
        val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {


                //binding.addPhoto.setImageURI(uri)


                /*Glide.with(this)
                    .load(uri)
                    //.override(312, 312)
                    //.transform(CenterCrop())
                    //.circleCrop()
                    //.fitCenter()
                    .centerCrop()
                    //.centerInside()
                    .transform(RoundedCorners(8))
                    .placeholder(R.drawable.add_photo)
                    .into(binding.addPhoto)*/

                /*Glide.with(parentView.context)
                    .load(model.artworkUrl100)
                    .centerCrop()
                    .transform(RoundedCorners(4)) // 2 совсем незаметно
                    .placeholder(R.drawable.placeholder)
                    .into(trackImage)*/

                //saveImageToPrivateStorage(uri)
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





        // добавление слушателя для обработки нажатия на кнопку Back
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // необходимо проверить данные, были ли они заполнены
                // ...
                confirmDialog.show()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        binding.createNewPlaylist.setOnClickListener {
            // создаём плейлист, сохраняем изменения и выходим
            // save()
            // конкретно тут добавляем картинку в хранилище, добавляем плейлист в БД

            // необходимо добавить плейлист в БД
            viewModel.insertPlaylistToDb(newPlaylist = newPlaylist)

            findNavController().navigateUp()

            customToast("Плейлист ${newPlaylist.playlistName} создан")   // Добавление Toast
            Log.i("newPlaylist", "$newPlaylist")


        }

    }


    private fun customToast(myText: String) {
        //val toast = Toast.makeText(context, "Correto!", Toast.LENGTH_SHORT)

        //val toastMessage = toast.view!!.findViewById<View>(android.R.id.message) as TextView
        //toastMessage.setTextColor(Color.RED)
        //toast.show()

        val inflater = layoutInflater
        val layout: View = inflater.inflate(
            R.layout.new_playlist_toast,
            null//(activity as RootActivity).findViewById<ViewGroup>(R.id.toast_layout_root)
        )

        val text = layout.findViewById<View>(R.id.text) as TextView
        text.text = myText

        val toast = Toast(requireContext())
        //toast.setGravity(Gravity.BOTTOM, 0, 0)
        toast.duration = Toast.LENGTH_LONG
        toast.setView(layout)
        toast.show()

    }
}