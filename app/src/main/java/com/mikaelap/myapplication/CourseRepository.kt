package com.mikaelap.myapplication

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.*

class CourseRepository(private val courseDao: CourseDao) {

    val allCourses: LiveData<List<Course>> = courseDao.getAllCourses()
    val searchResults = MutableLiveData<List<Course>>()

    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    fun insertCourse(newcourse: Course) {
        coroutineScope.launch(Dispatchers.IO) {
            courseDao.insertCourse(newcourse)
        }
    }

    fun deleteCourse(id: Int) {
        coroutineScope.launch(Dispatchers.IO) {
            courseDao.deleteCourse(id)
        }
    }

    fun smartSearch(courseName: String?, creditHour: String?, letterGrade: String?) {
        coroutineScope.launch(Dispatchers.Main) {
            val results = when {
                !courseName.isNullOrBlank() -> asyncFindByName("%$courseName%").await()
                !creditHour.isNullOrBlank() -> asyncFindByCreditHour(creditHour.toInt()).await()
                !letterGrade.isNullOrBlank() -> asyncFindByLetterGrade(letterGrade.uppercase()).await()
                else -> emptyList()
            }
            searchResults.value = results
        }
    }

    private fun asyncFindByName(name: String): Deferred<List<Course>> =
        coroutineScope.async(Dispatchers.IO) { courseDao.findCourseByName(name) }

    private fun asyncFindByCreditHour(creditHour: Int): Deferred<List<Course>> =
        coroutineScope.async(Dispatchers.IO) { courseDao.findCourseByCreditHour(creditHour) }

    private fun asyncFindByLetterGrade(letterGrade: String): Deferred<List<Course>> =
        coroutineScope.async(Dispatchers.IO) { courseDao.findCourseByLetterGrade(letterGrade) } }
