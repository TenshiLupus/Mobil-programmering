package com.example.mprog

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import java.io.File
import java.io.FileWriter
import kotlin.math.sqrt


const val EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE"
class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity";
    private var mEditText: EditText? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mEditText = findViewById<EditText>(R.id.edit_text);
    }

    private fun getFilePrime() : Long{
        val fileName = "primecounter.txt";
        var currentPrimeFile = File(this.filesDir, fileName)
        var primeFileWriter = FileWriter(currentPrimeFile)
        var currentPrime = currentPrimeFile.readText()

        if (currentPrime == ""){
            primeFileWriter?.write(3.toString())
            primeFileWriter.close()
            return 3.toLong()
        }
        return currentPrime!!.toLong()
    }

    private fun isPrime(candidate: Long): Boolean{
        var sqrt: Long = sqrt(candidate.toDouble()).toLong();
        var i: Long = 3;
        while(i < sqrt) {
            if ((candidate % i) == 0.toLong()) {
                return false;
            }
            i += 2
        }

        return true;
    }

    fun findNextPrime(view : View){
        var currentPrime = getFilePrime().toLong()
        if(isPrime(currentPrime)){
            this.mEditText?.setText(currentPrime.toString());
            currentPrime += 2

            val fileName = "primecounter.txt";
            var currentPrimeFile = File(this.filesDir, fileName)
            var primeFileWriter = FileWriter(currentPrimeFile)

            primeFileWriter?.write(currentPrime.toString())
            primeFileWriter?.close()
        }

    }

}