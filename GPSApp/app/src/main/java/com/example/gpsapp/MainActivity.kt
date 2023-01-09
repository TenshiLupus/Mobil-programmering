package com.example.gpsapp

import android.Manifest
import android.annotation.SuppressLint
import android.content.IntentSender
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.util.Log
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task


val RCS = 100

val locationRequest = LocationRequest.create()?.apply {
    interval = 10000
    fastestInterval = 5000
    priority = LocationRequest.PRIORITY_HIGH_ACCURACY
}


class MainActivity : AppCompatActivity() {

    private lateinit var fusedLocationClient : FusedLocationProviderClient
    private var requestingLocationUpdates = false
    private lateinit var locationCallback : LocationCallback

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        updateValuesFromBundle(savedInstanceState)

        val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()){ permissions ->
            when{
                permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                    Log.i("permission", "Granted")
                }
                permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                    Log.i("permission", "Granted")
                } else -> {
                Log.i("permission", "NONE GRANTED")
            }
            }
        }

        requestPermissionLauncher.launch(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION))
        createLocationRequest()

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        fusedLocationClient.lastLocation.addOnSuccessListener { location ->

        }

        locationCallback = object : LocationCallback(){
            override fun onLocationResult(p0: LocationResult) {

                for (location in p0.locations){
                    Log.i("locations", location.toString())
                }

            }
        }
    }

    override fun onResume() {
        super.onResume()
        if(requestingLocationUpdates){

        }
    }

    @SuppressLint("MissingPermission")
    fun startLocationUpdates(){
        fusedLocationClient.requestLocationUpdates(locationRequest!!, locationCallback, Looper.getMainLooper())
    }

    override fun onPause() {
        super.onPause()
        stopLocationUpdates()
    }

    private fun stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }


    fun createLocationRequest(){

        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest!!)

        val client : SettingsClient = LocationServices.getSettingsClient(this)
        val task : Task<LocationSettingsResponse> = client.checkLocationSettings(builder.build())

        task.addOnSuccessListener { locationSettingResponse ->

        }

        task.addOnFailureListener { exception ->
            if (exception is ResolvableApiException){
                try{
                    exception.startResolutionForResult(this@MainActivity, RCS)
                } catch (sendEx : IntentSender.SendIntentException){
                    //ignore error
                }
            }

        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putBoolean("LUK", requestingLocationUpdates)
        super.onSaveInstanceState(outState)
    }

    private fun updateValuesFromBundle(savedInstanceState: Bundle?) {
        savedInstanceState ?: return

        // Update the value of requestingLocationUpdates from the Bundle.
        if (savedInstanceState.keySet().contains("LUK")) {
            requestingLocationUpdates = savedInstanceState.getBoolean("LUK")
        }
    }
}