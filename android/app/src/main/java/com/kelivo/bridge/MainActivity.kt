package com.kelivo.bridge

import android.app.Activity
import android.os.Bundle
import android.view.Gravity
import android.widget.*
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException
import java.util.concurrent.TimeUnit

class MainActivity : Activity() {

    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(180, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val prefs = getSharedPreferences("config", MODE_PRIVATE)

        val layout = LinearLayout(this)
        layout.orientation = LinearLayout.VERTICAL
        layout.setPadding(40,40,40,40)

        val title = TextView(this)
        title.text = "Kelivo Bridge"
        title.textSize = 30f
        title.gravity = Gravity.CENTER

        val serverInput = EditText(this)
        serverInput.hint = "服务器地址"
        serverInput.setText(
            prefs.getString(
                "server",
                "https://memory5-vuv9.onrender.com"
            )
        )

        val messageInput = EditText(this)
        messageInput.hint = "输入测试消息"
        messageInput.setText("你好，你记得我吗？")

        val result = TextView(this)
        result.text = "等待回复..."
        result.textSize = 18f


        val saveButton = Button(this)
        saveButton.text = "保存地址"

        saveButton.setOnClickListener {

            prefs.edit()
                .putString(
                    "server",
                    serverInput.text.toString()
                )
                .apply()

            Toast.makeText(
                this,
                "已保存",
                Toast.LENGTH_SHORT
            ).show()
        }


        val sendButton = Button(this)
        sendButton.text = "发送测试消息"

        sendButton.setOnClickListener {

            val json = """
            {
              "model":"deepseek-ai/DeepSeek-V4-Flash",
              "messages":[
                {
                  "role":"user",
                  "content":"${messageInput.text}"
                }
              ]
            }
            """.trimIndent()


            val body = json.toRequestBody(
                "application/json".toMediaType()
            )


            val request = Request.Builder()
                .url(
                    serverInput.text.toString()
                    + "/v1/chat/completions"
                )
                .post(body)
                .build()


            result.text = "请求中，请等待..."
