package com.example.dailyquiz.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dailyquiz.models.QuestionResult
import com.example.dailyquiz.models.QuizHistoryEntry
import com.example.dailyquiz.ui.theme.CorrectAnswerColor
import com.example.dailyquiz.ui.theme.IncorrectAnswerColor

@Composable
fun QuestionCard(
    questionText: String,
    answers: List<String>,
    correctAnswer: String,
    selectedAnswer: String?,
    onSelectAnswer: ((String) -> Unit)? = null,
    showAnswerResult: Boolean = false
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {

        Text(
            text = questionText,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        answers.forEach { answer ->
            val isSelected = selectedAnswer == answer
            val isCorrect = answer == correctAnswer

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
                onClick = { if (onSelectAnswer != null && !showAnswerResult) onSelectAnswer(answer) },
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    backgroundColor = backgroundColor,
                    contentColor = textColor
                ),
                border = BorderStroke(2.dp, borderColor),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 3.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    RadioButton(
                        selected = isSelected,
                        onClick = {
                            if (onSelectAnswer != null && !showAnswerResult) onSelectAnswer(answer)
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
                        textAlign = TextAlign.Start,
                        color = textColor,
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun QuestionCardPreview() {
    MaterialTheme {
        Surface(modifier = Modifier.padding(16.dp)) {
            QuestionCard(
                questionText = "Какой язык программирования используется для Android?",
                answers = listOf("Kotlin", "Java", "Python", "Swift"),
                correctAnswer = "Kotlin",
                selectedAnswer = "Java",
                onSelectAnswer = null, // read-only для превью
                showAnswerResult = true
            )
        }
    }
}
