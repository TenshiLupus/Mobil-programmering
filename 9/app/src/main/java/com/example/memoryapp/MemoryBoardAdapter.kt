package com.example.memoryapp

import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.memoryapp.models.BoardSize
import kotlin.math.min

class MemoryBoardAdapter(
    private val context: Context,
    private val boardSize: BoardSize,
    private val cardImages: List<Int>
) :
    RecyclerView.Adapter<MemoryBoardAdapter.ViewHolder>(){

    companion object {
        private const val MARGIN_SIZE = 10
        private const val TAG = "MemoryBoardAdapter"
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val cardWith = parent.width / boardSize.getWidth() - (2 * MARGIN_SIZE)
        val cardHeight = parent.height / boardSize.getHeight() - (2 * MARGIN_SIZE)
        val cardSideLength = min(cardWith, cardHeight)
        val view = LayoutInflater.from(context).inflate(R.layout.m_card, parent, false)

        val layoutParams = view.findViewById<CardView>(R.id.cardView).layoutParams as ViewGroup.MarginLayoutParams

        layoutParams.width = cardSideLength
        layoutParams.height = cardSideLength
        layoutParams.setMargins(MARGIN_SIZE, MARGIN_SIZE, MARGIN_SIZE, MARGIN_SIZE)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = boardSize.numCards

    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        private val imageButton = itemView.findViewById<ImageButton>(R.id.imageButton)
        fun bind(position : Int){
            imageButton.setImageResource(cardImages[position])
            imageButton.setOnClickListener{
                Log.i(TAG, "Clicked on position ${position}")
            }
        }
    }
}