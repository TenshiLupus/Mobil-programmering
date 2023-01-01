package com.example.sqlite

import android.content.ContentValues
import android.database.sqlite.*
import android.os.Bundle
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
            Toast.makeText(this, current.toString(), Toast.LENGTH_SHORT)
            lastPrime = current
        }

    }

    fun findNextPrime(initialNumber:Int) : Int{
        var prime : Int = initialNumber
        var found : Boolean = false

        while(!found){
            prime++
            if(isPrime(prime))
                found = true
        }

        return prime
    }

    fun isPrime(currentNumber : Int): Boolean {
        for (num in 2..currentNumber){
            if((2 until num).none{ num % it == 0})
                addToDatabase(num)
        }
        return false
    }

    //add the current prime as a new record in the database
    fun addToDatabase(currentPrime : Int){

        var values = ContentValues()

        val time : LocalDate = LocalDate.now()
        values.put(tablePrime, currentPrime)
        values.put(tableDate, time.toString())
        var id : Long = (wdatabase.insert(tableName, null, values))
    }

    //Retrieve the last store prime in the database
    fun retrieveLastPrime() : Int {
        var result = rdatabase.rawQuery("SELECT * FROM $tableName ORDER BY $tablePrimaryKey DESC", null)


        //If there is an available record
        if(result.moveToNext()){
            var storedNumber = result.getInt(2)
            Toast.makeText(this, storedNumber.toString(), Toast.LENGTH_SHORT)
            result.close()
            return storedNumber
        }
        return 1
    }
}