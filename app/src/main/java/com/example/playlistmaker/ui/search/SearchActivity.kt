package com.example.playlistmaker.ui.search

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.Creator
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.api.SearchHistoryInteractor
import com.example.playlistmaker.domain.api.TracksInteractor
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.sharedPref


class SearchActivity : AppCompatActivity() {

    private val getTracksInteractor = Creator.provideTracksInteractor()
    private val getSearchHistoryInteractor = Creator.provideSearchHistoryInteractor()

    private val results: MutableList<Track> = mutableListOf()
    private val searchHistory = SearchHistory(sharedPref)

    private val adapter = TrackAdapter(results, { track -> showTrackInfo(track) }, searchHistory)
    private val searchAdapter = TrackAdapter(searchHistory.historyResults, { track -> showTrackInfo(track) }, searchHistory)
    //private val adapter = TrackAdapter({ track -> showTrackInfo(track) }, searchHistory)
    //private val searchAdapter = TrackAdapter({ track -> showTrackInfo(track) }, searchHistory)

    //подумать над названием
    private fun showTrackInfo(track: Track) {
        if (clickDebounce()) {
            val intent = Intent(this, PlayActivity::class.java)
            intent.putExtra("track", track)
            startActivity(intent)
        }
    }

    private var searchQuery: String = SEARCH_QUERY

    private var isClickAllowed = true

    private val handler = Handler(Looper.getMainLooper())
    private val searchRunnable = Runnable { search(inputEditText.text.toString()) }

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

