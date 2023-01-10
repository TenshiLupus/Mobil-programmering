package com.example.gpsapp


import android.Manifest
import android.app.*
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.IBinder
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat

import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import java.lang.UnsupportedOperationException

val LOCATION_SERVICE_ID = 175
val ACTION_START_LOCATION_SERVICE = "startLocationService"
val ACTION_STOP_LOCATION_SERVICE = "stopLocationService"


class LocationService() : Service(){

    private var locationCallback : LocationCallback = object : LocationCallback() {
        override fun onLocationResult(p0: LocationResult?) {
            super.onLocationResult(p0)
            if (p0 != null && p0.lastLocation != null) run {
                var latitude: Double = p0.lastLocation.latitude
                var longitude: Double = p0.lastLocation.longitude
                Log.d("Location", "$latitude , $longitude")
                Toast.makeText(applicationContext, "Latitude : $latitude , Longitude: $longitude", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onBind(p0: Intent?): IBinder? {
        throw UnsupportedOperationException("Not yet implemented")
    }

    private fun startLocationService(){
        var channelId : String = "location_notification_channel"
        var notificationManager : NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        var resultIntent: Intent = Intent()
        var pendingintent = PendingIntent.getActivity(applicationContext, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        var builder : NotificationCompat.Builder = NotificationCompat.Builder(applicationContext, channelId)

        builder.setSmallIcon(R.mipmap.ic_launcher)
        builder.setContentTitle("Location Service")
        builder.setDefaults(NotificationCompat.DEFAULT_ALL)
        builder.setContentText("Running")
        builder.setContentIntent(pendingintent)
        builder.setPriority(NotificationCompat.PRIORITY_MAX)

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            if(notificationManager != null && notificationManager.getNotificationChannel(channelId) == null){
                var notificationChannel = NotificationChannel(channelId,"Location service", NotificationManager.IMPORTANCE_HIGH)
                notificationChannel.description = "THis channel is used by location service"
                notificationManager.createNotificationChannel(notificationChannel)
            }
        }

        var locationRequest = LocationRequest()
        locationRequest.setInterval(5000)
        locationRequest.setFastestInterval(2000)
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)


        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            return
        }
        LocationServices.getFusedLocationProviderClient(this).requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
        startForeground(LOCATION_SERVICE_ID, builder.build())
    }

    private fun stopLocationService (){
        LocationServices.getFusedLocationProviderClient(this).removeLocationUpdates(locationCallback)
        stopForeground(true)
        stopSelf()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if(intent != null ){
            var action : String? = intent.action
            if(action != null){
                if(action.equals(ACTION_START_LOCATION_SERVICE)){
                    startLocationService()
                }
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }
}