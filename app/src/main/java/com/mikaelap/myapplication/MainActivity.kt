package com.mikaelap.myapplication

import android.R.id
import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider


val TimesNewRoman = FontFamily(
    Font(R.font.times, FontWeight.Normal),
    Font(R.font.times, FontWeight.Bold),
    Font(R.font.times, FontWeight.Normal, FontStyle.Italic),
    Font(R.font.times, FontWeight.Bold, FontStyle.Italic)
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        )[MainViewModel::class.java]

        setContent {
            MyApplicationTheme {
                MainScreen(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun MainScreen(viewModel: MainViewModel) {
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val tabs = listOf("Trivia", "Acad", "Fame") //list out each tab

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            TabRow(
                selectedTabIndex = selectedTabIndex,
                containerColor = Color(0xFFFBE475),
                contentColor = Color(0xFF1A2C57)
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        text = {
                            Text(
                                text = title,
                                style = TextStyle(
                                    fontFamily = TimesNewRoman,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier
            .padding(innerPadding)
            .border(width = 2.dp, color = Color(0xFF1A2C57))) {
            when (selectedTabIndex) {
                0 -> TriviaScreen(viewModel = viewModel)
                1 -> AcadScreen()
                2 -> FameScreen()
            }
        }
    }
}

@Composable
fun AcadScreen(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Acad Screen",
            style = TextStyle(
                fontFamily = TimesNewRoman,
                fontSize = 24.sp,
                color = Color(0xFF1A2C57)
            )
        )
    }
}

@Composable
fun FameScreen(modifier: Modifier = Modifier) {

    var showDialog by remember { mutableStateOf(false) }

    if (showDialog) {
        CustomDialog(onDismissRequest = { showDialog = false })
    }

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        TopBanner()
        FameBanner()
        Factoids()
    }
}

@Composable
fun FactoidBoxes(
    modifier: Modifier = Modifier,
    text: String,
    backgroundImage: Int? = null,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(color = Color(0xFF97CDEC))
            .clickable(
                onClick = onClick
            )
            .border(width = 3.dp, color = Color(0xFF1A2C57)),
        contentAlignment = Alignment.Center
    ) {
        if (backgroundImage != null) {
            Image(
                painter = painterResource(id = backgroundImage),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.85f))
        )
        Text(
            text = text,
            color = Color(0xAFFFFFFF),
            fontFamily = TimesNewRoman,
            fontSize = 18.sp,
            textAlign = TextAlign.Center
        )

    }
}

@Composable
fun Factoids() {
    var showDialog by remember { mutableStateOf(false) }

    if (showDialog) {
        CustomDialog(onDismissRequest = { showDialog = false })
    }
    Column(
        modifier = Modifier
            //.fillMaxSize() makes it appear like option 1 in group text
            //.background(Color(0xFF1A2C57))
    ) {
        Row(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            FactoidBoxes(
                modifier = Modifier.weight(1f),
                text = "National Ranking & Academics",
                backgroundImage = R.drawable.academics,
                onClick = { showDialog = true }
            )
            FactoidBoxes(
                modifier = Modifier.weight(1f),
                text = "Sustainability",
                backgroundImage = R.drawable.smithpark,
                onClick = { showDialog = true }
            )
            FactoidBoxes(
                modifier = Modifier.weight(1f),
                text = "Athletics",
                backgroundImage = R.drawable.athletics,
                onClick = { showDialog = true }
            )
        }

        Row(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            FactoidBoxes(
                modifier = Modifier.weight(1f),
                text = "Sylvia Plath",
                backgroundImage = R.drawable.sylviaplath,
                onClick = { showDialog = true }
            )
            FactoidBoxes(
                modifier = Modifier.weight(1f),
                text = "Julia Child",
                backgroundImage = R.drawable.juliachild,
                onClick = { showDialog = true }
            )
            FactoidBoxes(
                modifier = Modifier.weight(1f),
                text = "Nancy Reagan",
                backgroundImage = R.drawable.nancyreagan,
                onClick = { showDialog = true }
            )
        }
    }
}


