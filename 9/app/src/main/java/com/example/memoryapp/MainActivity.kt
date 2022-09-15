package com.example.memoryapp

import android.animation.ArgbEvaluator
import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.memoryapp.models.BoardSize
import com.example.memoryapp.models.UserImageList
import com.example.memoryapp.utils.EXTRA_BOARD_SIZE
import com.example.memoryapp.utils.EXTRA_GAME_NAME
import com.example.memoryapp.utils.MemoryGame
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso

class MainActivity : AppCompatActivity() {
    companion object {
        private const val TAG = "MainActivity"
        private const val CREATE_REQUEST_CODE = 7
    }

    private lateinit var clRoot : CoordinatorLayout
    private lateinit var recyclerViewBoard : RecyclerView
    private lateinit var textViewNumberPairs : TextView
    private lateinit var textViewNumberMoves : TextView

    private val db = Firebase.firestore
    private var gameName : String? = null
    private var customGameImages : List<String>? = null
    private var boardSize : BoardSize = BoardSize.EASY
    private lateinit var memoryGame: MemoryGame
    private lateinit var adapter : MemoryBoardAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        clRoot = findViewById(R.id.clRoot)
        recyclerViewBoard = findViewById(R.id.RecycleViewBoard)
        textViewNumberMoves = findViewById(R.id.textViewNumberMoves)
        textViewNumberPairs = findViewById(R.id.textViewNumberPairs)

        val intent = Intent(this, CreateActivity::class.java)
        intent.putExtra(EXTRA_BOARD_SIZE, BoardSize.EASY)
        startActivity(intent)

