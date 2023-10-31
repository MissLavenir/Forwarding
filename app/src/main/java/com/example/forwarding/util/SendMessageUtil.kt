package com.example.forwarding.util

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.telephony.SmsManager
import android.util.Log
import com.example.forwarding.GlobalSetting
import java.lang.StringBuilder
import kotlin.math.max
import kotlin.math.min

object SendMessageUtil {
    private val sManager = SmsManager.getDefault()
    fun sendMessage(context: Context, phone: String, message: String){
        if (message.length < 70){
            sendFun(context, phone, message)
        } else if (GlobalSetting.isCodeMerge && message.contains("验证码")){
            //精简验证码到一条短信转发
            val codeAfter = message.substringAfter("> ")
            val codMsg = StringBuilder().apply {
                append(message.substringBefore("> "))
                append("> ") //分割后是没有"> "的
                if (codeAfter.indexOf("验证码") > 20) append("...")
                append(codeAfter.substring(max(0, codeAfter.indexOf("验证码")-20), min(codeAfter.length, codeAfter.indexOf("验证码")+23)))
                if (codeAfter.length > 43) append("...")
            }
            sendFun(context, phone, codMsg.toString())
        } else {
            sendMultipartList(context, phone, message)
        }
    }
    private fun sendFun(context: Context, phone: String, message: String){
        Log.e("debug",message)
        sManager.sendTextMessage(
            phone,
            null,
            message,
            PendingIntent.getBroadcast(
                context,
                1,
                Intent(GlobalSetting.ACTION_SEND_MESSAGE).putExtra("sendExtra","send"),
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            ),
            PendingIntent.getBroadcast(
                context,
                2,
                Intent(GlobalSetting.ACTION_DELIVERY_MESSAGE).putExtra("sendExtra","deliver"),
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )
        )
    }

    private fun sendMultipartList(context: Context, phone: String, message: String){
        val msgArray = sManager.divideMessage(message)
        val sendArray = ArrayList<PendingIntent>()
        val deliverArray = ArrayList<PendingIntent>()
        msgArray.forEachIndexed { index, _ ->
            sendArray.add(PendingIntent.getBroadcast(
                context,
                index*2,
                Intent(GlobalSetting.ACTION_SEND_MESSAGE+index.toString()).putExtra("sendExtra","send"),
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            ))
            deliverArray.add(PendingIntent.getBroadcast(
                context,
                index*2+1,
                Intent(GlobalSetting.ACTION_DELIVERY_MESSAGE+(index*2+1).toString()).putExtra("sendExtra","deliver"),
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            ))
        }
        Log.e("debug",msgArray.toString())
        sManager.sendMultipartTextMessage(phone,null, msgArray, sendArray, deliverArray)
    }

}