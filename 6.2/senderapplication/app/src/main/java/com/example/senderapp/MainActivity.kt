package com.example.senderapp

import android.os.Bundle
import android.telephony.SmsManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    //define uninitialized variables in global scope

    private lateinit var phoneEdt: EditText
    private lateinit var messageEdt: EditText
    private lateinit var sendMsgBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //initialize layout input and button views
        phoneEdt = findViewById(R.id.idEdtPhone)
        messageEdt = findViewById(R.id.idEdtMessage)
        sendMsgBtn = findViewById(R.id.idBtnSendMessage)

        //Set listener on send button
        sendMsgBtn.setOnClickListener {

            //Retrieve input from views and convert them to strings
            val phoneNumber = phoneEdt.text.toString()
            val message = messageEdt.text.toString()

            //try block to catch any eventual errors that may arise
            try {

                //Initialize an sms manager object
                val smsManager: SmsManager = SmsManager.getDefault()

                //insert receiver etails and sent message
                smsManager.sendTextMessage(phoneNumber, null, message, null, null)

                //Dissplay a small screen notification
                Toast.makeText(applicationContext, "Message Sent", Toast.LENGTH_LONG).show()

            } catch (e: Exception) {

                // on failure display error message and request to input necessary information
                Toast.makeText(applicationContext, "Please enter all the data.."+e.message.toString(), Toast.LENGTH_LONG)
                    .show()
            }
        }
    }
}