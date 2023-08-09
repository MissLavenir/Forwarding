package com.example.forwarding

import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.widget.TextView
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.res.ResourcesCompat
import com.example.forwarding.ext.setStatusBarColor
import com.example.forwarding.ext.toast
import com.example.forwarding.service.MessageService
import com.example.forwarding.util.DialogUtil
import com.example.forwarding.util.SpUtil

class MainActivity : AppCompatActivity() {

    //开启后后台会发送一次短信转发
    private var isStartService = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setStatusBarColor(ResourcesCompat.getColor(resources, R.color.grey_700, theme))

        val savePhone = findViewById<TextView>(R.id.savePhone)
        val receivePhone = findViewById<AppCompatEditText>(R.id.receivePhone)
        val applyPermission = findViewById<TextView>(R.id.applyPermission)
        val startService = findViewById<TextView>(R.id.startService)
        val sendTest = findViewById<TextView>(R.id.sendTest)

        SpUtil.read(this, "phone")?.let {
            if (it is String && it.isNotEmpty()){
                GlobalSetting.phoneNumber = it
                receivePhone.setText(it)
            }
        }

        savePhone.setOnClickListener {
            val phone = receivePhone.text.toString()
            if (phone.length == 11){
                GlobalSetting.phoneNumber = phone
                SpUtil.save(this, "phone", phone)
                toast("保存成功")
            } else {
                toast("手机号码不正确")
            }
        }

        applyPermission.setOnClickListener {
            //原本是申请权限，但还要考虑垃圾机型提示已授权而系统内是“询问”的奇葩情况，因此直接跳转系统自己开权限吧
            DialogUtil.showConfirmDialog(
                this,
                "您将跳转到系统设置页面，请确保在设置中开启发送和接收短信等权限，部分机型也需要开启“允许前台运行”权限。",
                "权限提示"){
                if (it) {
                    startActivity(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:${application.packageName}")))
                }
            }
        }

        val intentService = Intent(applicationContext, MessageService::class.java)
        startService.setOnClickListener {
            if (isStartService){
                startService.text = "开启前台服务"
                stopService(intentService)
                isStartService = false
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P){
                    startForegroundService(intentService)
                    startService.text = "关闭前台服务"
                    isStartService = true
                } else {
                    startService(Intent(applicationContext, MessageService::class.java))
                    startService.text = "关闭前台服务"
                    isStartService = true
                }
            }
        }

        sendTest.setOnClickListener {
            if (isStartService){
                startService.text = "开启前台服务"
                stopService(intentService)
                isStartService = false
            }
            startActivity(Intent(this, TestForwardActivity::class.java))
        }

    }

}