package com.example.dailyquiz

import android.text.Html
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dailyquiz.api.TriviaQuestion
import com.example.dailyquiz.ui.theme.BackgroundColor

@Composable
fun QuizScreen(questions: List<TriviaQuestion>, onFinish: (correctCount: Int) -> Unit) {
    var currentIndex by remember { mutableStateOf(0) }
    var selectedAnswer by remember { mutableStateOf<String?>(null) }
    var correctCount by remember { mutableStateOf(0) }


    val question = questions[currentIndex]


    val allAnswers = remember(question) {
        (question.incorrect_answers + question.correct_answer).shuffled()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(40.dp))

            // Логотип
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "DAILYQUIZ",
                modifier = Modifier.height(60.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Карточка вопроса
            Box(
                modifier = Modifier
                    .background(Color.White, RoundedCornerShape(32.dp))
                    .padding(24.dp)
                    .fillMaxWidth()
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Вопрос ${currentIndex + 1} из ${questions.size}",
                        color = Color(0xFFB5AFFF),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = Html.fromHtml(question.question).toString(),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    allAnswers.forEach { answer ->
                        OutlinedButton(
                            onClick = { selectedAnswer = answer },
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.outlinedButtonColors(
                                backgroundColor = if (selectedAnswer == answer) Color(0xFFEAE6FF) else Color(
                                    0xFFF5F5F5
                                ),
                                contentColor = Color.Black
                            ),
                            border = if (selectedAnswer == answer)
                                BorderStroke(2.dp, BackgroundColor)
                            else
                                BorderStroke(1.dp, Color.LightGray),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 10.dp),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Start,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                RadioButton(
                                    selected = selectedAnswer == answer,
                                    onClick = { selectedAnswer = answer },
                                    colors = RadioButtonDefaults.colors(
                                        selectedColor = BackgroundColor
                                    )
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = answer,
                                    modifier = Modifier.weight(1f),
                                    textAlign = TextAlign.Start
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = {
                            if (selectedAnswer == question.correct_answer) {
                                correctCount++
                            }

                            if (currentIndex + 1 < questions.size) {
                                currentIndex++
                                selectedAnswer = null
                            } else {
                                onFinish(correctCount)
                            }
                        },
                        enabled = selectedAnswer != null,
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = if (selectedAnswer != null) BackgroundColor else Color.Gray
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("ДАЛЕЕ", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Вернуться к предыдущим вопросам нельзя",
                color = Color.White,
                fontSize = 14.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun QuizScreenPreview() {
    val sampleQuestion = TriviaQuestion(
        question = "Какой язык программирования используется для разработки Android-приложений?",
        correct_answer = "Kotlin",
        incorrect_answers = listOf("Swift", "Python", "JavaScript")
    )

    QuizScreen(
        questions = listOf(sampleQuestion),
        onFinish = {}
    )
}
