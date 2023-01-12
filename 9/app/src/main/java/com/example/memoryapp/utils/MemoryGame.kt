package com.example.memoryapp.utils

import com.example.memoryapp.models.BoardSize
import com.example.memoryapp.models.MemoryCard
import com.example.memoryapp.utils.DEFAULT_ICONS

class MemoryGame(

    //Global variables
    private val boardSize: BoardSize,
    private val customImages: List<String>?){

    val cards: List<MemoryCard>
    var numPairsFound = 0

    private var numCardFlips = 0
    private var indexOfSingleSelectedCard : Int? = null

    init {
        //whether this is triggered will depend if the user previously selected images to play with otherwise the default cards will be used
        if ( customImages == null) {
            val chosenImages = DEFAULT_ICONS.shuffled().take(boardSize.getNumPairs())
            val randomizedImages = (chosenImages + chosenImages).shuffled()
            cards = randomizedImages.map { MemoryCard(it) }
        } else{
            val randomizedImages = (customImages + customImages).shuffled()
            cards = randomizedImages.map { MemoryCard(it.hashCode(), it)}
        }
    }

    fun flipCard(position : Int) : Boolean {
        numCardFlips++
        val card = cards[position]
        var foundMatch = false

        //for each case on which the card can be in, give the proper response
        if(indexOfSingleSelectedCard == null){
            restoreCards()
            indexOfSingleSelectedCard = position
        } else{
            foundMatch = checkForMatch(indexOfSingleSelectedCard!!, position)
            indexOfSingleSelectedCard = null
        }
        card.isFaceUp = !card.isFaceUp
        return foundMatch
    }

    //Asserts wehther the current selected card is the same as the card that it is matched against
    private fun checkForMatch(position1: Int, position2: Int): Boolean {
        if(cards[position1].identifier != cards[position2].identifier) {
            return false
        }
        cards[position1].isMatched = true
        cards[position2].isMatched = true
        numPairsFound++
        return true
    }

    //When the cards are not matched. Flips them face down
    private fun restoreCards() {
        for (card in cards){
            if(!card.isMatched)
            card.isFaceUp = false
        }
    }

    //If the number of matched pairs is the same as the set limit on the boards size, returs true
    fun haveWonGame() : Boolean{
        return numPairsFound == boardSize.getNumPairs()
    }

    //Checks whehther the given card is face up
    fun isCardFaceUp(position : Int) : Boolean {
        return cards[position].isFaceUp
    }

    //Registers teh number of moves on the game session
    fun getNumMoves() : Int {
        return numCardFlips / 2
    }
}