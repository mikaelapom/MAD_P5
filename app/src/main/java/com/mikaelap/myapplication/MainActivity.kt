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
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.material3.DropdownMenu

val TimesNewRoman = FontFamily(
    Font(R.font.times, FontWeight.Normal),
    Font(R.font.times, FontWeight.Bold),
    Font(R.font.times, FontWeight.Normal, FontStyle.Italic),
    Font(R.font.times, FontWeight.Bold, FontStyle.Italic)
)

data class FactoidData(
    val title: String,
    val description: String
)

data class schoolEvent(
    val title: String,
    val shortDescription: String,
    val longDescription: String,
    val date: String,
    val imageRes: Int
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
    var selectedTabIndex by remember { mutableIntStateOf(3) }
    val tabs = listOf("Trivia", "Acad", "Fame", "Events", "Settings") //list out each tab

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
                3 -> EventScreen(modifier = Modifier
                    .fillMaxSize())
                4 -> SettingsScreen()
            }
        }
    }
}

@Composable
fun SettingsScreen(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Settings Screen",
            style = TextStyle(
                fontFamily = TimesNewRoman,
                fontSize = 24.sp,
                color = Color(0xFF1A2C57)
            )
        )
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
fun EventScreen(modifier: Modifier = Modifier) {
    val eventsList = mutableListOf<schoolEvent>(testEvent1, testEvent2, testEvent3, testEvent4, testEvent5, testEvent6, testEvent7, testEvent8, testEvent9, testEvent10)
    Column(modifier = modifier.fillMaxSize()) {
        TopBanner()
        EventBanner()
        ScrollBanner()

        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .background(color = Color(0xFF1A2C57))
        ) {
            item {
                EventTab(eventsList, 0)
                EventTab(eventsList, 1)
                EventTab(eventsList, 2)
                EventTab(eventsList, 3)
                EventTab(eventsList, 4)
                EventTab(eventsList, 5)
                EventTab(eventsList, 6)
                EventTab(eventsList, 7)
                EventTab(eventsList, 8)
                EventTab(eventsList, 9)
            }
        }
    }
}

@Composable
fun ScrollBanner() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(35.dp)
            .background(Color(0xFF1A2C57)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Take a scroll through this month's events ↓",
            style = TextStyle(
                fontFamily = TimesNewRoman,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
            )
        )
    }
}

