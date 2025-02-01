package com.example.playlistmaker.ui.mediaLibrary.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.databinding.FragmentNewPlaylistBinding
import com.example.playlistmaker.ui.RootActivity
import com.example.playlistmaker.ui.search.state.HistoryState

class NewPlaylistFragment : Fragment() {
    private lateinit var binding: FragmentNewPlaylistBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentNewPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //binding.toolbarNewPlaylist.setOnClickListener {
          //  findNavController().navigateUp()

            // вероятнее всего не тут необходимо делать, не работает системная кнопка назад
            //(activity as RootActivity).hideOrShowBottomNavigationView(View.VISIBLE)
        //}

            /*binding.newPlaylistName.addTextChangedListener(
            /*afterTextChanged = { s, _, _, _ ->
                s.toString()
            }*/
        )*/

    }
}