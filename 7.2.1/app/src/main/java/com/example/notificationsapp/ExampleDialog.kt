package com.example.notificationsapp

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatDialogFragment

class ExampleDialog : AppCompatDialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreateDialog(savedInstanceState)

        val builder : AlertDialog.Builder = AlertDialog.Builder(activity)

        builder.setTitle("This a dialog popup")
            .setMessage("proceed to program?")
            .setPositiveButton("Accept", DialogInterface.OnClickListener{
                dialog: DialogInterface?, which: Int ->
                Toast.makeText("")

            })
            .setNegativeButton("Cancel", DialogInterface.OnClickListener{
                dialog, which ->

            })
        return builder.create()
    }
}