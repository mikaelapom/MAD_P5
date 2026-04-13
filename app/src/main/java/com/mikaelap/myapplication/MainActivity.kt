package com.mikaelap.myapplication

import android.R.id
import android.annotation.SuppressLint
import android.app.Application
import android.os.Bundle
import android.widget.Switch
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.text.input.KeyboardType
import androidx.lifecycle.ViewModel
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Switch

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
    var selectedTabIndex by remember { mutableIntStateOf(2) }
    val tabs = listOf("Trivia", "Acad", "Home", "Events", "⚙\uFE0F") //list out each tab
    var isDarkMode by remember { mutableStateOf(true) } //dark mode global

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
                0 -> TriviaScreen(viewModel = viewModel, isDarkMode)
                1 -> AcadScreen(viewModel = viewModel, isDarkMode)
                2 -> FameScreen()
                3 -> EventScreen(isDarkMode = isDarkMode)
                4 -> SettingsScreen(isDarkMode = isDarkMode,
                    onToggle = { isDarkMode = it })
            }
        }
    }
}

//Tab Screens
@Composable
fun SettingsScreen(
    isDarkMode: Boolean,
    onToggle: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val backgroundColor = if (isDarkMode) Color(0xFF1A2C57) else Color.White
    val textColor = if (isDarkMode) Color.White else Color(0xFF1A2C57)

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(backgroundColor)
    ) {
        TopBanner()
        SettingsBanner()

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Dark Mode",
                color = textColor,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Switch(
                checked = isDarkMode,
                onCheckedChange = onToggle
            )
        }

        Text(
            text = "Credits",
            color = textColor,
            fontSize = 24.sp,
            modifier = Modifier.padding(16.dp)
        )
    }
}
@Composable
fun AcadScreen(
    viewModel: MainViewModel, isDarkMode: Boolean,
    modifier: Modifier = Modifier
) {
    val allCourses by viewModel.allCourses.observeAsState(listOf())
    val searchResults by viewModel.courseSearchResults.observeAsState(listOf())

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        TopBanner()
        FameBanner()

        GPAScreen(
            allCourses = allCourses,
            searchResults = searchResults,
            viewModel = viewModel,
            isDarkMode = isDarkMode
        )
    }
}

