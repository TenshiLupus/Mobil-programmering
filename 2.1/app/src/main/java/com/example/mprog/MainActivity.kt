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
    private val counterFile: File = primeCounterFile();
    private lateinit var mEditText: EditText;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        val readPrime = Integer.parseInt(counterFile.readText()).toLong();
        mEditText = findViewById<EditText>(R.id.edit_text);

    }

    fun primeCounterFile() : File{
        val fileName = "primecounter";
        return File(this.filesDir, fileName);

    }

    /** Called when the user taps the Send button */
    fun sendMessage(view: View) {

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

        if (isPrime(current)){
            counterFile.writeText(current.toString());
        }
    }
}