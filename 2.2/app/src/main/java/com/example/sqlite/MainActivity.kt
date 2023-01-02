package com.example.sqlite

import android.content.ContentValues
import android.database.sqlite.*
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import org.w3c.dom.Text
import java.time.LocalDate

class MainActivity : AppCompatActivity() {

    private lateinit var wdatabase : SQLiteDatabase
    private lateinit var rdatabase : SQLiteDatabase
    private var running : Boolean = false

    private val DBName = "Primes"
    private val tableName = "PrimeNumbers"
    private val tablePrimaryKey = "_id"
    private val tableDate = "dateFound"
    private val tablePrime = "prime"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val sqoh = SqlOpenHelper(this, DBName, tableName, tablePrimaryKey, tableDate, tablePrime)
        wdatabase = sqoh.writableDatabase
        rdatabase = sqoh.readableDatabase

        val primeNumberDisplay : TextView = findViewById(R.id.primeNumber)
        val button : Button = findViewById(R.id.button)

        var lastPrime = retrieveLastPrime()
        primeNumberDisplay.setText(lastPrime.toString())

        button.setOnClickListener {
            var current = findNextPrime(lastPrime)
            primeNumberDisplay.setText(current.toString())

            Toast.makeText(applicationContext, current.toString(), Toast.LENGTH_SHORT)
            lastPrime = current
        }

    }

    fun findNextPrime(initialNumber:Int) : Int{
        var prime : Int = initialNumber
        var found : Boolean = false

        while(!found){
            prime++
            Log.i("mytag", "Current evaluated number: $prime")
            if(isPrime(prime)) {
                Log.i("mytag", "WAS PRIME")
                addToDatabase(prime)
                found = true
                break
            }
        }

        return prime
    }

    private fun isPrime(currentNumber : Int): Boolean {

        for (i in 2..currentNumber / 2) {
            // condition for nonprime number
            if (currentNumber % i == 0) {
                return false
                break
            }
        }
        return true
    }

    //add the current prime as a new record in the database
    private fun addToDatabase(currentPrime : Int){

        var values = ContentValues()

        val time : LocalDate = LocalDate.now()
        values.put(tablePrime, currentPrime)
        values.put(tableDate, time.toString())
        var id : Long = (wdatabase.insert(tableName, null, values))
    }

    //Retrieve the last store prime in the database
    private fun retrieveLastPrime() : Int {
        var result = rdatabase.rawQuery("SELECT * FROM $tableName ORDER BY $tablePrimaryKey DESC", null)


        //If there is an available record
        if(result.moveToNext()){
            var storedNumber = result.getInt(1)
            Toast.makeText(this, storedNumber.toString(), Toast.LENGTH_SHORT)
            result.close()
            return storedNumber
        }
        return 1
    }
}