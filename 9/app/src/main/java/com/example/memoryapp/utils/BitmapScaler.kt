package com.example.memoryapp.utils

import android.graphics.Bitmap

//Helper functions to scale the bitmap to fit the aspect ratio of a memory card container
object BitmapScaler {

    fun scaleToFitWidth(b: Bitmap, width : Int) : Bitmap {
        val factor = width / b.width.toFloat()
        return Bitmap.createScaledBitmap(b, width, (b.width * factor).toInt(), true)
    }

    fun scaleToFitHeight(b : Bitmap, height : Int) : Bitmap {
        val factor = height / b.height.toFloat()
        return Bitmap.createScaledBitmap(b, (b.width * factor).toInt(), height, true)
    }

}
