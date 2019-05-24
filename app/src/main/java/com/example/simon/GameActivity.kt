package com.example.simon

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.FragmentManager


import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.*
import kotlinx.android.synthetic.main.game_over_fragment.*


import org.w3c.dom.Text
import java.lang.Thread.sleep


class GameActivity : AppCompatActivity() {
    lateinit var highScore : TextView
    lateinit var score : TextView
    lateinit var gameModel : GameModel

    lateinit var greenButton : Button
    lateinit var redButton: Button
    lateinit var blueButton: Button
    lateinit var yellowButton: Button

    lateinit var buttons : List<Button>

    private lateinit var game : LinearLayout
    private lateinit var frag : RelativeLayout
    private lateinit var scoreboard : LinearLayout

    private val fragmentManager : FragmentManager? = supportFragmentManager




    var ready : Boolean = false

    private var gameOverFragment : GameOverFragment? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        gameModel = GameModel(intent.getStringExtra("difficulty"), application)
        setContentView(R.layout.activity_game)
        highScore = findViewById(R.id.highScoreTag)
        score = findViewById(R.id.currentScoreTag)
        score.text = gameModel.score.toString()
        highScore.text = gameModel.highScore.toString()
        Log.d("in gameactivity", intent.getStringExtra(EXTRA_MESSAGE))

        greenButton = findViewById(R.id.greenButton)
        blueButton = findViewById(R.id.blueButton)
        redButton = findViewById(R.id.redButton)
        yellowButton = findViewById(R.id.yellowButton)

        game = findViewById(R.id.gameHorizontalLayout)
        frag = findViewById(R.id.gameOverFragment)
        scoreboard = findViewById(R.id.scoreBoard)

        buildFragment()


        buttons = listOf(
            redButton,
            blueButton,
            greenButton,
            yellowButton
        )
        for (button in buttons) {
            button.setOnTouchListener { view: View, motionEvent: MotionEvent ->
                onTouch(view, motionEvent)
                true
            }
        }


        Handler().postDelayed({displaySequence()}, 2000)


    }



    private fun onTouch(view: View, motionEvent: MotionEvent) {

        if(view.isClickable) {
            Log.d("onTouch", "Call to onTouch successful")
            //change the boackground color of the view ot simulate light behind opaque plastic
            view.setBackgroundColor(ContextCompat.getColor(this, nextColor(motionEvent.action, view)))
            if (motionEvent.action == MotionEvent.ACTION_UP) {
                Log.d("MotionEvent", "ACTION_UP")
                if (gameModel.checkPress(whichButton(view))) {
                    score.text = gameModel.getScoreString()
                    if (gameModel.highScore < gameModel.score) {
                        highScore.text = gameModel.getHighScoreString()
                    }
                    if (gameModel.checkToExtend()) {
                        displaySequence()
                    }
                } else {
                    Log.d("CheckPress", "Incorrect sequence entered")
                    gameOver()

                }
            }


        }
    }


    private fun displaySequence(){
        disableButtons()
        ready = false
        var i : Long = 0
          for( color in gameModel.sequence){
              Handler().postDelayed({flickColor(color)}, i)
              i += gameModel.patternSpeed
              Handler().postDelayed({flickColor(color + 4)}, i)
              i += gameModel.patternSpeed/2
        }
        Handler().postDelayed({enableButtons()}, i)
        ready = true
    }
    private fun flickColor(color : Int) = when(color){
        0 -> {
            Log.d("Changing Color:", "setting color to simonRedSelected")
            this.redButton.setBackgroundColor(ContextCompat.getColor(this, R.color.colorSimonRedSelected))

        }
        1 -> {
            Log.d("Changing Color:", "setting color to simonBlueSelected")
            this.blueButton.setBackgroundColor(ContextCompat.getColor(this, R.color.colorSimonBlueSelected))

        }
        2 -> {
            Log.d("Changing Color:", "setting color to simonGreenSelected")
            this.greenButton.setBackgroundColor(ContextCompat.getColor(this, R.color.colorSimonGreenSelected))

        }
        3 -> {
            Log.d("Changing Color:", "setting color to simonYellowSelected")
            this.yellowButton.setBackgroundColor(ContextCompat.getColor(this, R.color.colorSimonYellowSelected))

        }
        4 -> {
            Log.d("Changing Color:", "setting color to simonRed")
            this.redButton.setBackgroundColor(ContextCompat.getColor(this, R.color.colorSimonRed))

        }
        5 -> {
            Log.d("Changing Color:", "setting color to simonBlue")
            this.blueButton.setBackgroundColor(ContextCompat.getColor(this, R.color.colorSimonBlue))

        }
        6 -> {
            Log.d("Changing Color:", "setting color to simonGreen")
            this.greenButton.setBackgroundColor(ContextCompat.getColor(this, R.color.colorSimonGreen))

        }
        7 -> {
            Log.d("Changing Color:", "setting color to simonYellow")
            this.yellowButton.setBackgroundColor(ContextCompat.getColor(this, R.color.colorSimonYellow))

        }
        else -> print("Error: Invalid Rand function result")

    }
    private fun nextColor(event: Int, textView: View) = when(textView.id){
        R.id.redButton -> {

            if(MotionEvent.ACTION_DOWN == event) {
                R.color.colorSimonRedSelected
            }else{
                R.color.colorSimonRed
            }
        }
        R.id.blueButton -> {
            if(MotionEvent.ACTION_DOWN == event){
                R.color.colorSimonBlueSelected
            }else{
                R.color.colorSimonBlue
            }
        }
        R.id.yellowButton -> {
            if(MotionEvent.ACTION_DOWN == event){
                R.color.colorSimonYellowSelected
            }else{
                R.color.colorSimonYellow
            }
        }
        R.id.greenButton -> {
            if(MotionEvent.ACTION_DOWN == event){
                R.color.colorSimonGreenSelected
            }else{
                R.color.colorSimonGreen
            }
        }
        else -> textView.solidColor
    }
    private fun whichButton(textView: View) : Int = when(textView.id){
        R.id.redButton ->  0
        R.id.blueButton -> 1
        R.id.greenButton -> 2
        R.id.yellowButton -> 3
        else -> -1
    }
    private fun gameOver(){
        Log.d("gameOver", "Inside GameActivity.gameOver()")
        frag.bringToFront()
    }
    private fun buildFragment(){
        gameOverFragment = supportFragmentManager.findFragmentById(R.id.gameOverFragment) as? GameOverFragment
        if(gameOverFragment == null){
            Log.d("GameActivity.gameOver()","gameOverFragment was null, this is expected")
            gameOverFragment = GameOverFragment()
            fragmentManager?.beginTransaction()
                ?.add(R.id.gameOverFragment, gameOverFragment!!)
                ?.commit()

        }


        gameOverFragment?.listener = object : GameOverFragment.StateListener {
            override fun playAgainButtonPressed() {
                Log.d("playAgainButtonPressed", "inside overridden function in GameActivity Companion object")
                game.bringToFront()
                scoreboard.bringToFront()
                gameModel.restartGame()
                displaySequence()
                score.text = "0"
                highScore.text = gameModel.highScore.toString()
            }

            override fun mainMenuButtonPressed() {
                Log.d("mainMenuButtonPressed", "inside overridden function in GameActivity Companion object")

            }
        }
    }
    private fun disableButtons(){
        for (button in buttons){
            button.isClickable = false
        }
    }
    private fun enableButtons(){
        for(button in buttons){
            button.isClickable = true
        }
    }
}

