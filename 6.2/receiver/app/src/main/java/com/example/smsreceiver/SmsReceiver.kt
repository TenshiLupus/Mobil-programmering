package com.example.smsreceiver

import android.annotation.TargetApi
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.telephony.SmsMessage
import android.util.Log
import android.widget.Toast
import java.util.*

private const val TAG = "SmsReceiver"
private const val pdu_type = "pdus"
class SmsReceiver : BroadcastReceiver() {


    override fun onReceive(context: Context, intent: Intent) {
        // This method is called when the BroadcastReceiver is receiving an Intent broadcast.

        val bundle : Bundle? = intent.extras
        var msgs : Array<SmsMessage?>?
        var strMessage = ""
        val format : String? = bundle?.getString("format")
        val pdus : Array<*>  = bundle?.get(pdu_type) as Array<*>

        if(pdus != null){
            val isVersionM = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            msgs = arrayOfNulls<SmsMessage>(pdus.size)

            var i : Int = 0
            while(i < msgs.size){
                if(isVersionM){
                    msgs[i] = SmsMessage.createFromPdu(pdus[i] as ByteArray,format)
                }else{
                    msgs[i] = SmsMessage.createFromPdu(pdus[i] as ByteArray)
                }

                strMessage += "SMS from " + msgs[i]?.originatingAddress
                strMessage += " :" + msgs[i]?.messageBody + "\n"
                Log.d(TAG, "onReceive: " + strMessage)
                Toast.makeText(context, strMessage, Toast.LENGTH_LONG).show()
                i++
            }

        }

    }
}