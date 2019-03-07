package com.example.simon

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import com.example.simon.HighScoreDatabase.HighScoreRepository
import com.example.simon.HighScoreDatabase.HighScoreRoomDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class GameModel(difficulty: String, application: Application ): AndroidViewModel(application){



    private var parentJob = Job()
    private val coroutineContext : CoroutineContext
    get() = parentJob + Dispatchers.Main
    private val scope = CoroutineScope(coroutineContext)

    lateinit var highScoreRepository : HighScoreRepository

    var incrementBy : Int = assignIncrement(difficulty)

    var score : Int = 0
    var  highScore : Int = 0

    var i : Int = 0 // for counting how many buttons have been pressed

    var sequence : MutableList<colors> = mutableListOf<colors>()
    init{

        initDBConnection()
    }

    private fun insert(score : Int) = scope.launch(Dispatchers.IO){//Done as a coroutine so that we don't crash,
        highScoreRepository.insert(score)
    }
    private fun getHighScore() = scope.launch(Dispatchers.IO){
        highScore = highScoreRepository.highScore
    }
    private fun initDBConnection() = scope.launch(Dispatchers.IO){
        val highScoreDao = HighScoreRoomDatabase.getDatabase(getApplication(), scope).HighScoreDao()
        highScoreRepository = HighScoreRepository(highScoreDao)
        getHighScore()
    }
    private fun assignIncrement(difficulty: String): Int{
        return when(difficulty){
            "easy" ->  1
            "medium" ->  3
            "hard" ->  5
            "LUDACRIS SPEED" ->  10
            else ->  1
        }

    }
    private fun generateSequence(){
        for (i in 1..incrementBy){
            sequence.add(colors.values().toList().shuffled().first())
        }
    }
    fun checkPress(colors: colors) : Boolean{
        return colors == sequence.get(i)
    }

    override fun onCleared() {
        insert(score) // updating the high score database on destruction of model so that we keep track of high scores
        super.onCleared()
        parentJob.cancel()

    }

}