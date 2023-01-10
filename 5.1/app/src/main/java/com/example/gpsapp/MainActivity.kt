package com.example.gpsapp

import android.Manifest
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat


class MainActivity : AppCompatActivity() {

    private val REQUEST_CODE_LOCATION_PERMISSION = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        findViewById<Button>(R.id.startButton).setOnClickListener {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf<String>(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_CODE_LOCATION_PERMISSION)
            }
            else{
                startLocationService()
            }

        }

        findViewById<Button>(R.id.stopButton).setOnClickListener{
            stopLocationService()
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == REQUEST_CODE_LOCATION_PERMISSION && grantResults.isNotEmpty()){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                startLocationService()
            }else{
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun isLocationServiceRunning() : Boolean{
        var activityManager : ActivityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        if(activityManager != null){
            for ( service in activityManager.getRunningServices(Integer.MAX_VALUE)){
                if(LocationService::class.java.name.equals(service.service.className)){
                    if(service.foreground){
                        return true
                    }
                }
            }
            return false
        }
        return false
    }

    private fun startLocationService(){
        if(!isLocationServiceRunning()){
            var intent = Intent(applicationContext, LocationService::class.java)
            intent.setAction(ACTION_START_LOCATION_SERVICE)
            startService(intent)
            Toast.makeText(this,"Location service started",Toast.LENGTH_SHORT).show()
        }
    }

    private fun stopLocationService(){
        if(isLocationServiceRunning()) run {
            var intent = Intent(applicationContext, LocationService::class.java)
            intent.setAction(ACTION_STOP_LOCATION_SERVICE)
            startService(intent)
            Toast.makeText(this, "Location Service Stopped", Toast.LENGTH_SHORT).show()
        }
    }
}
