package com.example.forwarding.util

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import androidx.appcompat.widget.AppCompatTextView
import com.example.forwarding.R

object DialogUtil {

    /**
     * 弹出一个确实提示框
     * @param context 上下文
     * @param message 提示语
     * @param title 标题
     * @param callBack lambda表达式回调
     */
    fun showConfirmDialog(context: Context, message: String ,title: String?="提示", callBack: (Boolean) -> Unit) {
        val dialog = LayoutInflater.from(context).inflate(R.layout.dialog_sure_btn, null)
        val alert = AlertDialog.Builder(context).setView(dialog).create()
        val sureTitle = dialog.findViewById<AppCompatTextView>(R.id.sureTitle)
        val sureMessage = dialog.findViewById<AppCompatTextView>(R.id.sureMessage)
        val sureBtn = dialog.findViewById<AppCompatTextView>(R.id.sureBtn)
        val cancelBtn = dialog.findViewById<AppCompatTextView>(R.id.cancelBtn)

        sureTitle.text = title
        sureMessage.text = message

        sureBtn.setOnClickListener {
            callBack.invoke(true)
            alert.dismiss()
        }


        cancelBtn.setOnClickListener {
            callBack.invoke(false)
            alert.dismiss()
        }
        alert.show()
    }

}