@Composable
fun TriviaScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val questions by viewModel.allQuestions.observeAsState(emptyList())
    var questionIndex by remember { mutableIntStateOf(0) }

    var grade by remember { mutableDoubleStateOf(0.0) }
    var questionsAnswered by remember { mutableDoubleStateOf(0.0) }
    var questionsCorrect by remember { mutableDoubleStateOf(0.0) }

    val answered = remember {
        mutableStateListOf<Boolean>().apply {
            repeat(10) { add(false) }
        }
    }

    if (questions.isEmpty()) {
        Text("No questions in database")
        return
    }

    val current = questions[questionIndex]

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        TopBanner()
        SecondBanner()

        QuestionBox(current, questionIndex)
        Spacer(modifier = Modifier.height(4.dp))
        Score(questionsCorrect, grade)
        Spacer(modifier = Modifier.height(4.dp))
        AnswerButtons(
            current,
            questionIndex = questionIndex,
            answered,
            onAnswered = { isCorrect ->
                if (!answered[questionIndex]) {
                    answered[questionIndex] = true
                    questionsAnswered++
                    if (isCorrect) {
                        questionsCorrect++
                    }
                }
                grade = if (questionsAnswered > 0) questionsCorrect / questionsAnswered else 0.0
            })

        Spacer(modifier = Modifier.weight(1f))

        BackForwardReset(
            currentIndex = questionIndex,
            total = questions.size,
            onBack = {
                if (questionIndex > 0) questionIndex--
            },
            onNext = {
                if (questionIndex < 9) questionIndex++
            },
            onReset = {
                questionIndex = 0
                questionsAnswered = 0.0
                questionsCorrect = 0.0
                answered.fill(false)
                grade = 0.0
            }
        )
        Spacer(modifier = Modifier.height(8.dp))
    }
}

//function from alert dialogue burner code
@Composable
fun CustomDialog(onDismissRequest: () -> Unit) {
    AlertDialog(
        onDismissRequest = { onDismissRequest() },
        title = { Text(text = "National Ranking & Academics") },
        text = {
            Column {
                Text("Smith College is ranked #13 in National Liberal Arts Colleges and #10 in Best Value Schools. Of Forbes' America's Top College's ranking which accounts for 500 military academies, national universities, and liberal arts colleges, Smith rests at an impressive 135th place. Academics are taken very seriously at Smith, with an emphasis on research opportunities, experiential learning, and a vast selection of majors. The Ada Comstock Scholars Program also encourages students of nontraditional college ages to complete their unfinished degrees. Smith was also the first historically Women's college to offer an undergraduate degree in Engineering.")
                Image(painter = painterResource(id = R.drawable.techcrunch), contentDescription = "Your Image Description")
            }
        },
        confirmButton = {
            Button(
                onClick = { onDismissRequest() }
            ) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun AnswerButtons(
    question: TrivialQuestion,
    questionIndex: Int,
    answered: MutableList<Boolean>,
    onAnswered: (isCorrect: Boolean) -> Unit
) {
    val originalList = question.getShuffledAnswers()
    Column {
        AnswerRow("A", originalList[0], questionIndex, 0, originalList, question, answered, onAnswered)
        Spacer(modifier = Modifier.height(6.dp))
        AnswerRow("B", originalList[1], questionIndex, 1, originalList, question, answered, onAnswered)
        Spacer(modifier = Modifier.height(6.dp))
        AnswerRow("C", originalList[2], questionIndex, 2, originalList, question, answered, onAnswered)
        Spacer(modifier = Modifier.height(6.dp))
        AnswerRow("D", originalList[3], questionIndex, 3, originalList, question, answered, onAnswered)
    }
}

@Composable
fun QuestionBox(question: TrivialQuestion, index: Int) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF1A2C57))
            .height(90.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Text(
            text = "Question ${index + 1}: ${question.questionName}",
            modifier = Modifier.padding(horizontal = 12.dp),
            style = TextStyle(
                fontFamily = TimesNewRoman,
                fontSize = 18.sp,
                color = Color(0xFFFBE475)
            )
        )
    }
}

