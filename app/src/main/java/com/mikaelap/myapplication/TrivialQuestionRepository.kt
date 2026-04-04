package com.mikaelap.myapplication


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.*

class TrivialQuestionRepository(private val TrivialQuestionDao: TrivialQuestionDao) {

    val allQuestions: LiveData<List<TrivialQuestion>> = TrivialQuestionDao.getAllQuestions()
    val searchResults = MutableLiveData<List<TrivialQuestion>>()

    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    fun insertQuestion(newquestion: TrivialQuestion) {
        coroutineScope.launch(Dispatchers.IO) {
            TrivialQuestionDao.insertQuestion(newquestion)
        }
    }
    private fun asyncFindByName(name: String): Deferred<List<TrivialQuestion>> =
        coroutineScope.async(Dispatchers.IO) { TrivialQuestionDao.findQuestionByName(name) }

}