        setupBoard()
    }

    private fun setupBoard(){
        //Incase the game name is null set it equal to the name of the app
        supportActionBar?.title = gameName ?: getString(R.string.app_name)
        when(boardSize){
            BoardSize.EASY -> {
                textViewNumberMoves.text = "Easy: 4 x 2"
                textViewNumberPairs.text = "Pairs: 0 / 4"
            }
            BoardSize.MEDIUM -> {
                textViewNumberMoves.text = "Medium: 6 x 3"
                textViewNumberPairs.text = "Pairs: 0 / 9"
            }
            BoardSize.HARD -> {
                textViewNumberMoves.text = "Hard: 6 x 4"
                textViewNumberPairs.text = "Pairs : 0 / 12"
            }
        }

        textViewNumberPairs.setTextColor(ContextCompat.getColor(this, R.color.color_progress_none))
        memoryGame = MemoryGame(boardSize, customGameImages)
        adapter = MemoryBoardAdapter(this,boardSize, memoryGame.cards, object : MemoryBoardAdapter.CardClickListener{
            override fun onCardClicked(position : Int){
                updateGameWithFlip(position)
            }
        })

        recyclerViewBoard.adapter = adapter
        recyclerViewBoard.setHasFixedSize(true)
        recyclerViewBoard.layoutManager = GridLayoutManager(this, boardSize.getWidth())
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.mi_refresh -> {
                if (memoryGame.getNumMoves() > 0 && !memoryGame.haveWonGame()){
                    showAlertDialog("Quit your current game?", null, View.OnClickListener {
                        setupBoard()
                    })
                } else {
                    setupBoard()
                }
                return true
            }
            R.id.mi_new_size -> {
                showNewSizeDialog()
                return true
            }
            R.id.mi_custom -> {
                showCreationDialog()
                return true
            }
            R.id.mi_download -> {
                showDownloadDialog()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showDownloadDialog() {
        val boardDownloadView = LayoutInflater.from(this).inflate(R.layout.dialog_download_board, null)
        showAlertDialog("Fetch memory game", boardDownloadView, View.OnClickListener {
            //grab the text of the target game to download
            val editTextDownloadGame = boardDownloadView.findViewById<EditText>(R.id.editTextDownloadGame)
            val gameToDownload = editTextDownloadGame.text.toString().trim()
            downloadGame(gameToDownload)
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == CREATE_REQUEST_CODE && resultCode == Activity.RESULT_OK){
            val customGameName = data?.getStringExtra(EXTRA_GAME_NAME)
            if( customGameName ==  null ){
                Log.e(TAG, "Got nul custom game from CreateActivity")
                return
            }
            downloadGame(customGameName)
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun downloadGame(customGameName : String){
        db.collection("games").document(customGameName).get().addOnSuccessListener{ document ->
            val userImageList = document.toObject(UserImageList::class.java)
            if (userImageList?.images == null) {
                Log.e(TAG, "Invalid custom game data from Firebase")
                Snackbar.make(clRoot, "Sorry, we couldn't find any such game, '$customGameName'", Snackbar.LENGTH_LONG).show()
                return@addOnSuccessListener
            }
            val numCards = userImageList.images.size * 2
            //retrieve the size of the board by utilizing the number of cards
            boardSize = BoardSize.getByValue(numCards)
            customGameImages = userImageList.images
            gameName = customGameName
            //pre fetch images for inmediate presentation
            for (imageUrl in userImageList.images){
                Picasso.get().load(imageUrl).fetch()
            }
            Snackbar.make(clRoot, "You're now playing '$customGameName'!", Snackbar.LENGTH_LONG).show()
            setupBoard()

        }.addOnFailureListener{ exception ->
            Log.e(TAG, "Excepton when retrieving game", exception)
        }
    }

    private fun showCreationDialog() {
        val boardSizeView = LayoutInflater.from(this).inflate(R.layout.dialog_board_size, null)
        val radioGroupSize = boardSizeView.findViewById<RadioGroup>(R.id.radioGroupSize)
        showAlertDialog("Create your own memory board", boardSizeView, View.OnClickListener {
            val desiredBoardSize = when (radioGroupSize.checkedRadioButtonId){
                R.id.rbEasy -> BoardSize.EASY
                R.id.rbMedium -> BoardSize.MEDIUM
                else -> BoardSize.HARD
            }
            val intent = Intent(this, CreateActivity::class.java)
            intent.putExtra(EXTRA_BOARD_SIZE, desiredBoardSize)
            startActivityForResult(intent, CREATE_REQUEST_CODE)
        })
    }

    private fun showNewSizeDialog() {
        val boardSizeView = LayoutInflater.from(this).inflate(R.layout.dialog_board_size, null)
        val radioGroupSize = boardSizeView.findViewById<RadioGroup>(R.id.radioGroupSize)

        when (boardSize) {
            BoardSize.EASY -> radioGroupSize.check(R.id.rbEasy)
            BoardSize.MEDIUM -> radioGroupSize.check(R.id.rbMedium)
            BoardSize.HARD -> radioGroupSize.check(R.id.rbHard)
        }
        showAlertDialog("Choose new size", boardSizeView, View.OnClickListener {
            boardSize = when (radioGroupSize.checkedRadioButtonId){
                R.id.rbEasy -> BoardSize.EASY
                R.id.rbMedium -> BoardSize.MEDIUM
                else -> BoardSize.HARD
            }

            gameName = null
            customGameImages = null
            setupBoard()
        })
    }

    private fun showAlertDialog(title : String, view : View?, positiveClickListener : View.OnClickListener) {
        AlertDialog.Builder(this)
            .setTitle(title)
            .setView(view)
            .setNegativeButton("Cancel", null)
            .setPositiveButton("OK"){_,_ ->
                positiveClickListener.onClick(null)
            }.show()
    }

    private fun updateGameWithFlip(position : Int) {

        //fall cases
        if(memoryGame.haveWonGame()){
            Snackbar.make(clRoot, "You already won!", Snackbar.LENGTH_LONG).show()
            return
        }
        if(memoryGame.isCardFaceUp(position)){
            Snackbar.make(clRoot, "Invalid move!", Snackbar.LENGTH_LONG).show()
            return
        }

        //Flip the card at position
        if(memoryGame.flipCard(position)) {
            Log.i(TAG, "Found a match! Num pairs found: ${memoryGame.numPairsFound}")
            val color = ArgbEvaluator().evaluate(
                memoryGame.numPairsFound.toFloat() / boardSize.getNumPairs(),
                ContextCompat.getColor(this, R.color.color_progress_none),
                ContextCompat.getColor(this, R.color.color_progress_full)
            ) as Int
            textViewNumberPairs.setTextColor(color)
            textViewNumberPairs.text = "Pairs: ${memoryGame.numPairsFound} / ${boardSize.getNumPairs()}"
            if(memoryGame.haveWonGame()){
                Snackbar.make(clRoot, "You won! Congratulations", Snackbar.LENGTH_LONG)
            }
        }
        textViewNumberMoves.text = "Moves: ${memoryGame.getNumMoves()}"
        adapter.notifyDataSetChanged()
    }

}