package com.example.vibrationapp

import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.View
import android.widget.Button

class MainActivity : AppCompatActivity() {

    private lateinit var start : Button

    private lateinit var vibrator : Vibrator



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        start = findViewById(R.id.start_button)

        vibratePhone()
    }

    private fun vibratePhone(){
        vibrator = getSystemService(VIBRATOR_SERVICE) as Vibrator
        start.setOnClickListener{
            vibrator?.let {
                if(Build.VERSION.SDK_INT >= 26){
                    it.vibrate(VibrationEffect.createOneShot(5000, VibrationEffect.DEFAULT_AMPLITUDE))
                }else{
                   @Suppress("DEPRECATION")
                    it.vibrate(1000)
                }
            }
        }
    }
}