package com.example.simon.HighScoreDatabase

import android.support.annotation.WorkerThread
import com.example.simon.HighScoreDatabase.HighScoreDao
import com.example.simon.HighScoreDatabase.HighScoreDataModel

//stole and modified from https://codelabs.developers.google.com/codelabs/android-room-with-a-view-kotlin/#7

/*purpose of repository is isolation of different backend environments
Really isn't necessary for this project because it is mostly used to build a branch where we can cache data locally
to avoid excess network calls to a centralized database, however I'm doing it just because it's good practice
 */
class HighScoreRepository(private val highScoreDAO: HighScoreDao){
    val allScores: List<Int> = highScoreDAO.getAllScores() //bring all data up to RAM
    val highScore: Int = highScoreDAO.getHighScore()

    @WorkerThread //tells the compiler that this cannot be done by the main thread without resulting in application crash
    suspend fun insert(score : Int){ //compiler claims it is redundant if that's the case it should do the optimization
        val s = HighScoreDataModel(score)
        highScoreDAO.insert(s)
    }


}