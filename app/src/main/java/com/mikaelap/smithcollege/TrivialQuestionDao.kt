package com.mikaelap.smithcollege

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface TrivialQuestionDao {

    //to do: add error message if they are missing an input
    @Insert
    fun insertQuestion(question: TrivialQuestion)

    //for searching
    //this was originally exact matches only so i changed it to be like name instead
    @Query("SELECT * FROM questions WHERE questionName LIKE :name")
    fun findQuestionByName(name: String): List<TrivialQuestion>

    @Query("SELECT * FROM questions")
    fun getAllQuestions(): LiveData<List<TrivialQuestion>>


}