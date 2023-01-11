package com.example.savefile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

import android.widget.Button

import android.widget.TextView
import java.io.File


const val EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE"
class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity";
    private var mEditText: TextView? = null
    private lateinit var button : Button
    private lateinit var currentPrimeFile : File
    private var retrievedPrime : Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mEditText = findViewById(R.id.edit_text);

        retrievedPrime = getFilePrime()
        var fp = retrievedPrime.toString()
        mEditText?.text = fp

        button = findViewById(R.id.button_save)
        button.setOnClickListener {
            findNextPrime()
        }

    }

    private fun getFilePrime() : Long{
        val fileName = "primecounter.txt";
        val path = this.applicationContext.filesDir
        var initial = "3"


        Log.d("po", " OOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO $path")
        currentPrimeFile = File(this.applicationContext.filesDir, fileName)

        if (!currentPrimeFile.exists()){
            currentPrimeFile.writeText(initial)
        }

        var currentPrime = currentPrimeFile.bufferedReader().readLine().toLong()

        return currentPrime
    }

    private fun isPrime(candidate: Long): Boolean{

        var flag : Boolean = false
        var i: Long = 2;
        while(i <= (candidate / 2)) {
            if ((candidate % i) == 0.toLong()) {
                flag = true
                break
            }
            i ++
        }
        if (!flag){
            return true
        }
        return false

    }

    fun findNextPrime(){
        var currentPrime = retrievedPrime
        while(true) {
            if (isPrime(currentPrime)) {
                retrievedPrime += 2
                this.mEditText?.setText(retrievedPrime.toString());
                val fileName = "primecounter.txt";
                var primeFile = File(this.applicationContext.filesDir, fileName)
                primeFile.writeText(retrievedPrime.toString())

                Log.d(TAG, "passed ###")
                break;
            }
            else {
                currentPrime += 2
            }
        }
        Log.d(TAG, "Current prime is ${retrievedPrime}")
    }

}