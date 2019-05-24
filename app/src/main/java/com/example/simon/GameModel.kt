package com.example.simon

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import com.example.simon.HighScoreDatabase.HighScoreRepository
import com.example.simon.HighScoreDatabase.HighScoreRoomDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.lang.Math.abs
import kotlin.coroutines.CoroutineContext
import kotlin.random.Random

class GameModel(difficulty: String, application: Application ): AndroidViewModel(application) {



    private var parentJob = Job()
    private val coroutineContext : CoroutineContext
    get() = parentJob + Dispatchers.Main
    private val scope = CoroutineScope(coroutineContext)

    lateinit var highScoreRepository : HighScoreRepository

    var incrementBy : Int = assignIncrement(difficulty)
    var patternSpeed : Long = assignSpeed(difficulty)

    var score : Int = 0
    var  highScore : Int = 0
    lateinit var scoreList : List<Int>


    var i : Int = 0 // for counting how many buttons have been pressed

    var sequence : MutableList<Int> = mutableListOf()


    init{
        initDBConnection()
        generateSequence()
    }

    private fun insert(score : Int) = scope.launch(Dispatchers.IO){//Done as a coroutine so that we don't crash,

        highScoreRepository.insert(score)
    }
    private fun getHighScore() = scope.launch(Dispatchers.IO){
        highScore = highScoreRepository.highScore
        scoreList = highScoreRepository.allScores
    }
    private fun initDBConnection() = scope.launch(Dispatchers.IO){
        val highScoreDao = HighScoreRoomDatabase.getDatabase(getApplication(), scope).HighScoreDao()
        highScoreRepository = HighScoreRepository(highScoreDao)
        getHighScore()
    }
    private fun assignIncrement(difficulty: String): Int{
        return when(difficulty){
            "Easy" ->  1
            "Medium" ->  3
            "Hard" ->  5
            "LUDACRIS SPEED" ->  10
            else ->  1
        }

    }
    private fun assignSpeed(difficulty: String): Long{
        return when(difficulty){
            "Easy" ->  1000
            "Medium" ->  500
            "Hard" ->  250
            "LUDACRIS SPEED" ->  10
            else ->  1
        }

    }
    private fun generateSequence(){
        for (i in 0..incrementBy){
            sequence.add(abs(Random.nextInt() % 4))
        }
    }
    fun checkPress(colors: Int) : Boolean{
        if(colors == sequence.elementAt(i)){
            i++
            if(i >= score){
                score = i
            }
            return true
        }
        return false
    }
    fun checkToExtend(): Boolean{
        if(i == sequence.size){
            generateSequence()
            i = 0
            return true
        }
        return false
    }
    fun restartGame(){
        if(!scoreList.contains(score)) {
            insert(score)
        }
        sequence.clear()
        score = 0
        getHighScore()
        i = 0
        generateSequence()
    }
    fun getScoreString():String{
        return score.toString()
    }
    fun getHighScoreString() : String{
        return highScore.toString()
    }

    override fun onCleared() {
        insert(score) // updating the high score database on destruction of model so that we keep track of high scores
        super.onCleared()
        parentJob.cancel()

    }





}
