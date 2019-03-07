package com.example.simon.HighScoreDatabase

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query

//stole from https://codelabs.developers.google.com/codelabs/android-room-with-a-view-kotlin/#4
@Dao
interface HighScoreDao{     //interface to interact with the database

    //gives the user a standard interface function
    @Insert
    fun insert(score : HighScoreDataModel)

    //defines a database query that returns all
    // scores sorted in descending order
    @Query("SELECT * from  high_score_table ORDER BY score DESC")
    fun getAllScores() : List<Int>

    //defines database query that that returns the high score
    @Query("SELECT max(score) from high_score_table")
    fun getHighScore() : Int
}