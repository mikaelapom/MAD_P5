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

    @Query("SELECT * FROM courses WHERE creditHour = :creditHour")
    fun findCourseByCreditHour(creditHour: Int): List<Course>

    @Query("SELECT * FROM courses WHERE letterGrade = :letterGrade")
    fun findCourseByLetterGrade(letterGrade: String): List<Course>

    @Query("DELETE FROM courses WHERE courseId = :id")
    fun deleteCourse(id: Int)

    @Query("SELECT * FROM courses")
    fun getAllCourses(): LiveData<List<Course>>

}