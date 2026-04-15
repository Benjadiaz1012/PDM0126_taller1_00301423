package com.pdm0126.taller01_00301423

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import com.pdm0126.taller01_00301423.ui.theme.AndroidPediaByDiazTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AndroidPediaByDiazTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    QuizApp(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

data class Question(
    val id: Int,
    val question: String,
    val options: List<String>,
    val correctAnswer: String,
    val funFact: String,
)

val quizQuestions = listOf(
    Question(
        id = 1,
        question = "¿Donde se fundó la compañia de Andorid Inc?",
        options = listOf(
            "Palo Alto, California",
            "Mejicanos, San Salvador",
            "Los Angeles, California",
            "San Dieego, California"
        ),
        correctAnswer = "Palo Alto, California",
        funFact = "Para ser exactos fue en octubre del 2003."
    ),
    Question(
        id = 2,
        question = "¿En que año se creo el Andorid 1.5?",
        options = listOf(
            "2008",
            "2007",
            "2006",
            "2009"
        ),
        correctAnswer = "2009",
        funFact = "De hecho fue el mismo año de salio Andorid 1.0 y la 1.1."
    ),
    Question(
        id = 3,
        question = "¿Quien compro Andorid?",
        options = listOf(
            "Google",
            "Apple",
            "Kotlin",
            "Nitendo"
        ),
        correctAnswer = "Google",
        funFact = "Google compro Android en menos de 5 años"
    )
)

@Composable
fun QuizApp(modifier: Modifier = Modifier) {
    var screenState by rememberSaveable { mutableIntStateOf(0) }
    var currentIndex by rememberSaveable { mutableIntStateOf(0) }
    var score by rememberSaveable { mutableIntStateOf(0) }

    when (screenState) {
        0 -> StartScreen(
            modifier = modifier,
            onStartClick = {
                screenState = 1
            }
        )

        1 -> QuizScreen(
            modifier = modifier,
            currentIndex = currentIndex,
            score = score,
            isLastQuestion = currentIndex == quizQuestions.size - 1,
            onOptionClick = { option ->
                val currentQuestion = quizQuestions[currentIndex]
                if (option == currentQuestion.correctAnswer) {
                    score++
                }
            },
            onNextClick = {
                if (currentIndex < quizQuestions.size - 1) {
                    currentIndex++
                } else {
                    screenState = 2
                }
            }
        )

        2 -> EndScreen(
            modifier = modifier,
            score = score,
            onRestartClick = {
                currentIndex = 0
                score = 0
                screenState = 0
            }
        )
    }
}

@Composable
fun StartScreen(
    modifier: Modifier = Modifier,
    onStartClick: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "AndroidPedia")

        Spacer(modifier = Modifier.height(8.dp))

        Text(text = "¿Cuánto sabes de Android?")

        Spacer(modifier = Modifier.height(8.dp))

        Text(text = "Oscar Benjamin Diaz Escalante 00301423")

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onStartClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Comenzar Quiz")
        }
    }
}

@Composable
fun QuizScreen(
    modifier: Modifier = Modifier,
    currentIndex: Int,
    score: Int,
    onOptionClick: (String) -> Unit,
    onNextClick: () -> Unit,
    isLastQuestion: Boolean
) {
    val currentQuestion = quizQuestions[currentIndex]

    var answered by rememberSaveable(currentIndex) { mutableStateOf(false) }
    var selectedOption by rememberSaveable(currentIndex) { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(text = "Pregunta ${currentIndex + 1} de ${quizQuestions.size}")
        Text(text = "Puntaje: $score / ${quizQuestions.size}")

        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = currentQuestion.question,
                modifier = Modifier.padding(16.dp)
            )
        }
        currentQuestion.options.forEach { option ->

            val buttonColor = when {
                !answered -> Color.LightGray

                option == currentQuestion.correctAnswer -> Color.Green

                option == selectedOption && option != currentQuestion.correctAnswer ->
                    Color.Red

                else -> Color.LightGray
            }

            Button(
                onClick = {
                    if (!answered) {
                        selectedOption = option
                        answered = true
                        onOptionClick(option)
                    }
                },
                enabled = !answered,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = buttonColor,
                    disabledContainerColor = buttonColor,
                    contentColor = Color.Black,
                    disabledContentColor = Color.Black
                )
            ) {
                Text(text = option)
            }
        }

        if (answered) {
            Text(
                text = currentQuestion.funFact,
                modifier = Modifier.padding(top = 16.dp)
            )

            Button(
                onClick = onNextClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = if (isLastQuestion) "Ver Resultado" else "Siguiente"
                )
            }
        }
    }
}

@Composable
fun EndScreen(
    modifier: Modifier = Modifier,
    score: Int,
    onRestartClick: () -> Unit
) {
    val message = when (score) {
        3 -> "Perfecto. Obtuviste la mejor nota"
        2 -> "Muy bien. Casi obitenes la nota perfecta"
        1 -> "Bien. Solo tuviste una buena"
        else -> "Lo lamento, no obtuviste ni una buena."
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Resultado Final")

        Spacer(modifier = Modifier.height(8.dp))

        Text(text = "Obtuviste $score de ${quizQuestions.size}")

        Spacer(modifier = Modifier.height(8.dp))

        Text(text = message)

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onRestartClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Reiniciar Quiz")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun QuizAppPreview() {
    AndroidPediaByDiazTheme {
        QuizApp()
    }
}