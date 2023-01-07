package com.example.speechtotext

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.RecognizerIntent
import android.widget.Button
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    val RECOGNIZER_RESULT = 123
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val speechButton : Button = this.findViewById(R.id.button)
        speechButton.setOnClickListener {
            var intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speech to text")
            startActivityForResult(intent, RECOGNIZER_RESULT)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if(requestCode == RECOGNIZER_RESULT && resultCode == RESULT_OK){
            var wordList : List<String> = data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)!!
            val speechText = this.findViewById<TextView>(R.id.textView)
            speechText.text = wordList[0]
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}