package com.example.dailyquiz.models

import kotlinx.serialization.Serializable

@Serializable
data class TriviaQuestion(
    val question: String,
    val correct_answer: String,
    val incorrect_answers: List<String>
)
