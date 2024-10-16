package com.example.playlistmaker.ui.search

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.Creator
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivitySearchBinding
import com.example.playlistmaker.domain.api.SearchHistoryInteractor
import com.example.playlistmaker.domain.api.TracksInteractor
import com.example.playlistmaker.domain.models.Resource
import com.example.playlistmaker.domain.models.Track


class SearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchBinding

    // Обычный поиск
    private val getTracksInteractor = Creator.provideTracksInteractor()
    private val results: MutableList<Track> = mutableListOf()
    private val adapter = TrackAdapter(results) { track -> showTrackPlayer(track) }

    // История поиска
    private val getSearchHistoryInteractor = Creator.provideSearchHistoryInteractor()
    private var historyResults: MutableList<Track> = mutableListOf()
    private val searchAdapter = TrackAdapter(historyResults) { track -> showTrackPlayer(track) }

    private fun showTrackPlayer(track: Track) {
        if (clickDebounce()) {
            val intent = Intent(this, PlayActivity::class.java)
            intent.putExtra("track", track)
            startActivity(intent)
            getSearchHistoryInteractor.saveSearchHistory(track)
        }
    }

    private var searchQuery: String = SEARCH_QUERY

    private var isClickAllowed = true

    private val handler = Handler(Looper.getMainLooper())
    private val searchRunnable = Runnable { search(binding.searchEditText.text.toString()) }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(EDIT_TEXT, searchQuery)
    }

    companion object {
        private const val EDIT_TEXT = "EDIT_TEXT"
        private const val SEARCH_QUERY = ""
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
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
        binding = ActivitySearchBinding.inflate(layoutInflater) // Инициализация ViewBinding
        setContentView(binding.root)

        // Обычный поиск
        binding.trackRecycleView.layoutManager = LinearLayoutManager(this)
        binding.trackRecycleView.adapter = adapter

        // История поиска
        binding.searchRecycleView.layoutManager = LinearLayoutManager(this)
        binding.searchRecycleView.adapter = searchAdapter

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

                getSearchHistoryInteractor.getSearchHistory(
                    consumer = object : SearchHistoryInteractor.SearchHistoryConsumer {
                        override fun consume(results: List<Track>) {
                            searchAdapter.setItems(results)
                            historyResults = results.toMutableList()
                            if (results.isNotEmpty()) {
                                historyVisibility(View.VISIBLE)
                            }
                        }
                    })
            } else {
                historyVisibility(View.GONE)
                binding.trackRecycleView.visibility = View.VISIBLE
            }
        }

        binding.clearHistoryButton.setOnClickListener {
            getSearchHistoryInteractor.clearHistory()
            historyResults.clear()
            searchAdapter.notifyDataSetChanged()
            historyVisibility(View.GONE)
            binding.trackRecycleView.visibility = View.VISIBLE
        }

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // empty
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
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
                    searchDebounce()
                }
            }

            override fun afterTextChanged(s: Editable?) {
                // empty
            }
        }
        binding.searchEditText.addTextChangedListener(simpleTextWatcher)

        binding.updateButton.setOnClickListener {
            search(searchQuery)
        }

    }

    private fun historyVisibility(visibilityStatus: Int) {
        binding.historySearch.visibility = visibilityStatus
        binding.clearHistoryButton.visibility = visibilityStatus
        binding.searchRecycleView.visibility = visibilityStatus
    }

    private fun placeholderVisibility(visibilityStatus: Int) {
        binding.placeholderImage.visibility = visibilityStatus
        binding.placeholderMessage.visibility = visibilityStatus
    }

    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    private fun searchDebounce() {
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }

    private fun clickDebounce() : Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    private fun showProgressBar(isShow: Boolean) {
        if (isShow) {
            binding.progressBar.visibility = View.VISIBLE
            binding.trackRecycleView.visibility = View.GONE
        } else {
            binding.progressBar.visibility = View.GONE
            binding.trackRecycleView.visibility = View.VISIBLE
        }
    }

    private fun search(request: String) {
        if (request.isNotEmpty()) {
            showProgressBar(true)
            getTracksInteractor.searchTracks(
                expression = request,
                consumer = object : TracksInteractor.TracksConsumer {
                    override fun consume(foundTracks: Resource<List<Track>>) {
                        when (foundTracks) {
                            is Resource.Success -> {
                                handler.post {
                                    adapter.setItems(foundTracks.data)
                                    showMessage("", 0, false)
                                    showProgressBar(false)
                                }
                            }
                            is Resource.Error -> {
                                if (foundTracks.message == "Ничего не найдено") {
                                    handler.post {
                                        showMessage(
                                            getString(R.string.nothing_found),
                                            R.drawable.not_found_placeholder,
                                            false
                                        )
                                        showProgressBar(false)
                                    }
                                } else {
                                    Log.d("mysearch", "нет сети")
                                    handler.post {
                                        showMessage(
                                            getString(R.string.network_problems),
                                            R.drawable.no_network_placeholder,
                                            true
                                        )
                                        showProgressBar(false)
                                    }
                                }
                            }
                        }
                    }
                }
            )
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun showMessage(text: String, resource: Int, buttonVisibility: Boolean) {
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
    }

}