@Composable
fun GPAScreen(
    allCourses: List<Course>,
    searchResults: List<Course>,
    viewModel: MainViewModel, isDarkMode: Boolean
) {
    val bgColor = if (isDarkMode) Color(0xFF1A2C57) else Color.White
    val textColor = if (isDarkMode) Color.White else Color(0xFF1A2C57)
    var courseName by remember { mutableStateOf("") }
    var courseCreditHour by remember { mutableStateOf("") }
    var letterGrade by remember {
        mutableStateOf("")
    }

    var calculatedGPA by remember {
        mutableDoubleStateOf(-1.0)
    }

    var searching by remember { mutableStateOf(false) }

    var creditHourError by remember { mutableStateOf(false) }

    var letterGradeError by remember { mutableStateOf(false) }

    var courseNameError by remember { mutableStateOf(false) }

    var courseNameErrorMessage by remember {
        mutableStateOf("Please enter a course name")
    }

    val onCourseTextChange = { text : String ->
        courseName = text
        courseNameError = false
    }

    val onCreditHourTextChange = { text: String ->
        if (text.all { it.isDigit() }) {
            courseCreditHour = text
            creditHourError = false
        } else {
            creditHourError = true
        }
    }

    val onLetterGradeTextChange = { text: String ->
        val upper = text.uppercase()

        if (upper.length <= 1 && upper.all { it in "ABCDE" }) {
            letterGrade = upper
            letterGradeError = false
        } else {
            letterGradeError = true
        }
    }

    //use column to create text fields
    Column(
        horizontalAlignment = CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(bgColor)
    ) {
        com.mikaelap.myapplication.CustomTextField(
            title = "Course Name",
            textState = courseName,
            onTextChange = onCourseTextChange,
            keyboardType = KeyboardType.Text,
            isError = courseNameError,
            errorMessage = courseNameErrorMessage
//            Modifier.border(width = 0.5.dp, Color(0xFF1A2C57))
        )

        com.mikaelap.myapplication.CustomTextField(
            title = "Credit Hour",
            textState = courseCreditHour,
            onTextChange = onCreditHourTextChange,
            keyboardType = KeyboardType.Number,
            isError = creditHourError,
            errorMessage = "Please enter a number"
        )


        com.mikaelap.myapplication.CustomTextField(
            title = "Letter Grade",
            textState = letterGrade,
            onTextChange = onLetterGradeTextChange,
            keyboardType = KeyboardType.Text,
            isError = letterGradeError,
            errorMessage = "Enter A, B, C, D, or E"
        )



        //use row to arrange the buttons
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                .fillMaxWidth()
                .padding(0.dp) //was 10.dp
        ) {
            Button(
                onClick = {
                    //field checking
                    courseNameError = courseName.isBlank()
                    if (courseNameError) {
                        courseNameErrorMessage = "Please enter a course name"
                    }
                    creditHourError = courseCreditHour.isBlank() || !courseCreditHour.all { it.isDigit() }
                    letterGradeError = letterGrade.isBlank() || !(letterGrade.length == 1 && letterGrade.all { it in "ABCDE" })

                    //check all possible errors
                    if (!courseNameError && !creditHourError && !letterGradeError) {
                        val duplicate = allCourses.any { it.courseName.equals(courseName, ignoreCase = true) }
                        if (duplicate) {
                            courseNameError = true
                            courseNameErrorMessage = "Course already added"
                        } else {
                            //if no error then insrt
                            viewModel.insertCourse(
                                Course(courseName, courseCreditHour.toInt(), letterGrade)
                            )
                            searching = false
                            courseName = ""
                            courseCreditHour = ""
                            letterGrade = ""
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF97CDEC),
                    contentColor = Color(0xFF1A2C57)
                )
            ) { Text("Add") }

            Button(onClick = {
                searching = true
                viewModel.smartSearch(courseName, courseCreditHour, letterGrade)
            }, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF97CDEC), contentColor = Color(0xFF1A2C57))
            ) {
                Text("Search")
            }


            Button(onClick = {
                searching = false
                val courseToDelete = allCourses.firstOrNull {
                    it.courseName.equals(courseName, ignoreCase = true)
                }

// Only delete if a matching course exists
                courseToDelete?.let {
                    viewModel.deleteCourse(it.id)
                }

            }, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF97CDEC),contentColor = Color(0xFF1A2C57))
            ) {
                Text("Del")
            }
            calculatedGPA = com.mikaelap.myapplication.calculateGPA2(allCourses)

            Button(onClick = {
                searching = false
                courseName = ""
                courseCreditHour = ""
                letterGrade = ""
            }, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF97CDEC),contentColor = Color(0xFF1A2C57))
            ) {
                Text("Clear")
            }

            Button(onClick = {

                val gpa = com.mikaelap.myapplication.calculateGPA2(allCourses)
                calculatedGPA = gpa

            }, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF97CDEC),contentColor = Color(0xFF1A2C57))
            ) {
                Text("GPA")
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center
        ) {
            Text("GPA: %.2f".format(calculatedGPA), color = textColor)
        }

        LazyColumn(
        ) {
            val list = if (searching) searchResults else allCourses

            item {
                com.mikaelap.myapplication.TitleRow(
                    head1 = "ID",
                    head2 = "Course",
                    head3 = "Credit Hour",
                    head4 = "Letter Grade", isDarkMode = isDarkMode
                )
            }

            items(list) { course ->
                com.mikaelap.myapplication.CourseRow(
                    id = course.id,
                    name = course.courseName,
                    creditHour = course.creditHour,
                    letterGrade = course.letterGrade, isDarkMode = isDarkMode
                )
            }
        }
    }
}

@Composable
fun EventScreen(isDarkMode: Boolean,
                modifier: Modifier = Modifier) {
    val backgroundColor = if (isDarkMode) Color(0xFF1A2C57) else Color.White
    val eventsList = mutableListOf<schoolEvent>(testEvent1, testEvent2, testEvent3, testEvent4, testEvent5, testEvent6, testEvent7, testEvent8, testEvent9, testEvent10)
    Column(modifier = modifier.fillMaxSize()) {
        TopBanner()
        EventBanner()
        ScrollBanner()

        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .background(backgroundColor)
        ) {
            item {
                EventTab(eventsList, 0, isDarkMode)
                EventTab(eventsList, 1, isDarkMode)
                EventTab(eventsList, 2,isDarkMode)
                EventTab(eventsList, 3, isDarkMode)
                EventTab(eventsList, 4, isDarkMode)
                EventTab(eventsList, 5, isDarkMode)
                EventTab(eventsList, 6, isDarkMode)
                EventTab(eventsList, 7, isDarkMode)
                EventTab(eventsList, 8, isDarkMode)
                EventTab(eventsList, 9, isDarkMode)
            }
        }
    }
}

