package com.example.dailyquiz.screens

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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dailyquiz.R
import com.example.dailyquiz.api.TriviaQuestion
import com.example.dailyquiz.ui.theme.BackgroundColor
import com.example.dailyquiz.ui.theme.CorrectAnswerColor
import com.example.dailyquiz.ui.theme.IncorrectAnswerColor
import com.example.dailyquiz.ui.theme.TimerColor

@Composable
fun QuizScreen(questions: List<TriviaQuestion>, onFinish: (correctCount: Int) -> Unit, onBack: () -> Unit) {
    var currentIndex by remember { mutableStateOf(0) }
    var selectedAnswer by remember { mutableStateOf<String?>(null) }
    var correctCount by remember { mutableStateOf(0) }
    var showAnswerResult by remember { mutableStateOf(false) }
    var isCorrectAnswer by remember { mutableStateOf(false) }
    var triggerNext by remember { mutableStateOf(false) }
    var isTimeUp by remember { mutableStateOf(false) }
    var restartTrigger by remember { mutableStateOf(false) }
    var showExitDialog by remember { mutableStateOf(false) }


    val question = questions[currentIndex]


    val allAnswers = remember(question) {
        (question.incorrect_answers + question.correct_answer).shuffled()
    }

    var timeLeft by remember { mutableStateOf(300) } // 5 минут
    val totalTime = 300f

    LaunchedEffect(restartTrigger) {
        timeLeft = 300
        while (timeLeft > 0) {
            kotlinx.coroutines.delay(1000L)
            timeLeft--
            if (timeLeft == 0) {
                isTimeUp = true
            }
        }
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


    if (isTimeUp) {
        AlertDialog(

            onDismissRequest = { /* нельзя закрыть по тапу вне */ },
            title = {
                Text(
                    "Время вышло!",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            text = {
                Text(
                    "Вы не успели завершить викторину. Попробуйте ещё раз!",
                    fontSize = 16.sp,
                    color = Color.Black,
                    textAlign = TextAlign.Center

                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        currentIndex = 0
                        selectedAnswer = null
                        correctCount = 0
                        showAnswerResult = false
                        isCorrectAnswer = false
                        triggerNext = false
                        isTimeUp = false
                        restartTrigger = !restartTrigger
                    },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = BackgroundColor
                    ),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                ) {
                    Text("НАЧАТЬ ЗАНОВО", color = Color.White, fontWeight = FontWeight.Bold)
                }
            },
            shape = RoundedCornerShape(24.dp),
            backgroundColor = Color.White
        )
    }

    if (showExitDialog) {
        AlertDialog(
            onDismissRequest = { showExitDialog = false },
            title = {
                Text(
                    "Вы точно хотите выйти?",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            text = {
                Text(
                    "Результат текущей викторины не будет сохранён.",
                    fontSize = 16.sp,
                    color = Color.Black,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showExitDialog = false
                        onBack()
                    },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = IncorrectAnswerColor
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("ДА", color = Color.White, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                Button(
                    onClick = { showExitDialog = false },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = BackgroundColor
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("НЕТ", color = Color.White, fontWeight = FontWeight.Bold)
                }
            },
            shape = RoundedCornerShape(24.dp),
            backgroundColor = Color.White
        )
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
                    onClick = { showExitDialog = true },
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

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = String.format("%02d:%02d", (totalTime - timeLeft).toInt() / 60, (totalTime - timeLeft).toInt() % 60),
                            color = TimerColor,
                            fontSize = 14.sp
                        )

                        Text(
                            text = "05:00",
                            color = TimerColor,
                            fontSize = 14.sp
                        )
                    }

                    LinearProgressIndicator(
                        progress = (totalTime - timeLeft) / totalTime,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(RoundedCornerShape(50)),
                        backgroundColor = Color(0xFFE0E0E0),
                        color = TimerColor
                    )

                    Spacer(modifier = Modifier.height(16.dp))

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
                            showAnswerResult && isSelected && isCorrect -> Color(0xFFDFF5DD)
                            showAnswerResult && isSelected && !isCorrect -> Color(0xFFFFEAEA)
                            else -> Color(0xFFF5F5F5)
                        }

                        val borderColor = when {
                            showAnswerResult && isSelected && isCorrect -> CorrectAnswerColor
                            showAnswerResult && isSelected && !isCorrect -> IncorrectAnswerColor
                            else -> Color.LightGray
                        }

                        val radioColor = when {
                            showAnswerResult && isSelected && isCorrect -> CorrectAnswerColor
                            showAnswerResult && isSelected && !isCorrect -> IncorrectAnswerColor
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
                                    color = textColor,
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
