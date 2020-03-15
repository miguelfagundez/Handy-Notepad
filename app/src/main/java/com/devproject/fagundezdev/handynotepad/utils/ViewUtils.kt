package com.devproject.fagundezdev.handynotepad.utils

import android.app.Activity
import android.widget.Toast
import androidx.fragment.app.Fragment

/********************************************
 * View Utils file
 * Creating some view utils such as extension functions
 * @author: Miguel Fagundez
 * @date: March 06th, 2020
 * @version: 1.0
 * *******************************************/
//*************************************************
// Send a toast to this Activity/Fragment context
//*************************************************
fun Activity.toast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, duration).show()
}

fun Fragment.toast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(activity, message, duration).show()
}
