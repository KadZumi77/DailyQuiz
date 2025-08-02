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
import com.example.dailyquiz.ui.theme.CorrectAnswerColor
import com.example.dailyquiz.ui.theme.IncorrectAnswerColor

@Composable
fun QuizScreen(questions: List<TriviaQuestion>, onFinish: (correctCount: Int) -> Unit, onBack: () -> Unit) {
    var currentIndex by remember { mutableStateOf(0) }
    var selectedAnswer by remember { mutableStateOf<String?>(null) }
    var correctCount by remember { mutableStateOf(0) }
    var showAnswerResult by remember { mutableStateOf(false) }
    var isCorrectAnswer by remember { mutableStateOf(false) }
    var triggerNext by remember { mutableStateOf(false) }


    val question = questions[currentIndex]


    val allAnswers = remember(question) {
        (question.incorrect_answers + question.correct_answer).shuffled()
    }

    LaunchedEffect(triggerNext) {
        if (triggerNext) {
            kotlinx.coroutines.delay(1500)
            if (currentIndex + 1 < questions.size) {
                currentIndex++
                selectedAnswer = null
                showAnswerResult = false
                triggerNext = false
            } else {
                onFinish(correctCount)
            }
        }
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

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, bottom = 16.dp)
            ) {
                IconButton(
                    onClick = { onBack() },
                    modifier = Modifier.align(Alignment.CenterStart)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.arrow_back_icon),
                        contentDescription = "Назад",
                        tint = Color.White
                    )
                }

                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "DAILYQUIZ",
                    modifier = Modifier
                        .height(60.dp)
                        .align(Alignment.Center)
                )
            }


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
                        val isSelected = selectedAnswer == answer
                        val isCorrect = answer == question.correct_answer

                        val backgroundColor = when {
                            showAnswerResult && isSelected && isCorrect -> Color(0xFFDFF5DD) // зелёный фон
                            showAnswerResult && isSelected && !isCorrect -> Color(0xFFFFEAEA) // красный фон
                            else -> Color(0xFFF5F5F5)
                        }

                        val borderColor = when {
                            showAnswerResult && isSelected && isCorrect -> Color(0xFF4CAF50) // зелёная рамка
                            showAnswerResult && isSelected && !isCorrect -> Color(0xFFFF5252) // красная рамка
                            else -> Color.LightGray
                        }

                        val radioColor = when {
                            showAnswerResult && isSelected && isCorrect -> Color(0xFF4CAF50)
                            showAnswerResult && isSelected && !isCorrect -> Color(0xFFFF5252)
                            else -> Color.Gray
                        }

                        val textColor = Color.Black

                        OutlinedButton(
                            onClick = { if (!showAnswerResult) selectedAnswer = answer },
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.outlinedButtonColors(
                                backgroundColor = backgroundColor,
                                contentColor = textColor
                            ),
                            border = BorderStroke(2.dp, borderColor),
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
                                    selected = isSelected,
                                    onClick = {
                                        if (!showAnswerResult) selectedAnswer = answer
                                    },
                                    colors = RadioButtonDefaults.colors(
                                        selectedColor = radioColor,
                                        unselectedColor = radioColor,
                                        disabledColor = Color.Gray
                                    )
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = answer,
                                    modifier = Modifier.weight(1f),
                                    textAlign = TextAlign.Start,
                                    color = textColor
                                )
                            }
                        }
                    }



                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = {
                            showAnswerResult = true
                            isCorrectAnswer = selectedAnswer == question.correct_answer

                            if (isCorrectAnswer) correctCount++

                            triggerNext = true
                        },
                        enabled = selectedAnswer != null && !showAnswerResult,
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = if (selectedAnswer != null && !showAnswerResult) BackgroundColor else Color.Gray
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
        onFinish = {},
        onBack = {}
    )
}
