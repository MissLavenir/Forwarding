package com.example.forwarding.service

import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.content.IntentFilter
import android.os.IBinder
import android.provider.Telephony
import android.util.Log
import com.example.forwarding.GlobalSetting
import com.example.forwarding.MainActivity
import com.example.forwarding.broadcast.MessageControlBroadcast
import com.example.forwarding.util.NotificationUtil

class MessageService: Service() {
    private val mcBroadcast = MessageControlBroadcast

    override fun onCreate() {
        super.onCreate()
        Log.e("debugService","MessageService is created")

        val pendingIntent = PendingIntent.getActivity(baseContext,1,Intent(baseContext, MainActivity::class.java), PendingIntent.FLAG_IMMUTABLE)
        val notification = NotificationUtil(applicationContext).getNotification("短信转发服务","已开启短信转发服务，收到短信时将自动转发到目标手机!", pendingIntent)
        startForeground(GlobalSetting.MESSAGE_SERVICE_FOREGROUND_ID, notification.build())
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.e("debugService","mcBroadcast is registered")
        registerReceiver(mcBroadcast, IntentFilter(Telephony.Sms.Intents.SMS_RECEIVED_ACTION))
        registerReceiver(mcBroadcast, IntentFilter(GlobalSetting.ACTION_SEND_MESSAGE))
        registerReceiver(mcBroadcast, IntentFilter(GlobalSetting.ACTION_DELIVERY_MESSAGE))

        return START_STICKY
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onDestroy() {
        Log.e("debugService","mcBroadcast is unregistered")
        unregisterReceiver(mcBroadcast)
        stopForeground(STOP_FOREGROUND_REMOVE)
        super.onDestroy()
    }


}