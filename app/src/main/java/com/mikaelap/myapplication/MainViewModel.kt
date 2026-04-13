package com.mikaelap.myapplication

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.AndroidViewModel

class MainViewModel(application: Application) : AndroidViewModel(application) {

    val allQuestions: LiveData<List<TrivialQuestion>>
    private val repository: TrivialQuestionRepository
    val searchResults: MutableLiveData<List<TrivialQuestion>>

    val allCourses: LiveData<List<Course>>
    private val courseRepository: CourseRepository
    val courseSearchResults: MutableLiveData<List<Course>>

    var isDarkMode by mutableStateOf(true)

    init {
        val questionDb = TrivialQuestionRoomDatabase.getInstance(application)
        val dao = questionDb.TrivialQuestionDao()
        repository = TrivialQuestionRepository(dao)

        allQuestions = repository.allQuestions
        searchResults = repository.searchResults

        val courseDao = questionDb.courseDao()
        courseRepository = CourseRepository(courseDao)
        allCourses = courseRepository.allCourses
        courseSearchResults = courseRepository.searchResults
    }

    fun toggleDarkMode(enabled: Boolean) {
        isDarkMode = enabled
    }

    fun loadQuestions() {
        seedQuestions.forEach { question ->
            repository.insertQuestion(question)
        }
    }

    fun insertQuestion(question: TrivialQuestion) {
        repository.insertQuestion(question)
    }

    fun insertCourse(course: Course) {
        courseRepository.insertCourse(course)
    }

    fun deleteCourse(id: Int) {
        courseRepository.deleteCourse(id)
    }

    fun smartSearch(courseName: String?, creditHour: String?, letterGrade: String?) {
        courseRepository.smartSearch(courseName, creditHour, letterGrade)
    }
}
