package com.example.playlistmaker

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
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

    private val searchBaseUrl = "https://itunes.apple.com"
    private val retrofit = Retrofit.Builder()
        .baseUrl(searchBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val itunesService = retrofit.create(ItunesApi::class.java)

    private val results = ArrayList<Track>()
    private val adapter = TrackAdapter(results)

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        inputEditText = findViewById(R.id.search_edit_text)
        clearButton = findViewById(R.id.search_clear_button)

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
        }

        inputEditText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                inputEditText.hint = ""
            }
        }



        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                searchQuery = s.toString()
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearButton.visibility = clearButtonVisibility(s)
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
                    Toast.makeText(applicationContext, "Выполняем запрос", Toast.LENGTH_LONG).show()
                    search()
                    true
                }
            }
            false
        }

    }

    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    private fun search() {
        itunesService.searchTrack(inputEditText.text.toString())
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
                                Toast.makeText(applicationContext, "200: ОК", Toast.LENGTH_LONG).show()
                                //showMessage("", "")
                            } else {
                                print(response.body().toString())
                                Toast.makeText(applicationContext, "200: BAD, ничего не найдено", Toast.LENGTH_LONG).show()
                                Log.e("Retrofit", "Ошибка: " + response.body().toString());//showMessage(getString(R.string.nothing_found), "")
                            }

                        }
                        else -> Toast.makeText(applicationContext, "On Response: что-то пошло не так", Toast.LENGTH_LONG).show()
                        //showMessage(getString(R.string.something_went_wrong), response.code().toString())
                    }

                }

                override fun onFailure(call: Call<TracksResponse>, t: Throwable) {
                    Toast.makeText(applicationContext, "On Failure: что-то пошло не так", Toast.LENGTH_LONG).show()
                    //showMessage(getString(R.string.something_went_wrong), t.message.toString())
                }

            })
    }

}