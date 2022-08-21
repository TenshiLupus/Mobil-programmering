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
    private var mEditText: EditText = TODO();

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mEditText = findViewById<EditText>(R.id.edit_text);
        val currentPrime = getFilePrime();
        findNextPrime(currentPrime);
    }

    private fun getFilePrime() : Long{
        val fileName = "primecounter";
        val file = File(this.filesDir, fileName);

        if (file == null){
            return 2.toLong();
        }
        return file.readText().toLong();
    }

    private fun isPrime(candidate: Long): Boolean{
        var sqrt: Long = sqrt(candidate.toDouble()).toLong();
        var i: Long = 3;
        if ((candidate % i ) == 0.toLong()){
            return false;
        }
        return true;
    }

    private fun findNextPrime(current: Long){
        var currentPrime = current;
        while(true){
            if (isPrime(currentPrime)){
                val verifiedPrime = currentPrime.toString();
                counterFile.writeText(verifiedPrime);
                this.mEditText.setText(verifiedPrime);
            }
        }
    }

}