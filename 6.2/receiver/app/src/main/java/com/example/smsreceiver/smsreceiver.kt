package com.example.smsreceiver

import android.annotation.TargetApi
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.telephony.SmsMessage
import java.util.*

private const val TAG = "SmsReceiver"
private const val pdu_type = "pdus"
class SmsReceiver : BroadcastReceiver() {


    @TargetApi(Build.VERSION_CODES.Q)
    override fun onReceive(context: Context, intent: Intent) {
        // This method is called when the BroadcastReceiver is receiving an Intent broadcast.

        val bundle : Bundle? = intent.extras
        val msgs : Array<SmsMessage>? = null
        val strMessage = ""
        if(bundle != null){
            val pdus : Array<Objects>  = bundle.get("pdus")
            msgs = SmsMessage[pdus.]
        }

        //retrieve the SMS message in small segments

        val pdus : Array<*> = arrayOf(bundle.get(pdu_type))
        if(pdus != null){
            //Check android version
            val isVersionM : Boolean = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)

        }
    }
}