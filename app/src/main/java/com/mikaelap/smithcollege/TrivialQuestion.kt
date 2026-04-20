package com.mikaelap.smithcollege

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

//used as db table for each course
@Entity(tableName = "questions")
class TrivialQuestion {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "questionId")
    var id: Int = 0

    @ColumnInfo(name = "questionName")
    var questionName: String = ""

    @ColumnInfo(name = "wrong1")
    var wrong1: String = ""

    @ColumnInfo(name = "wrong2")
    var wrong2: String = ""

    @ColumnInfo(name = "wrong3")
    var wrong3: String = ""

    @ColumnInfo(name = "correct")
    var correct: String = ""

    fun getShuffledAnswers(): List<String> {
        return listOf(wrong1, wrong2, wrong3, correct).shuffled()
    }

    constructor() {}
    constructor(questionName: String, wrong1: String, wrong2: String, wrong3: String, correct: String) {
        this.questionName = questionName
        this.wrong1 = wrong1
        this.wrong2 = wrong2
        this.wrong3 = wrong3
        this.correct = correct
    }
}