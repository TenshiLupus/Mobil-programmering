package com.example.galleryslider

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.*


class MainActivity : AppCompatActivity() {

    private var REQUEST_CODE_SINGLE_SELECT = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val viewPager = this@MainActivity.findViewById<ViewPager2>(R.id.viewPager)
        val MAX_ITEMS_COUNT = 20

        val pickMultipleMedia = registerForActivityResult(ActivityResultContracts.PickMultipleVisualMedia(MAX_ITEMS_COUNT)) {
            uris ->
            if (uris.isNotEmpty()){

                var adapter = ViewPageAdapter(uris)
                viewPager.adapter = adapter
                viewPager.orientation = ViewPager2.ORIENTATION_VERTICAL
                viewPager.beginFakeDrag()
                viewPager.fakeDragBy(-10f)
                viewPager.endFakeDrag()
                Log.d("Photopicker","Number of items selected: ${uris.size}")
            }
        }

        pickMultipleMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageAndVideo))


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == RESULT_OK){
           when (requestCode){


               REQUEST_CODE_SINGLE_SELECT -> {
                   var mediaCount = 0
                   while (mediaCount < data?.clipData!!.itemCount){
                       val uri = data.clipData!!.getItemAt(mediaCount).uri
                   }
               }
           }
        }

    }
}