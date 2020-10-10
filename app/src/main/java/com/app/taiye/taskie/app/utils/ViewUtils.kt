

package com.app.taiye.taskie.app.utils

import android.app.AlertDialog
import android.content.Context
import android.view.View
import android.widget.Toast

/**
 * Helper functions for the View layer of the app.
 */
fun View.visible() {
  visibility = View.VISIBLE
}

fun View.gone() {
  visibility = View.GONE
}


fun View.invisible() {
  visibility = View.INVISIBLE
}

fun Context.toast(message: String, length: Int = Toast.LENGTH_SHORT) {
  Toast.makeText(this, message, length).show()
}


//fun View.alertDialog(title: String, description: String, button_text: String, icon_logo: Int, ) {
//  AlertDialog.Builder(this).setTitle(title)
//    .setMessage(description)
//    .setPositiveButton(button_text) { _, _ -> }
//    .setIcon(icon_logo).show()
//}