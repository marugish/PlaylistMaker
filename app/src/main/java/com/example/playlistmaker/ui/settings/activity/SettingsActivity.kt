package com.example.playlistmaker.ui.settings.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import com.example.playlistmaker.App
import com.example.playlistmaker.R
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.databinding.ActivitySettingsBinding
import com.example.playlistmaker.ui.settings.ThemeState
import com.example.playlistmaker.ui.settings.view_model.SettingsViewModel

class SettingsActivity : ComponentActivity() {
    private lateinit var binding: ActivitySettingsBinding
    private lateinit var viewModel: SettingsViewModel

    private val getSwitchThemeInteractor = Creator.provideSwitchThemeInteractor()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Инициализация ViewModel
        //viewModel = ViewModelProvider(this, SettingsViewModel.getViewModelFactory())[SettingsViewModel::class.java]


        // ........
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

        // Тёмная тема
        /*viewModel.observeThemeState().observe(this) {
            render(it)
        }*/

        // тумблер ещё никак не изменён, пока никак его не касаюсь

        //viewModel.getSwitchTheme()
        getSwitchThemeInteractor.getSwitchTheme { switchTheme ->
            Log.d("settingsactivity", "$switchTheme")
            binding.switchTheme.isChecked = switchTheme
        }

        binding.switchTheme.setOnCheckedChangeListener { _, checked ->
            //viewModel.updateSwitchTheme(theme = checked)

            getSwitchThemeInteractor.saveSwitchTheme(theme = checked)
            (applicationContext as App).switchTheme(checked)
        }


    }

    private fun render(themeState: ThemeState) {
        when (themeState) {
            is ThemeState.Active -> {
                stateSwitch(true)
                Log.d("mysettings", "Settings.Active")
            }
            is ThemeState.Deactive ->  {
                stateSwitch(false)
                Log.d("mysettings", "Settings.Deactive")
            }
        }
    }

    private fun stateSwitch(state: Boolean) {
        Log.d("mysettings", "state = $state")
        // не нравится мне
        // я же в app ещё что-то делаю
        binding.switchTheme.isChecked = state
        (applicationContext as App).switchTheme(state)
    }
}