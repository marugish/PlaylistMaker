package com.example.playlistmaker.ui.settings

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.App
import com.example.playlistmaker.Creator
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding
    private val getSwitchThemeInteractor = Creator.provideSwitchThemeInteractor()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbarSettings.setNavigationOnClickListener {
            finish()
        }

        binding.share.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.setType("text/plain")
            shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_link))
            startActivity(Intent.createChooser(shareIntent, getString(R.string.share_app)))
        }

        binding.support.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SENDTO)
            shareIntent.data = Uri.parse("mailto:")
            shareIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.email)))
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.theme_of_message))
            shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.message))
            startActivity(shareIntent)
        }

        binding.userAgreement.setOnClickListener {
            val openLink = Intent(Intent.ACTION_VIEW)
            openLink.data = Uri.parse(getString(R.string.address))
            startActivity(openLink)
        }

        getSwitchThemeInteractor.getSwitchTheme { switchTheme ->
            binding.switchTheme.isChecked = switchTheme
        }

        binding.switchTheme.setOnCheckedChangeListener { _, checked ->
            getSwitchThemeInteractor.saveSwitchTheme(theme = checked)
            (applicationContext as App).switchTheme(checked)
        }
    }
}