package com.example.dailyquiz.models

import kotlinx.serialization.Serializable

@Serializable
data class TriviaResponse(
    val results: List<TriviaQuestion>
)
