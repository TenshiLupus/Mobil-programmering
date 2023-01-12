package com.example.memoryapp.models

//Define ENUM Constants and helper functions
enum class BoardSize (val numCards : Int) {
    EASY(8),
    MEDIUM(18),
    HARD(24);

    //Initiate the bordsize base on the given difficulty, else default to easy, being the first
    companion object {
        fun getByValue(value : Int) = values().first {
            it.numCards == value
        }
    }

    //Return the width of the assigned size
    fun getWidth() : Int{
        return when (this){
           EASY -> 2
           MEDIUM -> 3
           HARD -> 4
        }
    }

    //return the height of board
    fun getHeight() : Int{
        return numCards / getWidth()
    }

    //Get the number of existing pairs
    fun getNumPairs(): Int {
        return numCards / 2
    }
}