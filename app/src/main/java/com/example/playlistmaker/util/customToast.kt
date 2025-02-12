package com.example.playlistmaker.util

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.example.playlistmaker.R

fun customToast(context: Context, layoutInflater: LayoutInflater, myText: String) {

    val inflater = layoutInflater
    val layout: View = inflater.inflate(
        R.layout.new_playlist_toast,
        null//(activity as RootActivity).findViewById<ViewGroup>(R.id.toast_layout_root)
    )

    val text = layout.findViewById<View>(R.id.text) as TextView
    text.text = myText

    val toast = Toast(context)
    //toast.setGravity(Gravity.BOTTOM, 0, 0)
    toast.duration = Toast.LENGTH_LONG
    toast.setView(layout)
    toast.show()

}