@Composable
fun EventTab(events: List<schoolEvent>, slotNumber: Int, isDarkMode: Boolean) {
    var isExpanded by remember { mutableStateOf(false) }
    val bg = if (isDarkMode) Color(0xFF1A2C57) else Color.White
    val text1 = if (isDarkMode) Color.White else Color(0xFF1A2C57)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(bg)
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
                color = text1,
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
                color = text1,
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
                color = text1,
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
                color = text1,
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TriviaScreen(
    viewModel: MainViewModel,
    isDarkMode: Boolean,
    modifier: Modifier = Modifier
) {
    val bgColor = if (isDarkMode) Color(0xFF1A2C57) else Color.White
    val textColor = if (isDarkMode) Color.White else Color(0xFF1A2C57)
    val questionsFromDb by viewModel.allQuestions.observeAsState(emptyList())
    
    var numQuestionsInput by remember { mutableStateOf("") }
    var isStarted by remember { mutableStateOf(false) }
    var activeQuestions by remember { mutableStateOf<List<TrivialQuestion>>(emptyList()) }
    var questionIndex by remember { mutableIntStateOf(0) }
    var showGrade by remember { mutableStateOf(false) }
    var inputError by remember { mutableStateOf(false) }

    val userSelections = remember { mutableStateMapOf<Int, String>() }
    val shuffledAnswersMap = remember { mutableStateMapOf<Int, List<String>>() }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(bgColor)
    ) {
        TopBanner()
        TriviaBanner()

        if (!isStarted) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    onClick = { viewModel.loadQuestions() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF97CDEC),
                        contentColor = Color(0xFF1A2C57)
                    )
                ) {
                    Text("Load")
                }

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = numQuestionsInput,
                    onValueChange = { 
                        if (it.isEmpty() || (it.all { char -> char.isDigit() } && it.toInt() in 1..10)) {
                            numQuestionsInput = it
                            inputError = false
                        } else {
                            inputError = true
                        }
                    },
                    label = { Text("Number of Questions") },
                    isError = inputError,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
                if (inputError) {
                    Text("Enter a number between 1 and 10", color = Color.Red, fontSize = 12.sp)
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        val n = numQuestionsInput.toIntOrNull()
                        if (n != null && n in 1..10 && questionsFromDb.isNotEmpty()) {
                            // Ensure unique questions by taking distinct ones (though shuffled usually handles it)
                            activeQuestions = questionsFromDb.distinctBy { it.questionName }.shuffled().take(n)
                            shuffledAnswersMap.clear()
                            activeQuestions.forEachIndexed { index, q ->
                                shuffledAnswersMap[index] = q.getShuffledAnswers()
                            }
                            isStarted = true
                            questionIndex = 0
                            userSelections.clear()
                            showGrade = false
                        }
                    },
                    enabled = numQuestionsInput.isNotEmpty() && !inputError && questionsFromDb.isNotEmpty(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF97CDEC),
                        contentColor = Color(0xFF1A2C57)
                    )
                ) {
                    Text("Go")
                }
            }
        } else {
            val current = activeQuestions[questionIndex]
            val answers = shuffledAnswersMap[questionIndex] ?: emptyList()

            QuestionBox(current, questionIndex)
            Spacer(modifier = Modifier.height(16.dp))
            
            Column(modifier = Modifier.padding(horizontal = 8.dp)) {
                answers.forEach { answer ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { userSelections[questionIndex] = answer }
                    ) {
                        RadioButton(
                            selected = (userSelections[questionIndex] == answer),
                            onClick = { userSelections[questionIndex] = answer },
                            colors = RadioButtonDefaults.colors(
                                selectedColor = textColor,
                                unselectedColor = Color(0xFF97CDEC)
                            )
                        )
                        Text(
                            text = answer,
                            fontFamily = TimesNewRoman,
                            fontSize = 18.sp,
                            color = textColor,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (showGrade) {
                val correctCount = activeQuestions.filterIndexed { index, q ->
                    userSelections[index] == q.correct
                }.size
                ScoreText("${correctCount}/${activeQuestions.size}")
            }

            Spacer(modifier = Modifier.weight(1f))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    onClick = { showGrade = true },
                    enabled = userSelections.size == activeQuestions.size,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (userSelections.size == activeQuestions.size) Color(0xFF97CDEC) else Color.Gray,
                        contentColor = Color(0xFF1A2C57)
                    ),
                    shape = CircleShape,
                    modifier = Modifier.width(150.dp).height(48.dp)
                ) {
                    Text("Grade", fontSize = 16.sp)
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(
                        onClick = { if (questionIndex > 0) questionIndex-- },
                        enabled = questionIndex > 0,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF97CDEC), contentColor = Color(0xFF1A2C57)),
                        shape = CircleShape, modifier = Modifier.size(68.dp),
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(0.dp)
                    ) { Text("Back", fontSize = 14.sp) }

                    Button(
                        onClick = { 
                            isStarted = false
                            numQuestionsInput = ""
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF97CDEC), contentColor = Color(0xFF1A2C57)),
                        shape = CircleShape, modifier = Modifier.size(68.dp),
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(0.dp)
                    ) { Text("Reset", fontSize = 14.sp) }

                    Button(
                        onClick = { if (questionIndex < activeQuestions.size - 1) questionIndex++ },
                        enabled = questionIndex < activeQuestions.size - 1,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF97CDEC), contentColor = Color(0xFF1A2C57)),
                        shape = CircleShape, modifier = Modifier.size(68.dp),
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(0.dp)
                    ) { Text("Next", fontSize = 14.sp) }
                }
            }
        }
    }
}

