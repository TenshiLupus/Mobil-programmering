  package com.example.memoryapp
  import android.Manifest
  import android.app.Activity
  import android.content.Intent
  import android.content.pm.PackageManager
  import android.graphics.Bitmap
  import android.graphics.ImageDecoder
  import android.net.Uri
  import android.os.Build
  import android.os.Bundle
  import android.provider.MediaStore
  import android.text.Editable
  import android.text.InputFilter
  import android.text.TextWatcher
  import android.util.Log
  import android.view.MenuItem
  import android.view.View
  import android.widget.Button
  import android.widget.EditText
  import android.widget.ProgressBar
  import android.widget.Toast
  import androidx.appcompat.app.AlertDialog
  import androidx.appcompat.app.AppCompatActivity
  import androidx.recyclerview.widget.GridLayoutManager
  import androidx.recyclerview.widget.RecyclerView
  import com.google.firebase.firestore.ktx.firestore
  import com.google.firebase.storage.ktx.storage
import com.example.memoryapp.models.BoardSize
  import com.example.memoryapp.utils.*
  import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.io.ByteArrayOutputStream

  class CreateActivity : AppCompatActivity() {

      companion object {
          private const val TAG = "CreateActivity"
          private const val PICK_PHOTO_CODE = 3
          private const val READ_EXTERNAL_PHOTOS_CODE = 42
          private const val READ_PHOTOS_PERMISSION = android.Manifest.permission.READ_EXTERNAL_STORAGE
        private const val MIN_GAME_NAME_LENGTH = 3
        private const val MAX_GAME_NAME_LENGTH = 14
      }



      private lateinit var recycleViewImagePicker : RecyclerView
      private lateinit var editTextGameName : EditText
      private lateinit var btnSave : Button
      private lateinit var progressionBarUploading : ProgressBar


      private lateinit var imagePickerAdapter : ImagePickerAdapter
      private lateinit var boardSize : BoardSize
      private var chosenImageUris : MutableList<Uri> = mutableListOf()
      private var numImagesRequired = -1
      private val storage = Firebase.storage
      private val db = Firebase.firestore


      override fun onCreate(savedInstanceState: Bundle?) {
          super.onCreate(savedInstanceState)
          setContentView(R.layout.activity_create)

          recycleViewImagePicker.findViewById<RecyclerView>(R.id.recycleViewImagePicker)
          editTextGameName.findViewById<EditText>(R.id.editTextGameName)
          btnSave.findViewById<Button>(R.id.btnSave)
          progressionBarUploading.findViewById<ProgressBar>(R.id.progressBarUploading)

          supportActionBar?.setDisplayHomeAsUpEnabled(true)
          boardSize = intent.getSerializableExtra(EXTRA_BOARD_SIZE) as BoardSize
          numImagesRequired = boardSize.getNumPairs()
          supportActionBar?.title = "Choose pic (0 / $numImagesRequired)"

          btnSave.setOnClickListener{
              saveDataToFirebase()
          }

          editTextGameName.filters = arrayOf(InputFilter.LengthFilter(MAX_GAME_NAME_LENGTH))
          editTextGameName.addTextChangedListener(object : TextWatcher{
              override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                  TODO("Not yet implemented")
              }

              override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                  TODO("Not yet implemented")
              }

              override fun afterTextChanged(s: Editable?) {
                  btnSave.isEnabled = shouldEnableSaveButton()
              }

          })
          imagePickerAdapter = ImagePickerAdapter(this, chosenImageUris, boardSize, object: ImagePickerAdapter.ImageClickListener{
              override fun onPlaceholderClicked(){
                  if(isPermissionGranted(this@CreateActivity, READ_PHOTOS_PERMISSION)){
                      launchIntentForPhotos()
                  } else {
                      requestPermission(this@CreateActivity, READ_PHOTOS_PERMISSION, READ_EXTERNAL_PHOTOS_CODE)
                  }
              }

          })
          recycleViewImagePicker.setHasFixedSize(true)
          recycleViewImagePicker.layoutManager = GridLayoutManager(this, boardSize.getWidth())
      }

      //Will store relevant data from the application state in a firebase cloud storage
      private fun saveDataToFirebase() {
          val customGameName = editTextGameName.text.toString()
          btnSave.isEnabled = false
          Log.i(TAG, "saveDataToFirebase")
          //Ensure we are not overwriting existing data
          db.collection("games").document(customGameName).get().addOnSuccessListener{ document ->
              if (document != null && document.data != null ){
                  AlertDialog.Builder(this)
                      .setTitle("Name taken")
                      .setMessage("A game already exists with the name $customGameName. Please choose another")
                      .setPositiveButton("OK", null)
                      .show()
                  btnSave.isEnabled = true
              }else{
                  handleImageUploading(customGameName)
              }
          }.addOnFailureListener{exception ->
              Log.e(TAG, "Encountered error while saving memory game", exception)
              Toast.makeText(this, "Encountered error while saving memory game", Toast.LENGTH_SHORT).show()
          }

      }

      private fun handleImageUploading(gameName: String){
          progressionBarUploading.visibility = View.VISIBLE
          var didEncounterError = false
          val uploadedImageUrls = mutableListOf<String>()
          for((index, photoUri) in chosenImageUris.withIndex()) {
              val imageByteArray = getImageByteArray(photoUri)
              val filePath = "images/${gameName}/${System.currentTimeMillis()}-${index}.jpg"
              val photoReference = storage.reference.child(filePath)
              photoReference.putBytes(imageByteArray)
                  .continueWithTask{photoUploadTask ->
                      Log.i(TAG, "Uploaded bytes: ${photoUploadTask.result?.bytesTransferred}")
                      photoReference.downloadUrl

                  }.addOnCompleteListener{downloadUrlTask ->
                      if (!downloadUrlTask.isSuccessful) {
                          Log.e(TAG, "Exception with Firebase storage", downloadUrlTask.exception)
                          Toast.makeText(this, "Failed to upload image", Toast.LENGTH_LONG).show()
                          didEncounterError = true
                          return@addOnCompleteListener
                      }
                      if (didEncounterError){
                          progressionBarUploading.visibility = View.GONE
                          return@addOnCompleteListener
                      }
                      val downloadUrl = downloadUrlTask.result.toString()
                      uploadedImageUrls.add(downloadUrl)
                      progressionBarUploading.progress = uploadedImageUrls.size * 100 / chosenImageUris.size
                      Log.i(TAG, "Finished uploading $photoUri, numUploaded")
                      if(uploadedImageUrls.size == chosenImageUris.size){
                          handleAllImagesUploaded(gameName, uploadedImageUrls)
                      }
                  }
          }
      }

      private fun handleAllImagesUploaded(gameName: String, imageUrls: MutableList<String>) {
          //A list of games that everyone have created
          db.collection("games").document( gameName)
              .set(mapOf("images" to imageUrls))
              .addOnCompleteListener{ gameCreationTask ->
                  progressionBarUploading.visibility = View.GONE
                  if(!gameCreationTask.isSuccessful){
                      Log.e(TAG, "exception with game creation", gameCreationTask.exception)
                      Toast.makeText(this, "Failed game creation", Toast.LENGTH_LONG).show()
                      return@addOnCompleteListener
                  }
                  Log.i(TAG, "Sucessfully created game $gameName")
                  AlertDialog.Builder(this)
                      .setTitle("upload complete! Let's play your game $gameName")
                      .setPositiveButton("OK") { _,_ ->
                          val resultData = Intent()
                          resultData.putExtra(EXTRA_GAME_NAME, gameName)
                          setResult(Activity.RESULT_OK, resultData)
                          finish()
                      }.show()
              }
      }

      private fun getImageByteArray(photoUri: Uri): ByteArray {
        val originalBitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val source = ImageDecoder.createSource(contentResolver, photoUri)
            ImageDecoder.decodeBitmap(source)
        } else {
            MediaStore.Images.Media.getBitmap(contentResolver, photoUri)
        }
          Log.i(TAG, "Original width ${originalBitmap.width} and height ${originalBitmap.height}")
          val scaledBitmap = BitmapScaler.scaleToFitHeight(originalBitmap, 250)
          Log.i(TAG, "Scaled width: ${scaledBitmap.width} and height ${scaledBitmap.height}")
          val byteOutputStream = ByteArrayOutputStream()
          scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 60, byteOutputStream)
          return byteOutputStream.toByteArray()
      }

      override fun onRequestPermissionsResult(
          requestCode: Int,
          permissions: Array<out String>,
          grantResults: IntArray
      ) {
          //Request for permission and run intent when permission is granted
          if (requestCode == READ_EXTERNAL_PHOTOS_CODE){
              if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                  launchIntentForPhotos()
              } else {
                 Toast.makeText(this, "In order to create a custom game, you need to provide access to your photos", Toast.LENGTH_LONG).show()
              }
          }
          super.onRequestPermissionsResult(requestCode, permissions, grantResults)
      }

      override fun onOptionsItemSelected(item: MenuItem): Boolean {
          if (item.itemId == android.R.id.home){
              finish()
              return true
          }
          return super.onOptionsItemSelected(item)

      }

      override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
          super.onActivityResult(requestCode, resultCode, data)
          if (requestCode != PICK_PHOTO_CODE || resultCode != Activity.RESULT_OK || data == null ) {
              Log.w(TAG, "Did not get data back from the launched activity, user likely canceled flow")
            return
          }
          val selectedUri = data?.data
          val clipData = data?.clipData
          if (clipData != null){
              Log.i(TAG, "clipData numImages ${clipData.itemCount}: $clipData")
              for (i in 0 until clipData.itemCount){
                  //alternative syntax to for loop [selection at index]
                  val clipItem = clipData.getItemAt(i)
                  if (chosenImageUris.size < numImagesRequired){
                      chosenImageUris.add(clipItem.uri)
                  }
              }
          } else if (selectedUri != null) {
              Log.i(TAG, "data: $selectedUri")
              chosenImageUris.add(selectedUri)
          }
          imagePickerAdapter.notifyDataSetChanged()
          supportActionBar?.title = "Choose pics (${chosenImageUris.size} / $numImagesRequired)"
          btnSave.isEnabled = shouldEnableSaveButton()
      }

      private fun shouldEnableSaveButton(): Boolean {
          if (chosenImageUris.size != numImagesRequired) {
              return false
          }
          if (editTextGameName.text.isBlank() || editTextGameName.text.length < MIN_GAME_NAME_LENGTH){
              return false
          }
          return true
      }

      //launch an intent for selecting photos
      private fun launchIntentForPhotos(){
          val intent = Intent(Intent.ACTION_PICK)
          intent.type = "image/*"
          intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
          startActivityForResult(Intent.createChooser(intent, "Select images"), PICK_PHOTO_CODE)
      }
  }