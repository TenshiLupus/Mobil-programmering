package com.example.mprog

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import java.io.File
import kotlin.math.sqrt


const val EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE"
class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity";


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        primeCounter();
    }

    fun primeCounter(){
        val fileName = "primecounter"
        val initialValue = 1

        var primeCounterFile = File(this.filesDir, fileName);
        Log.d(TAG, "File path ${primeCounterFile.path}");
    }

    /** Called when the user taps the Send button */
    fun sendMessage(view: View) {
        val editText = findViewById<EditText>(R.id.editTextTextPersonName)
        val message = editText.text.toString()
        val intent = Intent(this, DisplayMessageActivity::class.java).apply {
            putExtra(EXTRA_MESSAGE, message)
        }
        startActivity(intent)
    }

    private fun isPrime(candidate: Long): Boolean{
        var sqrt: Long = sqrt(candidate.toDouble()) as Long;
        var i: Long = 3;
        while(i < sqrt){
            if ((candidate % i ) == 0.toLong()) return false;

        }
        return true;
    }

    private fun findNextPrime(current: Long){
        if
    }
}