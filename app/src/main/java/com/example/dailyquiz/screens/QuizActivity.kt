package com.example.dailyquiz.screens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import com.example.dailyquiz.api.TriviaQuestion
import com.example.dailyquiz.api.fetchTriviaQuestions

class QuizActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            QuizActivityContent()
        }
    }
}

@Composable
fun QuizActivityContent() {
    var questions by remember { mutableStateOf<List<TriviaQuestion>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var isFinished by remember { mutableStateOf(false) }
    var correctAnswersCount by remember { mutableStateOf(0) }
    var shouldReload by remember { mutableStateOf(true) }
    val activity = LocalActivity.current



    // Загружаем вопросы при запуске или перезапускe
    LaunchedEffect(shouldReload) {
        isLoading = true
        questions = fetchTriviaQuestions()
        isLoading = false
        shouldReload = false
    }

    when {
        isLoading -> LoaderScreen()
        isFinished -> ResultScreen(
            score = correctAnswersCount,
            total = questions.size,
            onRestart = {
                correctAnswersCount = 0
                isFinished = false
                shouldReload = true
            }
        )
        else -> QuizScreen(
            questions = questions,
            onFinish = { score, results ->
                correctAnswersCount = score
                isFinished = true
            }
            ,
            onBack = {
                activity?.finish()
            }
        )
    }
}
