package com.example.simon.HighScoreDatabase

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey


//Defines a table for SQLITE 2 columns one primary key that is autogenerated values to avoid conflict, second is score
@Entity(tableName = "high_score_table")
data class HighScoreDataModel(@PrimaryKey(autoGenerate = true) @ColumnInfo(name = "score") val score : Int){

}