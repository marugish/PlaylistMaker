package com.example.playlistmaker.ui.settings.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.App
import com.example.playlistmaker.databinding.ActivitySettingsBinding
import com.example.playlistmaker.ui.settings.state.ThemeState
import com.example.playlistmaker.ui.settings.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding
    //private lateinit var viewModel: SettingsViewModel
    private val viewModel by viewModel<SettingsViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Инициализация ViewModel
        //viewModel = ViewModelProvider(this, SettingsViewModel.getViewModelFactory(this))[SettingsViewModel::class.java]

        // Для тёмной темы
        viewModel.observeThemeState().observe(this) {
            render(it)
        }

        binding.switchTheme.setOnCheckedChangeListener { _, checked ->
            viewModel.updateSwitchTheme(theme = checked)
        }

        binding.toolbarSettings.setNavigationOnClickListener {
            finish()
        }

        binding.share.setOnClickListener {
            viewModel.shareLink()
        }

        binding.support.setOnClickListener {
            viewModel.writeToSupport()
        }

        binding.userAgreement.setOnClickListener {
            viewModel.userAgreement()
        }

    }

    private fun render(themeState: ThemeState) {
        when (themeState) {
            is ThemeState.Active -> stateSwitch(true)
            is ThemeState.Deactive -> stateSwitch(false)
        }
    }

    private fun stateSwitch(state: Boolean) {
        binding.switchTheme.isChecked = state
        (applicationContext as App).switchTheme(state)
    }
}