@Composable
fun EventTab(events: List<schoolEvent>, slotNumber: Int) {
    var isExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(Color(0xFF1A2C57))
            .border(1.dp, Color(0xFFFBE475))
            .clickable { isExpanded = !isExpanded }
            .padding(12.dp)
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = events[slotNumber].title,
                color = Color(0xAFFFFFFF),
                fontFamily = TimesNewRoman,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .weight(1f),
                maxLines = 2,
                overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
            )
            Text(
                text = events[slotNumber].date,
                color = Color(0xCCFFFFFF),
                fontFamily = TimesNewRoman,
                fontSize = 14.sp
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = events[slotNumber].imageRes),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = events[slotNumber].shortDescription,
                color = Color(0xAFFFFFFF),
                fontFamily = TimesNewRoman,
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                textDecoration = TextDecoration.Underline
            )
        }

        if (isExpanded) {
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = events[slotNumber].longDescription,
                color = Color(0xAFFFFFFF),
                fontFamily = TimesNewRoman,
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
@Composable
fun FameScreen(modifier: Modifier = Modifier) {

    var selectedFactoid by remember { mutableStateOf<FactoidData?>(null) }

    if (selectedFactoid != null) {
        CustomDialog(
            factoid = selectedFactoid!!,
            onDismissRequest = { selectedFactoid = null }
        )
    }

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        TopBanner()
        FameBanner()
        Factoids(onFactoidClick = { selectedFactoid = it })
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
                .background(Color.Black.copy(alpha = 0.5f))
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
fun Factoids(onFactoidClick: (FactoidData) -> Unit) {

    Column {

        Row(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            FactoidBoxes(
                modifier = Modifier.weight(1f),
                text = "National Ranking & Academics",
                backgroundImage = R.drawable.academics,
                onClick = {
                    onFactoidClick(
                        FactoidData(
                            "National Ranking & Academics",
                            "Smith College is ranked #13 in National Liberal Arts Colleges and #10 in Best Value Schools. Of Forbes' America's Top College's ranking which accounts for 500 military academies, national universities, and liberal arts colleges, Smith rests at an impressive 135th place. Academics are taken very seriously at Smith, with an emphasis on research opportunities, experiental learning, and a vast selection of majors. The Ada Comstock Scholars Program also encourages students of nontraditional college ages to complete their unfinished degrees. Smith was also the first historically Women's college to offer an undergraduate degree in Engineering."
                        )
                    )
                }
            )

            FactoidBoxes(
                modifier = Modifier.weight(1f),
                text = "Sustainability",
                backgroundImage = R.drawable.smithpark,
                onClick = {
                    onFactoidClick(
                        FactoidData(
                            "Sustainability",
                            "Smith College excels at sustainability, earning an A- on the \"College Sustainability Card 2010\". As of 2022, Smith is in the process of working on a Geothermal Energy System to reduce carbon emissions and reach carbon neutrality by 2030. Additionally, Smith's Bechtel Environmental Classroom uses compostable toilets, solar panels, and net-zero energy use. Students doing fieldwork in the classroom are working to save the Magnolia Fraseri species, earning Smith the name of \"species champion\" by the U.S department of agriculture."
                        )
                    )
                }
            )

            FactoidBoxes(
                modifier = Modifier.weight(1f),
                text = "Athletics",
                backgroundImage = R.drawable.athletics,
                onClick = {
                    onFactoidClick(
                        FactoidData(
                            "Athletics",
                            "First historically women's college to join the NCAA with it's sports teams being called the \"Smith Bears\". IN fact, women's basketball started with Smith College. Only two years after the invention of basketball, Smith's athletic director adapted the sport for women. Smith has 11 varsity teams and 3 were recently NEWMAC Champions. Smith offers students and teams its own award, the Powerhouse Award, for excellent work."
                        )
                    )
                }
            )
        }

        Row(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            FactoidBoxes(
                modifier = Modifier.weight(1f),
                text = "Sylvia Plath",
                backgroundImage = R.drawable.sylviaplath,
                onClick = {
                    onFactoidClick(
                        FactoidData(
                            "Sylvia Plath",
                            "A renowned poet and novelist who graduated from smith in 1951 with a degree in English. She was on the Editorial Board of the Campus Cat, editor of the Smith Review, and secretary of the Honor Board. She also won two Smith prizes for her poetry. Plath went on to publish an abundance of wildly famous works such as The Bell Jar, The Colossus and Other Poems, and Ariel. In 1982, she won the Posthumus Pulitzer Prize for her work \"The Collected Poems\"."
                        )
                    )
                }
            )

            FactoidBoxes(
                modifier = Modifier.weight(1f),
                text = "Julia Child",
                backgroundImage = R.drawable.juliachild,
                onClick = {
                    onFactoidClick(
                        FactoidData(
                            "Julia Child",
                            "Culinary icon who graduated from Smith in 1934 with a degree in History. While attending, she played basketball, served on the Student Council, and took part in the Dramatics Association. She went on to finish her studies at Le Cordon Bleu in Paris, eventually cofounding L'ecole des trois gourmandes, and experienced massive success and fame from her PBS show The French Chef. In fact, Julia Child Day is celebrated at Smith College with a panel discussion of food, pleasure, and culture."
                        )
                    )
                }
            )

            FactoidBoxes(
                modifier = Modifier.weight(1f),
                text = "Nancy Reagan",
                backgroundImage = R.drawable.nancyreagan,
                onClick = {
                    onFactoidClick(
                        FactoidData(
                            "Nancy Reagan",
                            "Former first lady of the U.S who graduated from Smith with a degree in Drama and English in 1943. Before becoming first lady, she pursued her acting career, appearing in Donovan's Brain, The Next Voice You Hear, and Night Into Morning. As first lady, she launched the \"Just Say No\" campaign to oppose drug use in America's youth, The Foster Grandparent's Program to connect senior citizens to children with disabilities, and actively supported Vietnam War veterans through donations and hospital visitations."
                        )
                    )
                }
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
fun CustomDialog(
    factoid: FactoidData,
    onDismissRequest: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { onDismissRequest() },

        title = {
            Text(text = factoid.title)
        },

        text = {
            Column {
                Text(factoid.description)
                Spacer(modifier = Modifier.height(8.dp))

            }
        },

        confirmButton = {
            Button(onClick = { onDismissRequest() }) {
                Text("Close")
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
fun EventBanner() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(30.dp)
            .background(Color(0xFFFBE475)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Events & Student Life",
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