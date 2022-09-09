package com.example.memoryapp.utils

import com.example.memoryapp.models.BoardSize
import com.example.memoryapp.models.MemoryCard
import com.example.memoryapp.utils.DEFAULT_ICONS

class MemoryGame (private val boardSize : BoardSize){

    lateinit var cards: List<MemoryCard>

    var numPairsFound = 0

    private var indexOfSingleSelectedCard : Int? = null

    init {
        val chosenImages = DEFAULT_ICONS.shuffled().take(boardSize.getNumPairs())
        val randomizedImages = (chosenImages + chosenImages).shuffled()
        cards = randomizedImages.map { MemoryCard(it)}
    }


    fun flipCard(position : Int){

        var foundMatch = false
        if(indexOfSingleSelectedCard == null){
            restoreCards()
            indexOfSingleSelectedCard = position
        } else{
            val foundMatch = checkForMatch(indexOfSingleSelectedCard!!, position)
            indexOfSingleSelectedCard = null
        }
        val card = cards[position]
        card.isFaceUp = !card.isFaceUp
    }

    private fun checkForMatch(position1: Int, position2: Int): Boolean {
        if(cards[position1].identifier != cards[position2].identifier) {
            return false
        }
        cards[position1].isMatched = true
        cards[position2].isMatched = true
            numPairsFound++
        return true
    }

    private fun restoreCards() {
        for (card in cards){
            if(!card.isMatched)
            card.isFaceUp = false
        }
    }

    fun haveWonGame() : Boolean{
        return numPairsFound == boardSize.getNumPairs()
    }

    fun isCardFaceUp(position : Int) : Boolean {
        return cards[position].isFaceUp
    }

}