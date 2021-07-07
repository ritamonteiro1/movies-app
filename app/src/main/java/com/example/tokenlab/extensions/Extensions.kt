package com.example.tokenlab.extensions

import android.app.Dialog
import android.content.Context
import androidx.appcompat.app.AlertDialog
import com.example.tokenlab.R

fun Context.createLoadingDialog(): Dialog {
    val builder = AlertDialog.Builder(this)
    builder.setView(R.layout.progress_dialog)
    val dialog: Dialog = builder.create()
    dialog.setCancelable(false)
    dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
    return dialog
}