package com.example.dailyquiz.screens

import android.text.Html
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.dailyquiz.models.QuizHistoryEntry
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.example.dailyquiz.R
import com.example.dailyquiz.components.HistoryCard
import com.example.dailyquiz.components.QuestionCard
import com.example.dailyquiz.models.QuestionResult
import com.example.dailyquiz.ui.theme.BackgroundColor

@Composable
fun HistoryDetailScreen(entry: QuizHistoryEntry, onBack: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor)
            .padding(16.dp)
    ) {
        Column {
            IconButton(onClick = onBack, Modifier.padding(top = 16.dp)) {
                Icon(
                    painter = painterResource(id = R.drawable.arrow_back_icon),
                    contentDescription = "Назад",
                    tint = Color.White
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Результаты: ${entry.quizTitle}",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                itemsIndexed(entry.questions) { index, question ->
                    Card(
                        shape = RoundedCornerShape(24.dp),
                        backgroundColor = Color.White,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(20.dp)) {

                            Text(
                                text = "Вопрос ${index + 1} из ${entry.questions.size}",
                                color = Color.Gray,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            QuestionCard(
                                questionText = question.question,
                                answers = question.allAnswers,
                                correctAnswer = question.correctAnswer,
                                selectedAnswer = question.selectedAnswer,
                                onSelectAnswer = null,
                                showAnswerResult = true
                            )

                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HistoryDetailScreenPreview() {
    val mockEntry = QuizHistoryEntry(
        quizTitle = "Общая викторина",
        date = "03.08.2025",
        time = "14:00",
        score = 7,
        questions = listOf(
            QuestionResult(
                question = "Какой язык используется для Android разработки?",
                selectedAnswer = "Kotlin",
                isCorrect = true,
                correctAnswer = "Kotlin",
                allAnswers = listOf("Kotlin", "Java", "Python", "Swift")
            ),
            QuestionResult(
                question = "Столица Франции?",
                selectedAnswer = "Лондон",
                isCorrect = false,
                correctAnswer = "Париж",
                allAnswers = listOf("Париж", "Лондон", "Берлин", "Рим")
            )
        )
    )

    HistoryDetailScreen(entry = mockEntry, onBack = {})
}
