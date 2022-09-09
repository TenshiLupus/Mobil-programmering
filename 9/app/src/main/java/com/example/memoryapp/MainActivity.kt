package com.example.memoryapp

import android.animation.ArgbEvaluator
import android.app.GameManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.memoryapp.models.BoardSize
import com.example.memoryapp.models.MemoryCard
import com.example.memoryapp.utils.DEFAULT_ICONS
import com.example.memoryapp.utils.MemoryGame
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {
    companion object {
        private const val TAG = "MainActivity"
    }

    private lateinit var clRoot : ConstraintLayout
    private lateinit var recyclerViewBoard : RecyclerView
    private lateinit var textViewNumberMoves : TextView
    private lateinit var textViewNumberPairs : TextView

    private var boardSize : BoardSize = BoardSize.EASY
    private lateinit var memoryGame: MemoryGame
    private lateinit var adapter : MemoryBoardAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        clRoot = findViewById(R.id.clRoot)
        recyclerViewBoard = findViewById(R.id.RecycleViewBoard)
        textViewNumberMoves = findViewById(R.id.textViewNumberMoves)
        textViewNumberPairs = findViewById(R.id.textViewNumberPairs)

        textViewNumberPairs.setTextColor(ContextCompat.getColor(this, R.color.color_progress_none))
        memoryGame = MemoryGame(boardSize)
        
        adapter = MemoryBoardAdapter(this,boardSize, memoryGame.cards, object : MemoryBoardAdapter.CardClickListener{
            override fun onCardClicked(position : Int){
                updateGameWithFlip(position)
            }
        })

        recyclerViewBoard.adapter = adapter
        recyclerViewBoard.setHasFixedSize(true)
        recyclerViewBoard.layoutManager = GridLayoutManager(this, boardSize.getWidth())
    }

    private fun updateGameWithFlip(position : Int) {

        if(memoryGame.haveWonGame()){
            Snackbar.make(clRoot, "You already won!", Snackbar.LENGTH_LONG).show()
            return
        }
        if(memoryGame.isCardFaceUp(position)){
            Snackbar.make(clRoot, "Invalid move!", Snackbar.LENGTH_LONG).show()
            return
        }

        if(memoryGame.flipCard(position)) {
            Log.i(TAG, "Found a match! Num pairs found: ${memoryGame.numPairsFound}")
            val color = ArgbEvaluator().evaluate(
                memoryGame.numPairsFound.toFloat() / boardSize.getNumPairs(),
                ContextCompat.getColor(this, R.color.color_progress_none),
                ContextCompat.getColor(this, R.color.color_progress_full)
            ) as Int
            textViewNumberPairs.setTextColor(color)
            textViewNumberPairs.text = "Pairs: ${memoryGame.numPairsFound} / ${boardSize.getNumPairs()}"
            if(memoryGame.haveWonGame()){
                Snackbar.make(clRoot, "You won! Congratulations", Snackbar.LENGTH_LONG)
            }
        }
        textViewNumberMoves.text = "Moves: ${memoryGame.getNumMoves()}"
        adapter.notifyDataSetChanged()
    }

}