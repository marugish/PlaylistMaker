package com.example.playlistmaker

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.CompoundButton
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SwitchCompat
import androidx.appcompat.widget.Toolbar

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val toolbar = findViewById<Toolbar>(R.id.toolbar_settings)
        toolbar.setNavigationOnClickListener {
            finish()
        }

        val share = findViewById<ImageView>(R.id.share)
        share.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.setType("text/plain")
            shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_link))
            startActivity(Intent.createChooser(shareIntent, getString(R.string.share_app)))
        }

        val support = findViewById<ImageView>(R.id.support)
        support.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SENDTO)
            shareIntent.data = Uri.parse("mailto:")
            shareIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.email)))
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.theme_of_message))
            shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.message))
            startActivity(shareIntent)
        }

        val userAgreement = findViewById<ImageView>(R.id.user_agreement)
        userAgreement.setOnClickListener {
            val openLink = Intent(Intent.ACTION_VIEW)
            openLink.data = Uri.parse(getString(R.string.address))
            startActivity(openLink)
        }

        // ПЕРЕПРОВЕРИТЬ
        val themeSwitcher = findViewById<SwitchCompat>(R.id.switch_theme)
        val savedTheme = sharedPref.getBoolean(THEME_SWITCH_KEY, false)
        themeSwitcher.isChecked = savedTheme
        themeSwitcher.setOnCheckedChangeListener { switcher, checked ->
            sharedPref.edit().putBoolean(THEME_SWITCH_KEY, checked).apply()
            (applicationContext as App).switchTheme(checked)
        }

    }

}