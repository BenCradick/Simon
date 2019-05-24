package com.example.simon.HighScoreDatabase

import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

//Yoinked and modified from https://codelabs.developers.google.com/codelabs/android-room-with-a-view-kotlin/#6

//abstract class for construction of the database
@Database(entities = [HighScoreDataModel::class], version = 1)
abstract class HighScoreRoomDatabase : RoomDatabase(){
    abstract fun HighScoreDao() : HighScoreDao

    //serves to initialize the abstract class
    companion object {
        //reference to self essentially
        @Volatile
        private var INSTANCE: HighScoreRoomDatabase? = null

        //returns the newly initialized database
        fun getDatabase(context: Context,
                        scope: CoroutineScope
        ): HighScoreRoomDatabase {
            return INSTANCE ?: synchronized(this){ //closure that initializes database upon Instance being null
                val instance = Room.databaseBuilder(
                    context.applicationContext, //environment within the database lives
                    HighScoreRoomDatabase::class.java, //defines the class to be built from this abstract class
                    "high_score_database" // name if your IDE didn't clue you in
                ).addCallback(
                    HighScoreDataBaseCallback(
                        scope
                    )
                ).build() //populates database if it is empty
                INSTANCE = instance
                return instance
            }
        }
        private class HighScoreDataBaseCallback(
            private val scope : CoroutineScope
        ) : RoomDatabase.Callback(){

            override fun onOpen(db: SupportSQLiteDatabase) {
                super.onOpen(db)
                INSTANCE?.let{ highScoreRoomDatabase ->
                    scope.launch(Dispatchers.IO){
                        populateDatabase(
                            highScoreRoomDatabase.HighScoreDao()
                        )
                    }
                }
            }

        }
        fun populateDatabase(highScoreDao : HighScoreDao){
            if(highScoreDao.getAllScores().isEmpty()){
                val s = HighScoreDataModel(0)
                highScoreDao.insert(s)
            }
        }
    }

}