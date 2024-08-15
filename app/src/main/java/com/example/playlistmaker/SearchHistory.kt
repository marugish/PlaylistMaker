package com.example.playlistmaker

import android.content.SharedPreferences
import com.google.gson.Gson

class SearchHistory(private val sharedPreferences: SharedPreferences) {
    var historyResults = ArrayList<Track>()
    //private val searchAdapter = TrackAdapter(historyResults, searchHistory)
    // чтение
    fun read(sharedPreferences: SharedPreferences): Array<Track> {
        val json = sharedPreferences.getString(SEARCH_KEY, null) ?: return emptyArray()
        return Gson().fromJson(json, Array<Track>::class.java)
    }

    // запись
    fun write(track: Track) {
        // Будем получать 1 объект, а не список
        // для этого буду изменять historyResults
        val position = historyResults.indexOf(track)
        if (position != -1)
            historyResults.removeAt(position)
        // Есть ли этот трек уже в списке
        if (historyResults.size == 10) {
            historyResults.removeLast()
        }
        historyResults.add(0, track)

        // должно быть не больше 10 треков
        // если такой трек уже есть в списке, то тогда его нужно удалить, а потом заново добавить

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

    // понять, где будут обновления списка
}