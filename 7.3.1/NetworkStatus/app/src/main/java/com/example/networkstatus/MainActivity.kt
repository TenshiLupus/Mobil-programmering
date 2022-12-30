package com.example.networkstatus

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    private lateinit var messageButton : Button
    private lateinit var cm : ConnectivityManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        messageButton = findViewById(R.id.button)
        cm = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager


        messageButton.setOnClickListener {
            val s : String = "This phone is connected to a network - STATUS: "

            var status = ""

            var ni = cm.activeNetworkInfo
            if (ni != null){
                status = ni.isConnected.toString()
            }else{
                status = false.toString()
            }

            var toast = Toast.makeText(applicationContext, (s + status) , Toast.LENGTH_SHORT)
            toast.show() }
    }
}