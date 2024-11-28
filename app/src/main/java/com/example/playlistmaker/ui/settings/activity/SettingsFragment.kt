package com.example.playlistmaker.ui.settings.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.App
import com.example.playlistmaker.databinding.FragmentSettingsBinding
import com.example.playlistmaker.ui.settings.state.ThemeState
import com.example.playlistmaker.ui.settings.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment: Fragment() {
    private lateinit var binding: FragmentSettingsBinding
    private val viewModel by viewModel<SettingsViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Для тёмной темы
        viewModel.observeThemeState().observe(viewLifecycleOwner) {
            render(it)
        }

        binding.switchTheme.setOnCheckedChangeListener { _, checked ->
            viewModel.updateSwitchTheme(theme = checked)
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
        (requireActivity().applicationContext as App).switchTheme(state)
    }

}