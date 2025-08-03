package com.example.dailyquiz.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dailyquiz.models.QuizHistoryEntry
import com.example.dailyquiz.ui.theme.BackgroundColor
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.example.dailyquiz.R
import com.example.dailyquiz.components.HistoryCard
import com.example.dailyquiz.viewmodels.HistoryViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HistoryScreen(
    viewModel: HistoryViewModel,
    onBack: () -> Unit,
    onDelete: (QuizHistoryEntry) -> Unit,
    onEntryClick: (QuizHistoryEntry) -> Unit
) {

    val historyEntries = viewModel.entries
    var showDeleteDialog by remember { mutableStateOf(false) }
    var deletedEntry by remember { mutableStateOf<QuizHistoryEntry?>(null) }
    var selectedEntry by remember { mutableStateOf<QuizHistoryEntry?>(null) }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 32.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, bottom = 16.dp),
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

                Text(
                    text = "История",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.align(Alignment.Center)
                )
            }


            Spacer(modifier = Modifier.height(24.dp))

            if (selectedEntry == null) {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(historyEntries, key = { it.hashCode() }) { entry ->
                        var showMenu by remember { mutableStateOf(false) }

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight()
                                .combinedClickable(
                                    onClick = { onEntryClick(entry) }, // показать детали
                                    onLongClick = { showMenu = true } // удалить попытку
                                )
                        ) {
                            HistoryCard(entry)

                            DropdownMenu(
                                expanded = showMenu,
                                onDismissRequest = { showMenu = false }
                            ) {
                                DropdownMenuItem(onClick = {
                                    deletedEntry = entry
                                    viewModel.deleteEntry(entry)
                                    onDelete(entry)
                                    showMenu = false
                                    showDeleteDialog = true
                                }) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.trash_icon),
                                        contentDescription = "Удалить",
                                        tint = Color.Black
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Удалить")
                                }
                            }
                        }
                    }
                }
            } else {
                // Детали викторины
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    item {
                        Text(
                            text = "Результаты викторины: ${selectedEntry?.quizTitle}",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                    items(selectedEntry!!.questions) { question ->
                        Card(
                            shape = RoundedCornerShape(12.dp),
                            backgroundColor = Color.White,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text("Вопрос: ${question.question}")
                                Text("Ваш ответ: ${question.selectedAnswer}")
                                Text("Правильно: ${if (question.isCorrect) "Да" else "Нет"}")
                            }
                        }
                    }

                    item {
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { selectedEntry = null },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(backgroundColor = Color.White)
                        ) {
                            Text("Назад", color = BackgroundColor)
                        }
                    }
                }
            }


        }
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = {
                Text(
                    "Попытка удалена",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            text = {
                Text(
                    "Вы можете пройти викторину снова, когда будете готовы.",
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("ЗАКРЫТЬ", color = BackgroundColor, fontWeight = FontWeight.Bold)
                }
            },
            shape = RoundedCornerShape(24.dp),
            backgroundColor = Color.White
        )
    }

}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HistoryScreenPreview() {
    val sampleEntries = listOf(
        QuizHistoryEntry("Quiz 1", "7 июля", "12:03", 1),
        QuizHistoryEntry("Quiz 2", "8 июля", "14:15", 1),
        QuizHistoryEntry("Quiz 3", "9 июля", "09:30", 2),
        QuizHistoryEntry("Quiz 4", "10 июля", "16:45", 4),
        QuizHistoryEntry("Quiz 5", "11 июля", "11:00", 1),
    )
    val fakeViewModel = object : HistoryViewModel() {
        init {
            loadEntries(sampleEntries)
        }
    }
    HistoryScreen(
        viewModel = fakeViewModel,
        onBack = {},
        onDelete = {},
        onEntryClick = {}
    )
}
