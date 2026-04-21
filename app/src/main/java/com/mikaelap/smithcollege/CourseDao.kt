package com.mikaelap.smithcollege

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface CourseDao {

    @Insert
    fun insertCourse(course: Course)

    //for searching
    //this was originally exact matches only so i changed it to be like name instead
    @Query("SELECT * FROM courses WHERE courseName LIKE :name")
    fun findCourseByName(name: String): List<Course>

    //lookup by matching credit hour
    @Query("SELECT * FROM courses WHERE creditHour = :creditHour")
    fun findCourseByCreditHour(creditHour: Int): List<Course>

    //lookup by matching letter grade
    @Query("SELECT * FROM courses WHERE letterGrade = :letterGrade")
    fun findCourseByLetterGrade(letterGrade: String): List<Course>

    //removes course
    @Query("DELETE FROM courses WHERE courseId = :id")
    fun deleteCourse(id: Int)

    //returns all course
    @Query("SELECT * FROM courses")
    fun getAllCourses(): LiveData<List<Course>>

}