package com.example.contacts

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.add
import androidx.fragment.app.commit


class MainActivity : AppCompatActivity() {

    private val PERMISSIONS_REQUEST_READ_CONTACTS = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf<String>(Manifest.permission.READ_CONTACTS), PERMISSIONS_REQUEST_READ_CONTACTS);
            //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method
        }

        var button = this@MainActivity.findViewById(R.id.button) as Button

        button.setOnClickListener {
            if(savedInstanceState == null){
                val editText = this@MainActivity.findViewById<EditText>(R.id.editTextTextPersonName2)
                var searchName = editText.text.toString()

                var bundle = bundleOf("searchName" to searchName)
                supportFragmentManager.commit {

                    setReorderingAllowed(true)
                    add<ContactsFragment>(R.id.fragment_container_view, args = bundle)
                }
            }
            Toast.makeText(this, "PLEASE WORK", Toast.LENGTH_LONG).show()
            Log.i("myatag", "CLICKING")
        }
    }

    fun proceed(savedInstanceState: Bundle?){

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted
                return
            } else {
                Toast.makeText(this, "Until you grant the permission, we canot display the names", Toast.LENGTH_SHORT).show();
            }
        }

    }

}