@Composable
fun AnswerRow(label: String,
              text: String,
              questionIndex: Int,
              listIndex: Int,
              answerIndex: List<String>,
              question: TrivialQuestion,
              answered: List<Boolean>,
              onAnswered: (isCorrect: Boolean) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp)
            .padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Button(
            onClick = {
                val isCorrect = answerIndex[listIndex] == question.correct
                onAnswered(isCorrect)
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = if (answered[questionIndex] && answerIndex[listIndex] == question.correct) Color.Green
                else if (answered[questionIndex] && answerIndex[listIndex] != question.correct) Color.Red
                else
                    Color(0xFF97CDEC),
                contentColor = Color(0xFF1A2C57)

            ),
            shape = CircleShape,
            modifier = Modifier.size(44.dp),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(0.dp)
        ) {
            Text(label,
                textAlign = TextAlign.Center,
                style = TextStyle(
                    fontSize = 18.sp,
                    color = Color(0xFF1A2C57)
                )
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Text(text,
            fontFamily = TimesNewRoman,
            style = TextStyle(
                fontSize = 18.sp,
                color = Color(0xFF1A2C57)
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SecondBanner() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(30.dp)
            .background(Color(0xFFFBE475)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Trivia Questionnaire",
            style = TextStyle(
                fontFamily = TimesNewRoman,
                fontSize = 18.sp,
                color = Color(0xFF1A2C57)
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun FameBanner() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(30.dp)
            .background(Color(0xFFFBE475)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Fame & Achievements",
            style = TextStyle(
                fontFamily = TimesNewRoman,
                fontSize = 18.sp,
                color = Color(0xFF1A2C57)
            )
        )
    }
}

@Composable
fun TopBanner() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(55.dp)
            .background(Color(0xFFFBE475)),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(start = 8.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.smith),
                contentDescription = "Smith College Logo",
                modifier = Modifier
                    .size(45.dp)
                    .padding(end = 8.dp)
            )

            Text(
                text = "Smith College",
                style = TextStyle(
                    fontFamily = TimesNewRoman,
                    fontSize = 24.sp,
                    color = Color(0xFF1A2C57)
                )
            )
        }
    }
}

@Composable
fun BackForwardReset(
    currentIndex: Int,
    total: Int,
    onBack: () -> Unit,
    onNext: () -> Unit,
    onReset: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(75.dp)
            .padding(horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        Button(
            onClick = onBack,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF97CDEC),
                contentColor = Color(0xFF1A2C57)
            ),
            shape = CircleShape,
            modifier = Modifier.size(68.dp),
            enabled = currentIndex > 0,
            contentPadding = androidx.compose.foundation.layout.PaddingValues(0.dp)
        ) {
            Text("Back", fontSize = 14.sp)
        }

        Button(
            onClick = onReset,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF97CDEC),
                contentColor = Color(0xFF1A2C57)
            ),
            shape = CircleShape,
            modifier = Modifier.size(68.dp),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(0.dp)
        ) {
            Text("Reset", fontSize = 14.sp)
        }

        Button(
            onClick = onNext,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF97CDEC),
                contentColor = Color(0xFF1A2C57)
            ),
            shape = CircleShape,
            modifier = Modifier.size(68.dp),
            enabled = currentIndex < total - 1,
            contentPadding = androidx.compose.foundation.layout.PaddingValues(0.dp)
        ) {
            Text("Next", fontSize = 14.sp)
        }
    }
}

@SuppressLint("DefaultLocale")
@Composable
fun Score(questionsCorrect: Double, grade: Double) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(24.dp)
            .padding(horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = "Grade:  ${questionsCorrect.toInt()}/10",
            fontFamily = TimesNewRoman,
            style = TextStyle(
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1A2C57)
            )
        )
    }
}
