package com.example.dailyquiz

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.delay
import androidx.compose.foundation.Image
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.example.dailyquiz.ui.theme.BackgroundColor

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
    var isLoading by remember { mutableStateOf(false) }

    if (isLoading) {
        LoaderScreen()
    } else {
        DailyQuizMainScreen(onStartClick = {
            isLoading = true
        })
    }
}

@Composable
fun DailyQuizMainScreen(onStartClick: () -> Unit) {
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
                    onClick = { /* TODO */ },
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
                    onClick = onStartClick,
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

    LaunchedEffect(Unit) {
        delay(2000L)
        // Здесь можно будет перейти на следующий экран после загрузки
        // Пока оставим просто лоадер
    }

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

@Preview(showBackground = true)
@Composable
fun PreviewScreens() {
    AppRoot()
}
