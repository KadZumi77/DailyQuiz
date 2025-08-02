package com.example.dailyquiz

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
import com.example.dailyquiz.ui.theme.BackgroundColor
import com.example.dailyquiz.ui.theme.TextScoreColor

@Composable
fun ResultScreen(score: Int, total: Int, onRestart: () -> Unit) {
    val percentage = score.toFloat() / total
    val stars = when {
        percentage >= 1f -> 5
        percentage >= 0.8f -> 4
        percentage >= 0.6f -> 3
        percentage >= 0.4f -> 2
        percentage >= 0.2f -> 1
        else -> 0
    }

    val message = when (stars) {
        5 -> "Идеально!\n${score}/$total — вы ответили на всё правильно. Это блестящий результат!"
        4 -> "Почти идеально!\n${score}/$total — очень близко к совершенству. Ещё один шаг!"
        3 -> "Хороший результат!\n${score}/$total — вы на верном пути. Продолжайте тренироваться!"
        2 -> "Есть над чем поработать\n${score}/$total — не расстраивайтесь, попробуйте ещё раз!"
        1 -> "Сложный вопрос?\n${score}/$total — иногда просто не ваш день. Следующая попытка будет лучше!"
        else -> "Бывает и так!\n${score}/$total — не отчаивайтесь. Начните заново и удивите себя!"
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(24.dp)
        ) {
            Text("Результаты", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color.White)

            Spacer(modifier = Modifier.height(24.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White, shape = RoundedCornerShape(32.dp))
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Звёзды
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        repeat(5) { index ->
                            Image(
                                painter = painterResource(
                                    id = if (index < stars)
                                        R.drawable.ic_star_filled
                                    else
                                        R.drawable.ic_star_outline
                                ),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(50.dp)
                                    .padding(horizontal = 6.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        "$score из $total",
                        fontSize = 18.sp,
                        color = TextScoreColor,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = message,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = onRestart,
                        colors = ButtonDefaults.buttonColors(backgroundColor = BackgroundColor),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text("НАЧАТЬ ЗАНОВО", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }

        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewResultScreen() {
    ResultScreen(score = 4, total = 5, onRestart = {})
}
