package com.example.dailyquiz.screens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.dailyquiz.R
import com.example.dailyquiz.api.TriviaQuestion
import com.example.dailyquiz.api.fetchTriviaQuestions
import com.example.dailyquiz.models.QuizHistoryEntry
import com.example.dailyquiz.ui.theme.BackgroundColor
import com.example.dailyquiz.viewmodels.HistoryViewModel
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppRoot()
        }
    }
}

@Composable
fun AppRoot() {
    var screenState by remember { mutableStateOf<ScreenState>(ScreenState.Welcome) }
    val history = remember { mutableStateListOf<QuizHistoryEntry>() }
    var selectedEntry by remember { mutableStateOf<QuizHistoryEntry?>(null) }

    val historyViewModel: HistoryViewModel = viewModel()

    when (val state = screenState) {
        is ScreenState.Welcome -> DailyQuizMainScreen(
            onStartClick = { screenState = ScreenState.Loading },
            onHistoryClick = { screenState = ScreenState.History(history.toList()) }
        )
        is ScreenState.Loading -> {
            LoaderScreen()
            LaunchedEffect(Unit) {
                val questions = fetchTriviaQuestions()
                screenState = ScreenState.Quiz(questions)
            }
        }
        is ScreenState.Quiz -> QuizScreen(
            questions = state.questions,
            onFinish = { score, results ->
                val quizNumber = history.size + 1
                val entry = QuizHistoryEntry(
                    quizTitle = "Quiz $quizNumber",
                    date = getCurrentDate(),
                    time = getCurrentTime(),
                    score = score,
                    questions = results
                )
                history.add(0, entry)
                screenState = ScreenState.Result(score, state.questions.size)
            },
            onBack = { screenState = ScreenState.Welcome }
        )
        is ScreenState.Result -> ResultScreen(
            score = state.score,
            total = state.total,
            onRestart = { screenState = ScreenState.Loading }
        )
        is ScreenState.History -> {
            // Загрузка истории каждый раз при открытии экрана
            LaunchedEffect(Unit) {
                historyViewModel.loadEntries(history)
            }
            HistoryScreen(
                viewModel = historyViewModel,
                onBack = { screenState = ScreenState.Welcome },
                onDelete = { entry -> history.remove(entry) },
                onEntryClick = {
                    selectedEntry = it
                    screenState = ScreenState.HistoryDetail
                },
                onStartClick = { screenState = ScreenState.Loading }
            )
        }
        is ScreenState.HistoryDetail -> {
            selectedEntry?.let { entry ->
                HistoryDetailScreen(
                    entry = entry,
                    onBack = { screenState = ScreenState.History(history.toList()) }
                )
            }
        }
    }
}



sealed class ScreenState {
    object Welcome : ScreenState()
    object Loading : ScreenState()
    data class Quiz(val questions: List<TriviaQuestion>) : ScreenState()
    data class Result(val score: Int, val total: Int) : ScreenState()
    data class History(val entries: List<QuizHistoryEntry>) : ScreenState()
    object HistoryDetail : ScreenState()
    //object History : ScreenState()

}


@Composable
fun DailyQuizMainScreen(onStartClick: () -> Unit, onHistoryClick: () -> Unit) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor),
        contentAlignment = Alignment.TopCenter
    ) {
        Spacer(modifier = Modifier.height(40.dp))

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 32.dp, bottom = 64.dp)
        ) {
            Spacer(modifier = Modifier.height(40.dp))

            // Кнопка История
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                OutlinedButton(
                    onClick = { onHistoryClick() },
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White)
                ) {
                    Text(text = "История", color = BackgroundColor)
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        imageVector = Icons.Default.History,
                        contentDescription = "История",
                        tint = BackgroundColor
                    )
                }
            }

            Spacer(modifier = Modifier.height(100.dp))

            // Логотип
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Логотип DAILYQUIZ",
                modifier = Modifier
                    .height(70.dp),
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.height(40.dp))

            // Приветствие и кнопка
            Column(
                modifier = Modifier
                    .padding(horizontal = 24.dp)
                    .background(Color.White, RoundedCornerShape(24.dp))
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Добро пожаловать\nв DailyQuiz!",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        onStartClick()
                    },
                    colors = ButtonDefaults.buttonColors(backgroundColor = BackgroundColor),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "НАЧАТЬ ВИКТОРИНУ",
                        color = Color.White,
                        fontSize = 18.sp
                    )
                }
            }
        }
    }
}


@Composable
fun LoaderScreen() {


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF7A5FFF)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Логотип
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Логотип DAILYQUIZ",
                modifier = Modifier
                    .height(70.dp),
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.height(32.dp))

            CircularProgressIndicator(
                color = Color.White,
                strokeWidth = 4.dp
            )
        }
    }
}

fun getCurrentDate(): String {
    val format = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
    return format.format(Date())
}

fun getCurrentTime(): String {
    val format = SimpleDateFormat("HH:mm", Locale.getDefault())
    return format.format(Date())
}

fun calculateStars(score: Int, total: Int): Int {
    val percentage = score.toFloat() / total
    return when {
        percentage >= 1f -> 5
        percentage >= 0.8f -> 4
        percentage >= 0.6f -> 3
        percentage >= 0.4f -> 2
        percentage >= 0.2f -> 1
        else -> 0
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewScreens() {
    AppRoot()
}