package com.example.memoryapp.utils

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

//Helper functions to assert and request permissions from the device
fun isPermissionGranted(context : Context, permission : String) : Boolean {
    return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
}

fun requestPermission(activity: Activity?, permission: String, requestCode : Int ){
    ActivityCompat.requestPermissions(activity!!, arrayOf(permission), requestCode)
}