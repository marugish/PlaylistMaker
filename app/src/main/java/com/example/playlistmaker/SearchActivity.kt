package com.example.playlistmaker

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class SearchActivity : AppCompatActivity() {

    // Retrofit
    private val searchBaseUrl = "https://itunes.apple.com"
    private val retrofit = Retrofit.Builder()
        .baseUrl(searchBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val itunesService = retrofit.create(ItunesApi::class.java)

    private val results: MutableList<Track> = mutableListOf()
    private val searchHistory = SearchHistory(sharedPref)
    private val adapter = TrackAdapter(results, searchHistory)
    private val searchAdapter = TrackAdapter(searchHistory.historyResults, searchHistory)
    private var searchQuery: String = SEARCH_QUERY


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(EDIT_TEXT, searchQuery)
    }

    private companion object {
        const val EDIT_TEXT = "EDIT_TEXT"
        const val SEARCH_QUERY = ""
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
            results.clear()
            adapter.notifyDataSetChanged()
        }

        inputEditText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus && inputEditText.text.isEmpty()) {
                searchHistory.historyResults.clear()
                searchHistory.historyResults.addAll(searchHistory.read(sharedPref))
                searchAdapter.notifyDataSetChanged()
                if (searchHistory.historyResults.isNotEmpty())
                    historyVisibility(View.VISIBLE)
            } else {
                historyVisibility(View.GONE)
            }
            if (hasFocus && inputEditText.text.isNotEmpty()) {
                inputEditText.hint = ""
            }
        }

        historyButton.setOnClickListener {
            searchHistory.clearHistory()
            searchAdapter.notifyDataSetChanged()
            historyVisibility(View.GONE)
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
                        placeholderImage.visibility = View.GONE
                        placeholderMessage.visibility = View.GONE
                        updateButton.visibility = View.GONE
                        historyVisibility(View.VISIBLE)
                    }
                } else {
                    historyVisibility(View.GONE)
                }
            }

            override fun afterTextChanged(s: Editable?) {
                // empty
            }
        }
        inputEditText.addTextChangedListener(simpleTextWatcher)

        recycler = findViewById(R.id.track_recycle_view)
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = adapter

        inputEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (inputEditText.text.isNotEmpty()) {
                    search(inputEditText.text.toString())
                    recycler.visibility = View.VISIBLE
                    true
                }
            }
            false
        }

        updateButton.setOnClickListener {
            search(searchQuery)
        }

    }

    private fun historyVisibility(visibilityStatus: Int) {
        historyText.visibility = visibilityStatus
        historyButton.visibility = visibilityStatus
        historyRecycler.visibility = visibilityStatus
    }

    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    private fun search(request: String) {
        itunesService.searchTrack(request)
            .enqueue(object : Callback<TracksResponse> {
                override fun onResponse(call: Call<TracksResponse>,
                                        response: Response<TracksResponse>
                ) {
                    when (response.code()) {
                        200 -> {
                            if (response.body()?.results?.isNotEmpty() == true) {
                                results.clear()
                                results.addAll(response.body()?.results!!)
                                adapter.notifyDataSetChanged()
                                showMessage("", 0, false)
                                //Toast.makeText(applicationContext, "200: ОК", Toast.LENGTH_LONG).show()
                            } else {
                                showMessage(getString(R.string.nothing_found), R.drawable.not_found_placeholder, false)
                                //Toast.makeText(applicationContext, "200: BAD", Toast.LENGTH_LONG).show()
                            }

                        }
                        else -> showMessage(getString(R.string.network_problems), R.drawable.no_network_placeholder, true)
                        //Toast.makeText(applicationContext, "On Response: что-то пошло не так", Toast.LENGTH_LONG).show()
                    }

                }

                override fun onFailure(call: Call<TracksResponse>, t: Throwable) {
                    showMessage(getString(R.string.network_problems), R.drawable.no_network_placeholder, true)
                    //Toast.makeText(applicationContext, "On Failure: что-то пошло не так", Toast.LENGTH_LONG).show()
                }
            })
    }

    private fun showMessage(text: String, resource: Int, buttonVisibility: Boolean) {
        if (text.isNotEmpty()) {
            placeholderMessage.visibility = View.VISIBLE
            placeholderImage.visibility = View.VISIBLE
            results.clear()
            adapter.notifyDataSetChanged()
            placeholderMessage.text = text
            placeholderImage.setImageResource(resource)
            if (buttonVisibility) {
                updateButton.visibility = View.VISIBLE
            } else {
                updateButton.visibility = View.GONE
            }
        } else {
            placeholderMessage.visibility = View.GONE
            placeholderImage.visibility = View.GONE
            updateButton.visibility = View.GONE
        }
    }

}