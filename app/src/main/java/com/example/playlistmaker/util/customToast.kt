package com.example.playlistmaker.util

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.example.playlistmaker.R
import com.example.playlistmaker.ui.RootActivity

fun customToast(context: Context, layoutInflater: LayoutInflater, myText: String) {

    val layout: View = layoutInflater.inflate(
        R.layout.new_playlist_toast,
        null
    )

    val text = layout.findViewById<View>(R.id.text) as TextView
    text.text = myText

    val toast = Toast(context)
    toast.duration = Toast.LENGTH_LONG
    toast.setView(layout)
    toast.show()

}