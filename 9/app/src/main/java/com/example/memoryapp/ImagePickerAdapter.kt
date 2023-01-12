package com.example.memoryapp

import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.memoryapp.models.BoardSize
import kotlin.math.min

//Adapter that will be used to assign the images to the card containers
class ImagePickerAdapter(
    private val context : Context,
    private val imageUris: List<Uri>,
    private val boardSize: BoardSize,
    private val imageClickListener : ImageClickListener) : RecyclerView.Adapter<ImagePickerAdapter.ViewHolder>() {

    // static values
    companion object{
        private const val TAG = "ImagePickerAdapter"
    }

    interface ImageClickListener{
        fun onPlaceholderClicker()
    }

    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        private val imageViewCustomImage = itemView.findViewById<ImageView>(R.id.imageViewCustomImage)

        //Bind the viewholder ot the given uri
        fun bind(uri: Uri) {
            imageViewCustomImage.setImageURI(uri)
            imageViewCustomImage.setOnClickListener(null)
        }

        //binds no image
        fun bind() {
            imageViewCustomImage.setOnClickListener{
                imageClickListener.onPlaceholderClicker()
            }
        }
    }

    //based on the boards size, select the appropiate size to proportionally fit in the viewport
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        Log.i(TAG, "onCreateViewHolder")
        val view = LayoutInflater.from(context).inflate(R.layout.card_image, parent, false)
        val cardWidth = parent.width / boardSize.getWidth()
        val cardHeight = parent.height / boardSize.getHeight()
        val cardSideLength = min(cardWidth, cardHeight)
        val layoutParams = view.findViewById<ImageView>(R.id.imageViewCustomImage).layoutParams

        layoutParams.width = cardSideLength
        layoutParams.height = cardSideLength
        return ViewHolder(view)
    }

    //binds image to the correspondent card
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (position < imageUris.size){
            holder.bind(imageUris[position])
        } else {
            holder.bind()
        }
    }

    //gets the number of pairs
    override fun getItemCount() = boardSize.getNumPairs()
}
