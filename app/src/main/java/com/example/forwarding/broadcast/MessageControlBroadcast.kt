package com.example.forwarding.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Telephony
import android.telephony.SmsMessage
import android.util.Log
import com.example.forwarding.GlobalSetting
import com.example.forwarding.util.SendMessageUtil

object MessageControlBroadcast: BroadcastReceiver() {
    private val noSendList = arrayListOf("10001","10659037110000","10086","10010")

    override fun onReceive(context: Context, intent: Intent) {
        when(intent.action){
            GlobalSetting.ACTION_SEND_MESSAGE,GlobalSetting.ACTION_DELIVERY_MESSAGE -> {
                Log.e("debugBroadcast","${intent.action}=${intent.extras?.getString("sendExtra") ?: "--"}")
            }
            Telephony.Sms.Intents.SMS_RECEIVED_ACTION -> {
                intent.extras?.get("pdus")?.let {
                    try {
                        var message = ""
                        var disNumber = ""
                        (it as Array<Any>).forEach {byteArray ->
                            val sMsg = SmsMessage.createFromPdu(byteArray as ByteArray,"3gpp")
                            Log.e("message","tel:${sMsg.displayOriginatingAddress},sms:${sMsg.displayMessageBody}")
                            disNumber = sMsg.displayOriginatingAddress
                            message += sMsg.displayMessageBody
                        }
                        if (GlobalSetting.phoneNumber.length == 11 && !noSendList.contains(disNumber)) {
                            SendMessageUtil.sendMessage(context ,GlobalSetting.phoneNumber, "<原发送人：${disNumber}> $message")
                        }
                    } catch (e: Exception){
                        e.printStackTrace()
                    }
                }
            }
        }
    }
}