package com.example.forwarding

object GlobalSetting {

    var phoneNumber = ""
    var isCodeMerge = true

    const val ACTION_SEND_MESSAGE = "ACTION_SEND_MESSAGE"
    const val ACTION_DELIVERY_MESSAGE = "ACTION_DELIVERY_MESSAGE"

    const val MESSAGE_SERVICE_FOREGROUND_ID = 1013
    const val MESSAGE_SERVICE_CHANNEL_ID = "message_service_id"
    const val MESSAGE_SERVICE_CHANNEL_NAME = "message_service"
}