package com.example.dailyquiz.api

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

suspend fun fetchTriviaQuestions(): List<TriviaQuestion> {
    val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
            })
        }
    }

    val response: TriviaResponse = client
        .get("https://opentdb.com/api.php?amount=5&category=27&difficulty=easy&type=multiple")
        .body()

    client.close()
    return response.results
}

@Serializable
data class TriviaResponse(val results: List<TriviaQuestion>)

@Serializable
data class TriviaQuestion(
    val question: String,
    val correct_answer: String,
    val incorrect_answers: List<String>
)
