package com.example.dailyquiz

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun HistoryScreen(entries: List<QuizHistoryEntry>, onBack: () -> Unit) {
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

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(entries) { entry ->
                    HistoryCard(entry)
                }
            }
        }
    }
}

@Preview
@Composable
fun HistoryScreenPreview() {
    val sampleEntries = listOf(
        QuizHistoryEntry("Quiz 1", "7 июля", "12:03", 1),
        QuizHistoryEntry("Quiz 2", "8 июля", "14:15", 1),
        QuizHistoryEntry("Quiz 3", "9 июля", "09:30", 2),
        QuizHistoryEntry("Quiz 4", "10 июля", "16:45", 4),
        QuizHistoryEntry("Quiz 5", "11 июля", "11:00", 1),
    )
    HistoryScreen(entries = sampleEntries, onBack = {})
}
