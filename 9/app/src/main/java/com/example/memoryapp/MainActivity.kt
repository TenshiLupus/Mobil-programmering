package com.example.memoryapp

import android.animation.ArgbEvaluator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.memoryapp.models.BoardSize
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


    }

    private fun setupBoard(){
        when(boardSize){
            BoardSize.EASY -> {
                textViewNumberMoves.text = "Easy: 4 x 2"
                textViewNumberPairs.text = "Pairs: 0 / 4"
            }
            BoardSize.MEDIUM -> {
                textViewNumberMoves.text = "Medium: 6 x 3"
                textViewNumberPairs.text = "Pairs: 0 / 9"
            }
            BoardSize.HARD -> {
                textViewNumberMoves.text = "Hard: 6 x 4"
                textViewNumberPairs.text = "Pairs : 0 / 12"
            }
        }

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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.mi_refresh -> {
                if (memoryGame.getNumMoves() > 0 && !memoryGame.haveWonGame()){
                    showAlertDialog("Quit your current game?", null, View.OnClickListener {
                        setupBoard()
                    })
                }
                setupBoard()
            }
            R.id.mi_new_size -> {
                showNewSizeDialog()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showNewSizeDialog() {
        val boardSizeView = LayoutInflater.from(this).inflate(R.layout.dialog_board_size, null)
        val radioGroupSize = boardSizeView.findViewById<RadioGroup>(R.id.radioGroup)

        when (boardSize) {
            BoardSize.EASY -> radioGroupSize.check(R.id.rbEasy)
            BoardSize.MEDIUM -> radioGroupSize.check(R.id.rbMedium)
            BoardSize.HARD -> radioGroupSize.check(R.id.rbHard)
        }
        showAlertDialog("Choose new size", boardSizeView, View.OnClickListener {
            boardSize = when (radioGroupSize.checkedRadioButtonId){
                R.id.rbEasy -> BoardSize.EASY
                R.id.rbMedium -> BoardSize.MEDIUM
                else -> BoardSize.HARD
            }
            setupBoard()
        })
    }

    private fun showAlertDialog(title : String, view : View?, positiveClickListener : View.OnClickListener) {
        AlertDialog.Builder(this)
            .setTitle(title)
            .setView(view)
            .setNegativeButton("Cancel", null)
            .setPositiveButton("OK"){_,_ ->
                positiveClickListener.onClick(null)
            }.show()
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