package com.example.notificationsapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    private lateinit var button : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button = findViewById(R.id.button)
        button.setOnClickListener{
            openDialog()
        }
    }

    fun openDialog(){
        val dialog : ExampleDialog = ExampleDialog()
        dialog.show(supportFragmentManager, "example Dialog")
    }
}