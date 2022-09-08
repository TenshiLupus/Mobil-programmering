package com.example.memoryapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.memoryapp.models.BoardSize
import com.example.memoryapp.utils.DEFAULT_ICONS

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerViewBoard : RecyclerView
    private lateinit var textViewNumberMoves : TextView
    private lateinit var textViewNumberPairs : TextView

    private var boardSize : BoardSize = BoardSize.EASY

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerViewBoard = findViewById(R.id.RecycleViewBoard)
        textViewNumberMoves = findViewById(R.id.textViewNumberMoves)
        textViewNumberPairs = findViewById(R.id.textViewNumberPairs)

        val chosenImages = DEFAULT_ICONS.shuffled().take(boardSize.getNumPairs())
        val randomizedImages = (chosenImages + chosenImages).shuffled()
        randomizedImages.q
        
        recyclerViewBoard.adapter = MemoryBoardAdapter(this,boardSize, randomizedImages)
        recyclerViewBoard.setHasFixedSize(true)
        recyclerViewBoard.layoutManager = GridLayoutManager(this, boardSize.getWidth())
    }

}