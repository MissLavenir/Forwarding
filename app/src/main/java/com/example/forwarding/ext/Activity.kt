package com.example.forwarding.ext

import android.app.Activity
import android.view.WindowManager
import android.widget.Toast
import androidx.annotation.ColorInt

fun Activity.toast(string: String) {
    Toast.makeText(this, string, Toast.LENGTH_LONG).show()
}

fun Activity.setStatusBarColor(@ColorInt color : Int){
    val window = window
    //这一步最好要做，因为如果这两个flag没有清除的话下面没有生效
    window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS or  WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
    getWindow().statusBarColor = color
}
