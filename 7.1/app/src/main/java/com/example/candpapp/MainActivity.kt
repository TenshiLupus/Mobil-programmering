package com.example.candpapp

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    private var inputText : EditText? = null
    private var copyButton : Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        inputText = findViewById(R.id.editText)
        copyButton = findViewById(R.id.copyButton)

        copyButton?.setOnClickListener{
            val clipboard : ClipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip : ClipData= ClipData.newPlainText("EditText", inputText?.text.toString())
            clipboard.setPrimaryClip(clip)

            Toast.makeText(this, "Text Copied", Toast.LENGTH_SHORT).show()
        }
    }
}