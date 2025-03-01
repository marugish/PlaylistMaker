package com.example.playlistmaker.util

fun getTrackCountMessage(trackCount: Int): String {
    return when {
        trackCount % 10 == 1 && trackCount % 100 != 11 -> "$trackCount трек"
        trackCount % 10 in 2..4 && (trackCount % 100 !in 12..14) -> "$trackCount трека"
        else -> "$trackCount треков"
    }
}

fun getTrackCountMinutes(minutes: Int): String {
    return when {
        minutes % 10 == 1 && minutes % 100 != 11 -> "$minutes минута"
        minutes % 10 in 2..4 && (minutes % 100 !in 12..14) -> "$minutes минуты"
        else -> "$minutes минут"
    }
}