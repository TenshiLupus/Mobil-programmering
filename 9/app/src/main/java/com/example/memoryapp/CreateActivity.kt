  package com.example.memoryapp

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.memoryapp.models.BoardSize
import com.example.memoryapp.utils.EXTRA_BOARD_SIZE

  class CreateActivity : AppCompatActivity() {

      private lateinit var recycleViewImagePicker : RecyclerView
      private lateinit var editTextGameName : EditText
      private lateinit var btnSave : Button

      private lateinit var boardSize : BoardSize
      private val chosenImageUris = mutableListOf<Uri>()
      private var numImagesRequired = -1


      override fun onCreate(savedInstanceState: Bundle?) {
          super.onCreate(savedInstanceState)
          setContentView(R.layout.activity_create)

          recycleViewImagePicker.findViewById<RecyclerView>(R.id.recycleViewImagePicker)
          editTextGameName.findViewById<EditText>(R.id.editTextGameName)
          btnSave.findViewById<Button>(R.id.btnSave)

          supportActionBar?.setDisplayHomeAsUpEnabled(true,)
          boardSize = intent.getSerializableExtra(EXTRA_BOARD_SIZE) as BoardSize
          numImagesRequired = boardSize.getNumPairs()
          supportActionBar?.title = "Choose pic (0 / $numImagesRequired)"

          recycleViewImagePicker.adapter = ImagePickerAdapter(this, chosenImageUris, boardSize)
          recycleViewImagePicker.setHasFixedSize(true)
          recycleViewImagePicker.layoutManager = GridLayoutManager(this, boardSize.getWidth())
      }

      override fun onOptionsItemSelected(item: MenuItem): Boolean {
          if (item.itemId == android.R.id.home){
              finish()
              return true
          }
          return super.onOptionsItemSelected(item)
      }
  }