package com.example.mprog

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.FileProvider
import java.io.File
import java.io.IOException
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.*

private const val REQUEST_CODE = 7

const val EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE"
class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity";
    private val button = findViewById<Button>(R.id.btnTakePicture)
    private val imageContainer = findViewById<ImageView>(R.id.imageView)
    lateinit var currentPhotoPath: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button.setOnClickListener {
            TakeImage()
        }

    }

    @Throws(IOException::class)
    private fun createImageFile(): File{
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("").format(Date())
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)

        return File.createTempFile(
            "JPEG_${timeStamp}_",
            ".jpg",
            storageDir
        ).apply {
            currentPhotoPath = absolutePath
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?){

        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK){
            val imageBitMap = data?.extras?.get("data") as Bitmap
            imageContainer.setImageBitmap(imageBitMap)
        }
        else{
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun TakeImage(){
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also {
            takePictureIntent ->

            takePictureIntent.resolveActivity(packageManager)?.also {
                val photoFile: File? = try {
                    createImageFile()
                }catch (ex : IOException){
                    null
                }

                photoFile?.also{
                    val photoURI: Uri = FileProvider.getUriForFile(
                        this,
                        "com.example.android.fileprovider",
                        it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, REQUEST_CODE)
                }
            }
        }
    }




}