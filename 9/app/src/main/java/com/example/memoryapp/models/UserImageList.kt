package com.example.memoryapp.models

import com.google.firebase.firestore.PropertyName

//defines the list retrieved from Firestore
data class UserImageList (
    @PropertyName("images") val images : List<String>? = null
        )