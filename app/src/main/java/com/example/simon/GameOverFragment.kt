package com.example.simon


import android.os.Bundle
import android.os.PersistableBundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import kotlinx.android.synthetic.main.game_over_fragment.*
import kotlinx.android.synthetic.main.game_over_fragment.view.*
import org.w3c.dom.Text


class GameOverFragment : Fragment() {




    // I borrowed heavily from flashy in this project

    //allows GameActivity to react to clicks on the two fragment buttons
    interface StateListener {
        fun playAgainButtonPressed()
        fun mainMenuButtonPressed()

    }

    var listener: StateListener? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        // the actual view that will get inserted into activity_game
        val view = inflater.inflate(R.layout.game_over_fragment, container, false)

        //set listener that will call implementations of the interface
        view.playAgainButton.setOnClickListener {
            listener?.playAgainButtonPressed()
        }
        //ditto ^
        view.mainMenuButton.setOnClickListener {
            listener?.mainMenuButtonPressed()
        }



        return view

    }
}

