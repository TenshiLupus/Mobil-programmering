package com.example.notificationsapp

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AppCompatDialogFragment

class ExampleDialog : AppCompatDialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreateDialog(savedInstanceState)

        val builder : AlertDialog.Builder = AlertDialog.Builder(activity)

        builder.setTitle("Information")
            .setMessage("This is a Dialog")
            .setPositiveButton("ok", DialogInterface.OnClickListener{
                dialog: DialogInterface?, which: Int ->


            })
        return builder.create()
    }
}