package com.example.dailyquiz.models

data class QuizHistoryEntry(
    val quizTitle: String,
    val date: String,
    val time: String,
    val score: Int,
    val questions: List<QuestionResult> = emptyList()
)

data class QuestionResult(
    val question: String,
    val selectedAnswer: String,
    val isCorrect: Boolean,
    val correctAnswer: String,
    val allAnswers: List<String>
)


