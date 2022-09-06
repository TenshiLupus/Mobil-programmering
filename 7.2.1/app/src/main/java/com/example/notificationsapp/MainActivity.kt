package com.example.notificationsapp

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

private const val CHANNEL_ID = "SEVEN"
private const val CHANNEL_NAME = "tricorn"
private const val NOTIFICATION_ID = 0

class MainActivity : AppCompatActivity() {
    private lateinit var buttonDialog : Button
    private lateinit var buttonNotification : Button
    private lateinit var buttonToast : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        createNotificationChannel()

        //Buttons
        buttonDialog = findViewById(R.id.dialog_button)
        buttonNotification = findViewById(R.id.notification_button)
        buttonToast = findViewById(R.id.toast_button)


        //Notification Button setup
        val intent = Intent(this, MainActivity::class.java)

        val pendingIntent = TaskStackBuilder.create(this).run {
            addNextIntentWithParentStack(intent)
            getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
        }

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Notification triggered")
            .setContentText("This notifcation has been triggered by the app")
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .build()

        val notificationManager = NotificationManagerCompat.from(this)


        //Setup button functionality
        buttonDialog.setOnClickListener{
            openDialog()
        }
        buttonNotification.setOnClickListener{
            notificationManager.notify(NOTIFICATION_ID,notification)
        }
        buttonToast.setOnClickListener {
            Toast.makeText(this, "Toasted", Toast.LENGTH_SHORT).show()
        }

    }

    fun createNotificationChannel(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT).apply{
                lightColor = Color.GREEN
                enableLights(true)
            }
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }

    fun openDialog(){
        val dialog : ExampleDialog = ExampleDialog()
        dialog.show(supportFragmentManager, "example Dialog")
    }


}