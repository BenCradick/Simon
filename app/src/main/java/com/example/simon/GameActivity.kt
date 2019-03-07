package com.example.simon

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import org.w3c.dom.Text

class GameActivity : AppCompatActivity() {
    lateinit var highScore : TextView
    lateinit var score : TextView
    lateinit var gameModel : GameModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        gameModel = GameModel(EXTRA_MESSAGE, application)
        setContentView(R.layout.activity_game)
        highScore = findViewById(R.id.highScoreTag)
        score = findViewById(R.id.currentScoreTag)
        score.text = gameModel.score.toString()
        highScore.text = gameModel.highScore.toString()
        Log.d("in gameactivity",intent.getStringExtra(EXTRA_MESSAGE))
    }

    fun memorySequence(){

    }
}
