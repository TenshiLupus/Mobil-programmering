package com.example.texttospeech

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.widget.Button
import java.util.*
import java.util.Random.*

class MainActivity : AppCompatActivity(), TextToSpeech.OnInitListener {

    private lateinit var myTTs : TextToSpeech
    private val phrases : ArrayList<String> = arrayListOf()
    private val STATUS_CODE = 100
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        phrases.add("MIDNIGHT EXIGENT")
        phrases.add("SIDDHARTA GOLEM")

        val startButton : Button = this.findViewById(R.id.playButton)
        startButton.setOnClickListener {
            var checkIntent : Intent = Intent()
            checkIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA)
            startActivityForResult(checkIntent, STATUS_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == STATUS_CODE){
            if(resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
                myTTs = TextToSpeech(this, this)
                myTTs.setLanguage(Locale.US)
            }else run {
                var ttsLoadIntent = Intent()
                ttsLoadIntent.action = TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA
                startActivity(ttsLoadIntent)
            }
        }
    }

    override fun onInit(status: Int) {
        if(status == TextToSpeech.SUCCESS) run {
            var random : Random = Random()
            var n : Int = random.nextInt(phrases.size)
            myTTs.speak(phrases[n], TextToSpeech.QUEUE_FLUSH, null)
        }
        else if(status == TextToSpeech.ERROR){
            myTTs.shutdown()
        }
    }
}