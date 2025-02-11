package com.example.playlistmaker.ui.mediaLibrary.activity

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
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
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentNewPlaylistBinding
import com.example.playlistmaker.domain.db.model.Playlist
import com.example.playlistmaker.ui.RootActivity
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

    // Необходимо создать экземпляр класса как раз, чтобы его передать
    private var newPlaylist: Playlist = Playlist()
    private var photoUri: Uri? = null

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
            .setNeutralButton("Отмена") { _, _ ->
                // ничего не делаем
            }.setPositiveButton("Завершить") { _, _ ->
                findNavController().navigateUp()//popBackStack()
                //requireActivity().supportFragmentManager.popBackStack()
                //(activity as RootActivity).hideOrShowBottomNavigationView(View.VISIBLE)
            }

        binding.toolbarNewPlaylist.setOnClickListener {
            checkEnteredInfo()
        }

        // Регистрируем событие, которое вызывает Photo picker
        val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {

                Glide.with(this)
                    .load(uri)
                    //.centerCrop()
                    //.transform(RoundedCorners(8)) // края не закругляются
                    .placeholder(R.drawable.add_photo)
                    .into(binding.addPhoto)

                photoUri = uri
                // сохранение фотографии в хранилище
                // ...

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

        // Кнопка Back
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                checkEnteredInfo()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        binding.createNewPlaylist.setOnClickListener {
            // создаём плейлист, сохраняем изменения и выходим
            // save()
            // конкретно тут добавляем картинку в хранилище, добавляем плейлист в БД
            photoUri?.let { it ->
                val randomNumber = Random.nextInt(10000)
                saveImageToPrivateStorage(it, "${newPlaylist.playlistName}_$randomNumber")
            }
            // необходимо добавить плейлист в БД
            viewModel.insertPlaylistToDb(newPlaylist = newPlaylist)

            // переношу в другое место
            //findNavController().navigateUp()
            //(activity as RootActivity).hideOrShowBottomNavigationView(View.VISIBLE) // ????????????


            //customToast(requireContext(), layoutInflater,"Плейлист ${newPlaylist.playlistName} создан")   // Добавление Toast
            //Log.i("newPlaylist", "$newPlaylist")


        }

        viewModel.idPlaylist.observe(viewLifecycleOwner) {
            if (it != -1L) {
                Log.i("myPlaylist", "$it")
                findNavController().navigateUp()//popBackStack()
                //requireActivity().supportFragmentManager.popBackStack()
                //(activity as RootActivity).hideOrShowBottomNavigationView(View.VISIBLE)
                customToast(requireContext(), layoutInflater,"Плейлист '${newPlaylist.playlistName}' создан")   // Добавление Toast
                // сохранение фотографии в хранилище
                //photoUri?.let { it1 -> saveImageToPrivateStorage(it1, "${newPlaylist.playlistName}_$it") }
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
            findNavController().navigateUp()//popBackStack()

            //requireActivity().supportFragmentManager.popBackStack()

            //(activity as RootActivity).hideOrShowBottomNavigationView(View.VISIBLE)
        }
    }

    private fun saveImageToPrivateStorage(uri: Uri, photoName: String) {
        val filePath = File(requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES), "PlaylistMaker")
        if (!filePath.exists()) {
            filePath.mkdirs() // создаем каталог, если он не создан
        }
        newPlaylist = newPlaylist.copy(photoUrl = "$filePath/$photoName.jpg")
        //создаём экземпляр класса File, который указывает на файл внутри каталога
        val file = File(filePath, "$photoName.jpg")
        // создаём входящий поток байтов из выбранной картинки
        val inputStream = requireActivity().contentResolver.openInputStream(uri)
        // создаём исходящий поток байтов в созданный выше файл
        val outputStream = FileOutputStream(file)
        BitmapFactory
            .decodeStream(inputStream)
            .compress(Bitmap.CompressFormat.JPEG, 30, outputStream)
    }


    /*private fun customToast(myText: String) {
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

    }*/
}