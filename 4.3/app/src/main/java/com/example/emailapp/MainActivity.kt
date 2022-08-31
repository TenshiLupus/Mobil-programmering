package com.example.emailapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.net.URI

private const val REQUEST_CODE = 7
private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {

    private lateinit var editTextTo : EditText
    private lateinit var editSubjectTo : EditText
    private lateinit var editTextMessage : EditText
    private lateinit var button : Button
    private lateinit var attachmentButton : Button
    private lateinit var attachmentName : TextView
    private var uri : Uri? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        editTextTo = findViewById(R.id.edit_text_to)
        editSubjectTo = findViewById(R.id.edit_text_subject)
        editTextMessage = findViewById(R.id.edit_text_message)

        button = findViewById(R.id.button_send)
        attachmentButton = findViewById(R.id.attachment_button)
        attachmentName = findViewById(R.id.attachment_name)

        attachmentButton.setOnClickListener() {
            openFolder()
        }
        button.setOnClickListener(){
            sendMail()
        }

    }

    private fun sendMail(){
        val recipientList = editTextTo.text.toString()
        val recipients : Array<String> = recipientList.split(",").toTypedArray()
        val subject = editSubjectTo.text.toString()
        val message = editTextMessage.text.toString()


        val intent : Intent = Intent(Intent.ACTION_SEND)
        intent.putExtra(Intent.EXTRA_EMAIL , recipients)
        intent.putExtra(Intent.EXTRA_SUBJECT, subject)
        intent.putExtra(Intent.EXTRA_TEXT, message)
        if (uri != null) {
            intent.putExtra(Intent.EXTRA_STREAM, uri);
        }

        intent.setType("message/rfc822")

        startActivity(Intent.createChooser(intent, "Please choose and email client"))
    }

    fun openFolder() {
        val intent : Intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        intent.putExtra("return-data", true)
        startActivityForResult(Intent.createChooser(intent, "Complete action using"), REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            uri = data?.data!!;
            attachmentName.text = uri!!.lastPathSegment;
            attachmentName.visibility = View.VISIBLE;
        }
    }
}