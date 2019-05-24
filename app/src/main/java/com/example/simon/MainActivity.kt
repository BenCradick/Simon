package com.example.simon

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView

//following the android documentation example, i don't know what this does other than drop the
// package name in the global namespace, it's used when initiating next funciton but it doesn't really provide me
// with a reason why they're doing what they're doing
const val EXTRA_MESSAGE = "difficulty"

class MainActivity : AppCompatActivity() {

    lateinit var buttonHard : TextView
    lateinit var buttonEasy : TextView
    lateinit var buttonMedium : TextView
    lateinit var buttonLudacris : TextView



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        buttonHard = findViewById(R.id.hardButton)
        buttonEasy = findViewById(R.id.easyButton)
        buttonMedium = findViewById(R.id.mediumButton)
        buttonLudacris = findViewById(R.id.ludacrisButton)

        buttonHard.setOnClickListener{ sendMessage(buttonHard) }
        buttonEasy.setOnClickListener { sendMessage(buttonEasy) }
        buttonMedium.setOnClickListener { sendMessage(buttonMedium) }
        buttonLudacris.setOnClickListener { sendMessage(buttonLudacris) }





    }
    fun sendMessage(textView: TextView){
        val intent = Intent(this@MainActivity, GameActivity::class.java).apply{
            putExtra(EXTRA_MESSAGE, textView.text.toString())
        }
        startActivity(intent)
    }



}
