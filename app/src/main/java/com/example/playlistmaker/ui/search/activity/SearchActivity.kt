package com.example.playlistmaker.ui.search.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.ComponentActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.R
import com.example.playlistmaker.ui.search.TracksState
import com.example.playlistmaker.databinding.ActivitySearchBinding
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.ui.player.activity.PlayActivity
import com.example.playlistmaker.ui.search.TrackAdapter
import com.example.playlistmaker.ui.search.view_model.SearchViewModel
import com.example.playlistmaker.util.SearchError


class SearchActivity : ComponentActivity() {    //AppCompatActivity() {
    private lateinit var binding: ActivitySearchBinding
    private lateinit var viewModel: SearchViewModel

    // Обычный поиск
    //private val getTracksInteractor = Creator.provideTracksInteractor()

    private val results: MutableList<Track> = mutableListOf()
    private val adapter = TrackAdapter(results) { track -> showTrackPlayer(track) }

    // История поиска
    //private val getSearchHistoryInteractor = Creator.provideSearchHistoryInteractor()

    private var historyResults: MutableList<Track> = mutableListOf()
    private val searchAdapter = TrackAdapter(historyResults) { track -> showTrackPlayer(track) }

    private fun showTrackPlayer(track: Track) {
        if (clickDebounce()) {
            val intent = Intent(this, PlayActivity::class.java)
            intent.putExtra("track", track)
            startActivity(intent)
            viewModel.saveSearchHistory(track)//getSearchHistoryInteractor.saveSearchHistory(track)
        }
    }

    private var searchQuery: String = SEARCH_QUERY

    private var isClickAllowed = true

