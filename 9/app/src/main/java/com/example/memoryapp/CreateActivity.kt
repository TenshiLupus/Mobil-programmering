  package com.example.memoryapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.example.memoryapp.models.BoardSize
import com.example.memoryapp.utils.EXTRA_BOARD_SIZE

  class CreateActivity : AppCompatActivity() {

      private lateinit var boardSize : BoardSize
      private var numImagesRequired = -1

      override fun onCreate(savedInstanceState: Bundle?) {
          super.onCreate(savedInstanceState)
          setContentView(R.layout.activity_create)

          boardSize = intent.getSerializableExtra(EXTRA_BOARD_SIZE) as BoardSize
          numImagesRequired = boardSize.getNumPairs()
          supportActionBar?.title = "Choose pic (0 / $numImagesRequired)"
      }

      override fun onOptionsItemSelected(item: MenuItem): Boolean {
          if (item.itemId == android.R.id.home){
              finish()
              return true
          }
          return super.onOptionsItemSelected(item)
      }
  }