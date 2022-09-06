package com.example.notificationsapp

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.TaskStackBuilder
import android.content.Context
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

private const val CHANNEL_ID = "7"
private const val CHANNEL_NAME = "tricorn"
private const val NOTIFICATION_ID = 0

class MainActivity : AppCompatActivity() {
    private lateinit var buttonDialog : Button
    private lateinit var buttonNotification : Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        createNotificationChannel()

        buttonDialog = findViewById(R.id.dialog_button)
        buttonNotification = findViewById(R.id.notification_button)
        buttonDialog.setOnClickListener{
            openDialog()

        }

        val pendingIntent = TaskStackBuilder.create(this)

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Notification triggered")
            .setContentText("This notifcation has been triggered by the app")
            .setSmallIcon(com.google.android.material.R.drawable.abc_ic_star_black_16dp)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        val notificationManager = NotificationManagerCompat.from(this)
        buttonNotification.setOnClickListener{
            notificationManager.notify(NOTIFICATION_ID,notification)
        }
    }


    fun createNotificationChannel(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT).apply{
                lightColor = Color.GREEN
                enableLights(true)
                val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            }
        }
    }

    fun openDialog(){
        val dialog : ExampleDialog = ExampleDialog()
        dialog.show(supportFragmentManager, "example Dialog")
    }
}