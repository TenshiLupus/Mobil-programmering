package com.example.calllog

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.CallLog
import android.text.TextUtils.indexOf
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import android.widget.ListView
import android.widget.ListAdapter
import android.widget.ArrayAdapter
import android.widget.AdapterView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.sql.Date
import java.text.SimpleDateFormat

private val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {

    private var stringList : Array<String> = emptyArray()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED){

            if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_CALL_LOG)) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_CALL_LOG),1)
            } else {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_CALL_LOG),1)
            }
        }else {

           getCallDetails()

        }
    }

    //Present result based on permission request
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            1 -> {
                if(grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CALL_LOG) == PackageManager.PERMISSION_GRANTED){
                        Toast.makeText(this, "permission granted", Toast.LENGTH_SHORT).show()
                        //make a call log list
                        getCallDetails()

                    }
                }
                else {
                    Toast.makeText(this, "No permission granted", Toast.LENGTH_SHORT).show()
                }
                return
            }
        }

    }

    //Retrieve call logs from system handled by a ListView adapter
    private fun getCallDetails() {
        val sb : StringBuffer = StringBuffer()
        val managedCursor : Cursor? = contentResolver.query(CallLog.Calls.CONTENT_URI, null, null, null, null)
        val number : Int = managedCursor!!.getColumnIndex(CallLog.Calls.NUMBER)
        val type : Int = managedCursor!!.getColumnIndex(CallLog.Calls.TYPE)
        val date : Int = managedCursor!!.getColumnIndex(CallLog.Calls.DATE)
        val duration : Int = managedCursor!!.getColumnIndex(CallLog.Calls.DURATION)
        sb.append("Call Details:\n\n")

        while (managedCursor.moveToNext()){
            val phNumber : String = managedCursor.getString(number)
            val callType : String = managedCursor.getString(type)
            val callDate : String = managedCursor.getString(date)
            val callDayTime = Date(callDate.toLong())
            val formatter : SimpleDateFormat = SimpleDateFormat("dd-MM-yy HH:mm")
            val dateString : String = formatter.format(callDayTime)
            val callDuration : String = managedCursor.getString(duration)
            var dir: String? = null
            val dircode : Int = Integer.parseInt(callType)
            when (dircode){
                CallLog.Calls.OUTGOING_TYPE -> dir = "OUTGOING"
                CallLog.Calls.INCOMING_TYPE -> dir = "INCOMING"
                CallLog.Calls.MISSED_TYPE -> dir = "MISSED"
            }
            sb.append("\nPhone Number: " + phNumber +
                    "\nCallType: " + dir +
                    "\n Call Date: " + dateString +
                    "\n Call Duration: " + callDuration)
            sb.append("\n\n#\n\n")

        }


        stringList = sb.split('#').toTypedArray()

        val returnedArray = stringList
        val la : ArrayAdapter<String> = ArrayAdapter(this, android.R.layout.simple_list_item_1, returnedArray)
        val callsLog : ListView = findViewById(R.id.callsList)
        callsLog.adapter = la

        callsLog.onItemClickListener = AdapterView.OnItemClickListener{ parent : AdapterView<*>, view1: View, position: Int, id : Long ->

            //assign number based on the number stored in string format
            val log =  parent.getItemAtPosition(position).toString()
            val beggining = log.indexOf("Phone Number: ") + 14
            Log.d(TAG, "${log.indexOf("Phone Number: ")} ")
            Log.d(TAG, "${beggining}")
            val tel = log.substring(beggining, beggining + 11)
            Log.d(TAG, tel)
            val intentStr = "tel:" + tel
            val telIntent = Intent(Intent.ACTION_DIAL, Uri.parse(intentStr))
            startActivity(telIntent)

        }
        Log.d(TAG, "${stringList}")
        managedCursor.close()

    }


}