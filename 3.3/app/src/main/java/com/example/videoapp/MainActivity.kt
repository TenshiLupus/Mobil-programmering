package com.example.videoapp

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.VideoView
import android.provider.MediaStore
import android.view.View
import android.widget.MediaController

private const val REQUEST_VIDEO_CAPTURE = 7

class MainActivity : AppCompatActivity() {

    private lateinit var videoView : VideoView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        videoView = findViewById(R.id.videoView)

        val mediaCollection = MediaController(this)
        mediaCollection.setAnchorView(videoView)
        videoView.setMediaController(mediaCollection)
    }

    fun startVideo(view : View){
        val intent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
        if(intent.resolveActivity(packageManager) != null && checkCameraHardware(this)){
            startActivityForResult(intent, REQUEST_VIDEO_CAPTURE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data : Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)
        if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK) {
            val videoUri: Uri? = data?.data
            videoView.setVideoURI(videoUri)
            videoView.start()
        }
    }

    private fun checkCameraHardware(context: Context): Boolean {
        return context.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA)
    }
}