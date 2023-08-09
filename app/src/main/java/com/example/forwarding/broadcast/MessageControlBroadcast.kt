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

    override fun onReceive(context: Context, intent: Intent) {
        when(intent.action){
            GlobalSetting.ACTION_SEND_MESSAGE,GlobalSetting.ACTION_DELIVERY_MESSAGE -> {
                Log.e("debugBroadcast","${intent.action}=${intent.extras?.getString("sendExtra") ?: "--"}")
            }
            Telephony.Sms.Intents.SMS_RECEIVED_ACTION -> {
                intent.extras?.get("pdus")?.let {
                    try {
                        (it as Array<Any>).forEach {byteArray ->
                            val message = SmsMessage.createFromPdu(byteArray as ByteArray,"3gpp")
                            Log.e("message","tel:${message.displayOriginatingAddress},sms:${message.displayMessageBody}")
                            if (GlobalSetting.phoneNumber.length == 11){
                                SendMessageUtil.sendMessage(context ,GlobalSetting.phoneNumber, "<原发送人：${message.displayOriginatingAddress}> ${message.displayMessageBody}")
                            }
                        }
                    } catch (e: Exception){
                        e.printStackTrace()
                    }
                }
            }
        }
    }
}