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
        mEditText?.setText(getFilePrime().toString());
    }

    private fun getFilePrime() : Long{
        val fileName = "primecounter.txt";
        var currentPrimeFile = File(this.getFilesDir(), fileName)
        var currentPrime = currentPrimeFile.readText()

        if (currentPrime == "" || currentPrime == null){
            currentPrimeFile.writeText(3.toString())
            return 3.toLong()
        }
        return currentPrime!!.toLong()
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

    fun findNextPrime(view : View){
        var currentPrime = getFilePrime().toLong()
        while(true) {
            if (isPrime(currentPrime)) {
                this.mEditText?.setText(currentPrime.toString());
                currentPrime += 2
                val fileName = "primecounter.txt";
                var currentPrimeFile = File(this.getFilesDir(), fileName)
                currentPrimeFile.writeText(currentPrime.toString())
                Log.d(TAG, "passed ###")
                break;
            }
            else {
                currentPrime += 2
            }
        }
        Log.d(TAG, "Current prime is ${currentPrime}")
    }

}