package com.example.forwarding.util

import android.content.Context
import android.util.Base64
import android.util.Log
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

object SpUtil {
    fun save(context: Context, modelName: String, data: Any?) {
        val preferences = context.getSharedPreferences(
            "base64"
                    + modelName, Context.MODE_PRIVATE
        )
        // 创建字节输出流
        val bytes = ByteArrayOutputStream()
        try {
            // 创建对象输出流，并封装字节流
            val oos = ObjectOutputStream(bytes)
            // 将对象写入字节流
            oos.writeObject(data)
            // 将字节流编码成base64的字符窜
            val outString = String(Base64.encode(bytes.toByteArray(), Base64.DEFAULT))
            val editor = preferences.edit()
            editor.putString(modelName, outString)
            editor.apply()
        } catch (e: IOException) {
            print(e.toString())
        }
        Log.i("ok", "存储成功")
    }

    fun read(context: Context, modelName: String): Any? {
        var data: Any? = null
        val preferences = context.getSharedPreferences(
            "base64"
                    + modelName, Context.MODE_PRIVATE
        )
        val productBase64 = preferences.getString(modelName, "") ?: return ""
        // 读取字节
        val base64: ByteArray = Base64.decode(productBase64.toByteArray(), Base64.DEFAULT)

        // 封装到字节流
        val bytes = ByteArrayInputStream(base64)
        try {
            // 再次封装
            val bis = ObjectInputStream(bytes)
            try {
                // 读取对象
                data = bis.readObject() as Any
            } catch (_: ClassNotFoundException) {

            }
        } catch (_: IOException) {
        }

        return data
    }
}