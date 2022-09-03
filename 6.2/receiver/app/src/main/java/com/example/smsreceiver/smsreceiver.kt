package com.example.smsreceiver

import android.annotation.TargetApi
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.telephony.SmsMessage
import android.widget.Toast
import java.util.*

private const val TAG = "SmsReceiver"
private const val pdu_type = "pdus"
class SmsReceiver : BroadcastReceiver() {


    @TargetApi(Build.VERSION_CODES.Q)
    override fun onReceive(context: Context, intent: Intent) {
        // This method is called when the BroadcastReceiver is receiving an Intent broadcast.

        val bundle : Bundle? = intent.extras
        var msgs : Array<SmsMessage?>? = null
        var strMessage = ""
        if(bundle != null){
            val pdus : Array<Objects>  = bundle.get("pdus") as Array<Objects>
            msgs = arrayOfNulls<SmsMessage>(pdus.size)

            var i : Int = 0
            while(i < msgs.size){
                msgs[i] = SmsMessage.createFromPdu(pdus[i] as ByteArray)
                strMessage = (msgs[i]?.messageBody) as String
                Toast.makeText(context, strMessage, Toast.LENGTH_SHORT).show()
            }

        }

    }
}