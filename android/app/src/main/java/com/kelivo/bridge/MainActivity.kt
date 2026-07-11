package com.kelivo.bridge

import android.app.Activity
import android.os.Bundle
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

        val layout = LinearLayout(this)
        layout.orientation = LinearLayout.VERTICAL
        layout.setPadding(40,40,40,40)

        val serverInput = EditText(this)
        serverInput.setText("https://memory5-vuv9.onrender.com")

        val messageInput = EditText(this)
        messageInput.setText("你好，你记得我吗？")

        val result = TextView(this)
        result.text = "等待回复"


        val button = Button(this)
        button.text = "发送测试消息"

        button.setOnClickListener {

            result.text = "请求中，请等待..."

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


            client.newCall(request)
                .enqueue(object: Callback {

                    override fun onFailure(
                        call: Call,
                        e: IOException
                    ) {
                        runOnUiThread {
                            result.text = e.message
                        }
                    }


                    override fun onResponse(
                        call: Call,
                        response: Response
                    ) {
                        val text = response.body?.string()

                        runOnUiThread {
                            result.text =
                                "状态:${response.code}\n$text"
                        }
                    }
                })
        }


        layout.addView(serverInput)
        layout.addView(messageInput)
        layout.addView(button)
        layout.addView(result)

        setContentView(layout)
    }
}