@Composable
fun ScoreText(score: String) {
    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        Text(
            text = "Score: $score",
            style = TextStyle(
                fontFamily = TimesNewRoman,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1A2C57)
            )
        )
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

//Banners
@Preview(showBackground = true)
@Composable
fun TriviaBanner() {
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
fun SettingsBanner() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(30.dp)
            .background(Color(0xFFFBE475)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Settings",
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

//Trivia Functions
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

//Fame Functions
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

//GPA Calculator
// GPA calculation functionality
private fun calculateGPA2(allCourses: List<Course>): Double {
    // Dummy data for illustration. Replace with actual data retrieval and calculation logic

    val gradePoints = mapOf("A" to 4.0, "B" to 3.0, "C" to 2.0, "D" to 1.0, "E" to 0.0,"a" to 4.0, "b" to 3.0, "c" to 2.0, "d" to 1.0, "e" to 0.0)
    val totalCreditHours = allCourses.sumOf { it.creditHour }
    val totalPoints = allCourses.sumOf { it.creditHour * (gradePoints[it.letterGrade] ?: 0.0) }

    return if (totalCreditHours > 0) totalPoints / totalCreditHours else 0.0
}

@Composable
fun TitleRow(head1: String, head2: String, head3: String, head4: String, isDarkMode: Boolean) {
    val bgColor = if (isDarkMode) Color(0xFF1A2C57) else Color(0xFFFBE475)
    val textColor = if (isDarkMode) Color.White else Color(0xFF1A2C57)
    Row(
        modifier = Modifier
            .background(bgColor)
            //.border(width = 0.5.dp, Color.Black)
            .fillMaxWidth()
            .padding(5.dp)
    ) {
        val timesNewRomanStyle = TextStyle(fontFamily = com.mikaelap.myapplication.TimesNewRoman,
            fontSize = 18.sp,
            color = textColor
        )

        Text(head1, style = timesNewRomanStyle,
            modifier = Modifier
                .weight(0.1f))
        Text(head2, style = timesNewRomanStyle,
            modifier = Modifier
                .weight(0.2f))
        Text(head3, style = timesNewRomanStyle,
            modifier = Modifier.weight(0.2f))
        Text(head4, style = timesNewRomanStyle,
            modifier = Modifier.weight(0.2f))
    }
}

@Composable
fun CourseRow(id: Int, name: String, creditHour: Int, letterGrade: String, isDarkMode: Boolean) {
    val textColor = if (isDarkMode) Color.White else Color(0xFF1A2C57)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)
    ) {
        Text(id.toString(), color = textColor, modifier = Modifier
            .weight(0.1f))
        Text(name, color = textColor, modifier = Modifier.weight(0.2f))
        Text(creditHour.toString(), color = textColor, modifier = Modifier.weight(0.2f))
        Text(letterGrade, color = textColor, modifier = Modifier.weight(0.2f))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTextField(
    title: String,
    textState: String,
    onTextChange: (String) -> Unit,
    keyboardType: KeyboardType,
    modifier: Modifier = Modifier,
    isError: Boolean = false,
    errorMessage: String? = null
) {
    Column {
        OutlinedTextField(
            value = textState,
            onValueChange = onTextChange,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            singleLine = true,
            label = { Text(title) },
            isError = isError,
            modifier = modifier.padding(10.dp),
            textStyle = TextStyle(
                fontWeight = FontWeight.Bold,
                fontSize = 30.sp
            )
        )

        if (isError && errorMessage != null) {
            Text(
                text = errorMessage,
                color = Color.Red,
                fontSize = 14.sp,
                modifier = Modifier.padding(start = 16.dp)
            )
        }
    }
}



//creates view model
class MainViewModelFactory(val application: Application) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainViewModel(application) as T
    }
}