package com.example.playlistmaker.ui.search

import android.content.SharedPreferences
import com.example.playlistmaker.SEARCH_KEY
import com.example.playlistmaker.domain.models.Track
import com.google.gson.Gson

class SearchHistory(private val sharedPreferences: SharedPreferences) {
    var historyResults: MutableList<Track> = mutableListOf()
    private val gson: Gson = Gson()

    fun read(sharedPreferences: SharedPreferences): MutableList<Track> {
        val json = sharedPreferences.getString(SEARCH_KEY, null) ?: return mutableListOf()
        historyResults = gson.fromJson(json, Array<Track>::class.java).toMutableList()
        return historyResults
    }

    fun write(track: Track) {
        // Получаем 1 трек, а не весь список. Изменяем historyResults
        val position = historyResults.indexOf(track) // Проверяем, есть ли этот трек в списке
        if (position != -1)
            historyResults.removeAt(position) // удаляем найденный трек
        if (historyResults.size == 10) { // должно быть не больше 10 треков
            historyResults.removeLast()
        }
        historyResults.add(0, track) // теперь только добавляем в начало

        val json = gson.toJson(historyResults)
        sharedPreferences.edit()
            .putString(SEARCH_KEY, json)
            .apply()
    }

    fun clearHistory() {
        sharedPreferences.edit()
            .remove(SEARCH_KEY)
            .apply()
        historyResults.clear()
    }
}