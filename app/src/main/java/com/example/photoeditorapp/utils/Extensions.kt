package com.example.photoeditorapp.utils

import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.example.photoeditorapp.R

fun Fragment.showConfirmGoBackDialog(
    onConfirm: () -> Unit,
    onReject: () -> Unit = {}
) {
    val builder = AlertDialog.Builder(requireActivity())
    builder.setTitle(R.string.cancel)
    builder.setMessage(R.string.alert_dialog_message)
    builder.setIcon(R.drawable.ic_cancel_last_change)
    builder.setPositiveButton(R.string.yes) { dialog, _ ->
        onConfirm()
        dialog.cancel()
    }
    builder.setNegativeButton(R.string.no) { dialog, _ ->
        onReject()
        dialog.cancel()
    }
    val alertDialog: AlertDialog = builder.create()
    alertDialog.setCancelable(true)
    alertDialog.show()

}