        val inputEditText = findViewById<EditText>(R.id.search_edit_text)
        searchQuery = savedInstanceState.getString(EDIT_TEXT, SEARCH_QUERY)
        inputEditText.setText(searchQuery)
    }

    private lateinit var inputEditText: EditText
    private lateinit var clearButton: ImageView
    private lateinit var recycler: RecyclerView
    private lateinit var placeholderMessage: TextView
    private lateinit var placeholderImage: ImageView
    private lateinit var updateButton: Button
    private lateinit var progressBar: ProgressBar

    //всё для истории поиска
    private lateinit var historyText: TextView
    private lateinit var historyRecycler: RecyclerView
    private lateinit var historyButton: Button


    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        inputEditText = findViewById(R.id.search_edit_text)
        clearButton = findViewById(R.id.search_clear_button)
        placeholderMessage = findViewById(R.id.placeholderMessage)
        placeholderImage = findViewById(R.id.placeholder_image)
        updateButton = findViewById(R.id.update_button)
        progressBar = findViewById(R.id.progress_bar)

        recycler = findViewById(R.id.track_recycle_view)
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = adapter

        // всё для истории поиска
        historyText = findViewById(R.id.history_search)
        historyButton = findViewById(R.id.clear_search_button)
        historyRecycler = findViewById(R.id.search_recycle_view)

        historyRecycler.layoutManager = LinearLayoutManager(this)
        historyRecycler.adapter = searchAdapter


        val toolbarBack = findViewById<Toolbar>(R.id.toolbar_search)
        toolbarBack.setNavigationOnClickListener {
            finish()
        }

        clearButton.setOnClickListener {
            inputEditText.setText("")
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(it.windowToken , 0)
            inputEditText.clearFocus()
            inputEditText.hint = getString(R.string.search)
            //results.clear()
            //adapter.notifyDataSetChanged()
            // Новое
            adapter.setItems(emptyList())
            placeholderVisibility(View.GONE)
        }

        inputEditText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus && inputEditText.text.isEmpty()) {

                getSearchHistoryInteractor.getSearchHistory(
                    consumer = object : SearchHistoryInteractor.SearchHistoryConsumer {
                        override fun consume(results: List<Track>) {
                            searchAdapter.setItems(results)
                            if (results.isNotEmpty()) {
                                historyVisibility(View.VISIBLE)
                            }
                        }
                    })

                // лишняя работа похоже тут есть
                // Убрала
                //searchAdapter.setItems(searchHistory.read(sharedPref).toList())

                //searchHistory.historyResults.clear()
                //searchHistory.historyResults.addAll(history)
                //searchAdapter.notifyDataSetChanged()

                // Убрала временно
                //if (searchHistory.historyResults.isNotEmpty())
                    //historyVisibility(View.VISIBLE)
            } else {
                historyVisibility(View.GONE)
                recycler.visibility = View.VISIBLE
            }
        }

        historyButton.setOnClickListener {
            searchHistory.clearHistory()
            searchAdapter.notifyDataSetChanged()

            historyVisibility(View.GONE)
            recycler.visibility = View.VISIBLE
        }

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // empty
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearButton.visibility = clearButtonVisibility(s)
                searchQuery = s.toString()
                if (inputEditText.hasFocus() && s?.isEmpty() == true) {
                    if (searchHistory.historyResults.isNotEmpty()) {
                        recycler.visibility = View.GONE
                        placeholderVisibility(View.GONE)
                        updateButton.visibility = View.GONE
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
        inputEditText.addTextChangedListener(simpleTextWatcher)


        updateButton.setOnClickListener {
            search(searchQuery)
        }

    }

    private fun historyVisibility(visibilityStatus: Int) {
        historyText.visibility = visibilityStatus
        historyButton.visibility = visibilityStatus
        historyRecycler.visibility = visibilityStatus
    }

    private fun placeholderVisibility(visibilityStatus: Int) {
        placeholderImage.visibility = visibilityStatus
        placeholderMessage.visibility = visibilityStatus
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

    // Подумать над названием
    private fun showProgress(isShow: Boolean) {
        if (isShow) {
            progressBar.visibility = View.VISIBLE
            recycler.visibility = View.GONE
        } else {
            progressBar.visibility = View.GONE
            recycler.visibility = View.VISIBLE
        }
    }

    private fun search(request: String) {
        if (request.isNotEmpty()) {

            showProgress(true)

            getTracksInteractor.searchTracks(
                expression = request,
                consumer = object : TracksInteractor.TracksConsumer {
                    override fun consume(foundTracks: List<Track>) {
                        handler.post {
                            if (foundTracks.isNotEmpty()) {
                                adapter.setItems(foundTracks)
                                //results.clear()
                                //results.addAll(foundTracks)
                                //adapter.notifyDataSetChanged()
                                showMessage("", 0, false)

                                showProgress(false)
                            }
                        }
                        /*val currentRunnable = detailsRunnable
                        if (currentRunnable != null) {
                            handler.removeCallbacks(currentRunnable)
                        }

                        val newDetailsRunnable = Runnable {
                            when (data) {
                                is ConsumerData.Error -> showError(data.message)
                                is ConsumerData.Data -> {
                                    val productDetails = data.value
                                    val productDetailsInfo = ProductDetailsMapper.map(productDetails)
                                    showProductDetails(productDetailsInfo)
                                }
                            }
                        }
                        detailsRunnable = newDetailsRunnable
                        handler.post(newDetailsRunnable)*/
                    }
                }
            )

            /*RetrofitItunesClient.itunesService.searchTrack(request).enqueue(object : Callback<TracksSearchResponse> {
                    @SuppressLint("NotifyDataSetChanged")
                    override fun onResponse(
                        call: Call<TracksSearchResponse>,
                        response: Response<TracksSearchResponse>
                    ) {
                        progressBar.visibility = View.GONE
                        when (response.code()) {
                            200 -> {
                                if (response.body()?.results?.isNotEmpty() == true) {
                                    results.clear()
                                    results.addAll(response.body()?.results!!)
                                    adapter.notifyDataSetChanged()
                                    showMessage("", 0, false)
                                    recycler.visibility = View.VISIBLE
                                    //Toast.makeText(applicationContext, "200: ОК", Toast.LENGTH_LONG).show()
                                } else {
                                    showMessage(
                                        getString(R.string.nothing_found),
                                        R.drawable.not_found_placeholder,
                                        false
                                    )
                                    //Toast.makeText(applicationContext, "200: BAD", Toast.LENGTH_LONG).show()
                                }
                            }
                            else -> showMessage(
                                getString(R.string.network_problems),
                                R.drawable.no_network_placeholder,
                                true
                            )
                            //Toast.makeText(applicationContext, "On Response: что-то пошло не так", Toast.LENGTH_LONG).show()
                        }
                    }

                    override fun onFailure(call: Call<TracksSearchResponse>, t: Throwable) {
                        progressBar.visibility = View.GONE
                        showMessage(
                            getString(R.string.network_problems),
                            R.drawable.no_network_placeholder,
                            true
                        )
                        //Toast.makeText(applicationContext, "On Failure: что-то пошло не так", Toast.LENGTH_LONG).show()
                    }
                }) */
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun showMessage(text: String, resource: Int, buttonVisibility: Boolean) {
        if (text.isNotEmpty()) {
            placeholderVisibility(View.VISIBLE)
            adapter.setItems(emptyList())
            //results.clear()
            //adapter.notifyDataSetChanged()
            placeholderMessage.text = text
            placeholderImage.setImageResource(resource)
            if (buttonVisibility) {
                updateButton.visibility = View.VISIBLE
            } else {
                updateButton.visibility = View.GONE
            }
        } else {
            placeholderVisibility(View.GONE)
            updateButton.visibility = View.GONE
        }
    }

}