    private val handler = Handler(Looper.getMainLooper())
    //private val searchRunnable = Runnable { search(binding.searchEditText.text.toString()) }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(EDIT_TEXT, searchQuery)
    }

    companion object {
        private const val EDIT_TEXT = "EDIT_TEXT"
        private const val SEARCH_QUERY = ""
        //private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        searchQuery = savedInstanceState.getString(EDIT_TEXT, SEARCH_QUERY)
        binding.searchEditText.setText(searchQuery)
    }


    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Инициализация ViewModel
        viewModel = ViewModelProvider(this)[SearchViewModel::class.java]

        // Обычный поиск
        binding.trackRecycleView.layoutManager = LinearLayoutManager(this)
        binding.trackRecycleView.adapter = adapter

        // История поиска
        binding.searchRecycleView.layoutManager = LinearLayoutManager(this)
        binding.searchRecycleView.adapter = searchAdapter

        // Подписываемся на изменения
        viewModel.observeState().observe(this) {
            render(it)
        }




        binding.toolbarSearch.setNavigationOnClickListener {
            finish()
        }

        binding.searchClearButton.setOnClickListener {
            binding.searchEditText.setText("")
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(it.windowToken , 0)
            binding.searchEditText.clearFocus()
            adapter.setItems(emptyList())
            placeholderVisibility(View.GONE)
        }

        binding.searchEditText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus && binding.searchEditText.text.isEmpty()) {

                getSearchHistoryInteractor.getSearchHistory { results ->
                    searchAdapter.setItems(results)
                    historyResults = results.toMutableList()
                    if (results.isNotEmpty()) {
                        historyVisibility(View.VISIBLE)
                    }
                }
            } else {
                historyVisibility(View.GONE)
                binding.trackRecycleView.visibility = View.VISIBLE
            }
        }

        binding.clearHistoryButton.setOnClickListener {
            // скорее всего будет новый observer
            getSearchHistoryInteractor.clearHistory()
            historyResults.clear()
            searchAdapter.notifyDataSetChanged()
            historyVisibility(View.GONE)
            binding.trackRecycleView.visibility = View.VISIBLE
        }

        binding.searchEditText.addTextChangedListener(
            onTextChanged = { s, _, _, _ ->
                binding.searchClearButton.visibility = clearButtonVisibility(s)
                searchQuery = s.toString()
                if (binding.searchEditText.hasFocus() && s?.isEmpty() == true) {
                    if (historyResults.isNotEmpty()) {
                        binding.trackRecycleView.visibility = View.GONE
                        placeholderVisibility(View.GONE)
                        binding.updateButton.visibility = View.GONE
                        historyVisibility(View.VISIBLE)
                    }
                } else {
                    historyVisibility(View.GONE)
                    viewModel.searchDebounce(changedText = s?.toString() ?: "")
                }
            }
        )

        binding.updateButton.setOnClickListener {
            viewModel.searchRequest(searchQuery)
            //search(searchQuery)
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

    /*private fun searchDebounce() {
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }*/

    private fun clickDebounce() : Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    /*private fun search(request: String) {
        if (request.isNotEmpty()) {
            showProgressBar(true)
            getTracksInteractor.searchTracks(expression = request
            ) { foundTracks, errorMessage ->
                handler.post {
                    showProgressBar(false)
                    if (foundTracks != null) {
                        adapter.setItems(foundTracks)
                        showMessage("", 0, false)
                    }
                    if (errorMessage != null) {
                        when (errorMessage) {
                            SearchError.NO_RESULTS -> {
                                showMessage(
                                    getString(R.string.nothing_found),
                                    R.drawable.not_found_placeholder,
                                    false
                                )
                            }
                            SearchError.NETWORK_ERROR -> {
                                showMessage(
                                    getString(R.string.network_problems),
                                    R.drawable.no_network_placeholder,
                                    true
                                )
                            }
                        }
                    }
                }
            }
        }
    }*/

    private fun render(state: TracksState) {
        when (state) {
            is TracksState.Loading -> showLoading()
            is TracksState.Content -> showContent(state.tracks)
            is TracksState.Error -> showError(state.errorMessage)
            is TracksState.Empty -> showEmpty(state.message)
        }
    }

    private fun placeholderVisibility(visibilityStatus: Int) {
        binding.placeholderImage.visibility = visibilityStatus
        binding.placeholderMessage.visibility = visibilityStatus
    }

    private fun showLoading() {
        binding.trackRecycleView.visibility = View.GONE
        // то, что идёт вместе с placeholder
        placeholderVisibility(View.GONE)
        //binding.placeholderMessage.visibility = View.GONE
        //binding.placeholderImage.visibility = View.GONE
        binding.updateButton.visibility = View.GONE
        // скорее всего нужно будет ещё скрыть Историю
        // ...
        binding.progressBar.visibility = View.VISIBLE
    }
    /*private fun showProgressBar(isShow: Boolean) {
        if (isShow) {
            binding.progressBar.visibility = View.VISIBLE
            binding.trackRecycleView.visibility = View.GONE
        } else {
            binding.progressBar.visibility = View.GONE
            binding.trackRecycleView.visibility = View.VISIBLE
        }
    }*/

    private fun showContent(tracks: List<Track>) {
        binding.trackRecycleView.visibility = View.VISIBLE

        placeholderVisibility(View.GONE)
        binding.updateButton.visibility = View.GONE
        binding.progressBar.visibility = View.GONE

        adapter.setItems(tracks)
        //adapter.movies.clear()
        //adapter.movies.addAll(movies)
        //adapter.notifyDataSetChanged()
    }
    private fun showError(errorMessage: SearchError) {

        adapter.setItems(emptyList()) // ?????????

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

    /*private fun showMessage(text: String, resource: Int, buttonVisibility: Boolean) {
        if (text.isNotEmpty()) {
            placeholderVisibility(View.VISIBLE)
            adapter.setItems(emptyList())
            binding.placeholderMessage.text = text
            binding.placeholderImage.setImageResource(resource)
            if (buttonVisibility) {
                binding.updateButton.visibility = View.VISIBLE
            } else {
                binding.updateButton.visibility = View.GONE
            }
        } else {
            placeholderVisibility(View.GONE)
            binding.updateButton.visibility = View.GONE
        }
    }*/

}