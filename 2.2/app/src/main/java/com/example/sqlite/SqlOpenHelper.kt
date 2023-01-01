package com.example.sqlite

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.util.*

class SqlOpenHelper(context : Context, DBName: String, tableName : String, tablePrimaryKey : String, tableDate : String, tablePrime : String) : SQLiteOpenHelper(context, DBName, null, 1) {

    private val DBstring: String
    private val Tname : String
    private val Tdate : String
    private val Tprime : String
    private val Tpk : String

    init {
        DBstring = DBName

        Tname = tableName
        Tprime = tablePrime
        Tdate = tableDate
        Tpk = tablePrimaryKey
    }

    //By this point the database already exists
    override fun onCreate(db: SQLiteDatabase?) {
        createDatabase(db!!)
    }

    fun createDatabase(db : SQLiteDatabase){
        var query =
            "CREATE TABLE $Tname($Tpk INTEGER primary key autoincrement not null, $Tprime INTEGER, $Tdate TEXT);"
        db?.execSQL(query)
    }
    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        TODO("Not yet implemented")
    }
}