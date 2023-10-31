package com.example.forwarding

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.res.ResourcesCompat
import com.example.forwarding.ext.setStatusBarColor
import com.example.forwarding.ext.toast
import com.example.forwarding.util.SendMessageUtil

class TestForwardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_forward)

        setStatusBarColor(ResourcesCompat.getColor(resources, R.color.grey_700, theme))

        val inputPhone = findViewById<AppCompatEditText>(R.id.inputPhone)
        val inputMessage = findViewById<AppCompatEditText>(R.id.inputMessage)
        val sendSms = findViewById<TextView>(R.id.sendSms)

        if (GlobalSetting.phoneNumber.isNotEmpty()) inputPhone.setText( GlobalSetting.phoneNumber )

        sendSms.setOnClickListener {
            if (inputPhone.text.toString().length != 11) {
                return@setOnClickListener toast("手机号码错误")
            }
            GlobalSetting.phoneNumber = inputPhone.text.toString()

            if (inputMessage.text.toString().isEmpty()) {
                return@setOnClickListener toast("短信内容不能为空")
            }

            SendMessageUtil.sendMessage(this, inputPhone.text.toString(), "<原发送人：test> "+inputMessage.text.toString())

        }
    }


}