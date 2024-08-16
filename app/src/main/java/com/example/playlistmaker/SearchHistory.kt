package com.example.playlistmaker

import android.content.SharedPreferences
import com.google.gson.Gson

class SearchHistory(private val sharedPreferences: SharedPreferences) {
    var historyResults = ArrayList<Track>()

    fun read(sharedPreferences: SharedPreferences): Array<Track> {
        val json = sharedPreferences.getString(SEARCH_KEY, null) ?: return emptyArray()
        return Gson().fromJson(json, Array<Track>::class.java)
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

        val json = Gson().toJson(historyResults)
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