package com.example.dailyquiz.models

data class QuizHistoryEntry(
    val title: String,
    val date: String,
    val time: String,
    val stars: Int // от 0 до 5
)
