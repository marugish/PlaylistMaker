package com.example.playlistmaker.ui.search.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentSearchBinding
import com.example.playlistmaker.domain.search.model.Track
import com.example.playlistmaker.ui.search.state.HistoryState
import com.example.playlistmaker.ui.search.state.TracksState
import com.example.playlistmaker.ui.search.view_model.SearchViewModel
import com.example.playlistmaker.util.SearchError
import org.koin.androidx.viewmodel.ext.android.viewModel
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.ui.RootActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchFragment: Fragment()  {
    companion object {
        private const val EDIT_TEXT = "EDIT_TEXT"
        private const val SEARCH_QUERY = ""
    }

    private lateinit var binding: FragmentSearchBinding

    private val viewModel by viewModel<SearchViewModel>()

    // Обычный поиск
    private val adapter = TrackAdapter { track -> showTrackPlayer(track) }

    // История поиска
    private val searchAdapter = TrackAdapter { track -> showTrackPlayer(track) }

    private fun showTrackPlayer(track: Track) {
        if (viewModel.clickDebounce()) {
            val bundle = Bundle()
            bundle.putSerializable("track", track)
            findNavController().navigate(R.id.playFragment, bundle)
            (activity as RootActivity).hideOrShowBottomNavigationView(View.GONE)

            viewModel.saveSearchHistory(track)
        }
    }

    private var searchQuery: String = SEARCH_QUERY

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(EDIT_TEXT, searchQuery)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as RootActivity).hideOrShowBottomNavigationView(View.VISIBLE)

        savedInstanceState?.let {
            searchQuery = it.getString(EDIT_TEXT).toString()
            binding.searchEditText.setText(searchQuery)
        }

        // Обычный поиск
        binding.trackRecycleView.layoutManager = LinearLayoutManager(requireContext())
        binding.trackRecycleView.adapter = adapter

        // История поиска
        binding.searchRecycleView.layoutManager = LinearLayoutManager(requireContext())
        binding.searchRecycleView.adapter = searchAdapter

        // Подписываемся на изменения Обычного поиска
        viewModel.observeState().observe(viewLifecycleOwner) {
            render(it)
        }

        // Подписываемся на изменения Истории поиска
        viewModel.observeHistoryState().observe(viewLifecycleOwner) {
            render(it)
        }

        binding.updateButton.setOnClickListener {
            viewModel.searchRequest(searchQuery)
        }

        binding.clearHistoryButton.setOnClickListener {
            viewModel.clearHistorySearch()
        }

        binding.searchClearButton.setOnClickListener {
            binding.searchEditText.setText("")
            val inputMethodManager = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(it.windowToken , 0)
            binding.searchEditText.clearFocus()
            adapter.setItems(emptyList())
            placeholderVisibility(View.GONE)
        }

        binding.searchEditText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus && binding.searchEditText.text.isEmpty()) {
                viewModel.getHistorySearch()
            } else {
                historyVisibility(View.GONE)
                binding.trackRecycleView.visibility = View.VISIBLE
            }
        }

        binding.searchEditText.addTextChangedListener(
            onTextChanged = { s, _, _, _ ->
                binding.searchClearButton.visibility = clearButtonVisibility(s)
                searchQuery = s.toString()
                viewModel.searchDebounce(changedText = s?.toString() ?: "")
                if (binding.searchEditText.hasFocus()  ) {
                    if (s?.isEmpty() == true) {
                        val historyState = viewModel.observeHistoryState().value
                        if (historyState is HistoryState.Content) {
                            showHistory(historyState.tracks)
                        }
                    } else {
                        historyVisibility(View.GONE)
                    }
                }
            }
        )
    }

    private fun showHistory(historyTracks: List<Track>) {
        binding.trackRecycleView.visibility = View.GONE
        placeholderVisibility(View.GONE)
        binding.updateButton.visibility = View.GONE
        if (historyTracks.isNotEmpty()) {
            historyVisibility(View.VISIBLE)
        }
    }

    private fun historyVisibility(visibilityStatus: Int) {
        binding.historySearch.visibility = visibilityStatus
        binding.clearHistoryButton.visibility = visibilityStatus
        binding.searchRecycleView.visibility = visibilityStatus
    }

    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    /*private fun clickDebounce() : Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            viewLifecycleOwner.lifecycleScope.launch {
                delay(CLICK_DEBOUNCE_DELAY)
                isClickAllowed = true
            }
        }
        return current
    }*/

    private fun render(state: TracksState) {
        when (state) {
            is TracksState.Loading -> showLoading()
            is TracksState.Content -> showContent(state.tracks)
            is TracksState.Error -> showError(state.errorMessage)
            is TracksState.Empty -> showEmpty(state.message)
        }
    }

    private fun render(historyState: HistoryState) {
        when (historyState) {
            is HistoryState.Clear -> clearSearchHistory()
            is HistoryState.Content -> showHistoryContent(historyState.tracks)
        }
    }

    private fun clearSearchHistory() {
        historyVisibility(View.GONE)
        binding.trackRecycleView.visibility = View.VISIBLE
        searchAdapter.setItems(emptyList())
    }

    private fun showHistoryContent(historyTracks: List<Track>) {
        searchAdapter.setItems(historyTracks)
        if (historyTracks.isNotEmpty()) {
            historyVisibility(View.VISIBLE)
        }
        binding.trackRecycleView.visibility = View.GONE
    }

    private fun placeholderVisibility(visibilityStatus: Int) {
        binding.placeholderImage.visibility = visibilityStatus
        binding.placeholderMessage.visibility = visibilityStatus
    }

    private fun showLoading() {
        binding.trackRecycleView.visibility = View.GONE
        placeholderVisibility(View.GONE)
        binding.updateButton.visibility = View.GONE
        historyVisibility(View.GONE)
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun showContent(tracks: List<Track>) {
        binding.trackRecycleView.visibility = View.VISIBLE

        placeholderVisibility(View.GONE)
        binding.updateButton.visibility = View.GONE
        binding.progressBar.visibility = View.GONE

        historyVisibility(View.GONE)

        adapter.setItems(tracks)
    }

    private fun showError(errorMessage: SearchError) {
        adapter.setItems(emptyList())

        binding.trackRecycleView.visibility = View.GONE
        binding.progressBar.visibility = View.GONE
        placeholderVisibility(View.VISIBLE)
        when (errorMessage) {
            SearchError.NETWORK_ERROR -> {
                binding.updateButton.visibility = View.VISIBLE
                binding.placeholderMessage.text = getString(R.string.network_problems)
                binding.placeholderImage.setImageResource(R.drawable.no_network_placeholder)
            }
            SearchError.NO_RESULTS -> {
                binding.updateButton.visibility = View.GONE
                binding.placeholderMessage.text = getString(R.string.nothing_found)
                binding.placeholderImage.setImageResource(R.drawable.not_found_placeholder)
            }
        }
    }

    private fun showEmpty(emptyMessage: SearchError) {
        showError(emptyMessage)
    }

}