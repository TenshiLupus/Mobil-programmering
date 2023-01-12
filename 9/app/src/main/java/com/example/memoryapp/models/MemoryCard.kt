package com.example.memoryapp.models

//define the structure of a memory card with its properties
data class MemoryCard (
    val identifier : Int,
    val imageUrl: String? = null,
    var isFaceUp: Boolean = false,
    var isMatched : Boolean = false

)