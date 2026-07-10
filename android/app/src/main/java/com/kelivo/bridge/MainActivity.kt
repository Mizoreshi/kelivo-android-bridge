package com.kelivo.bridge

import android.app.Activity
import android.os.Bundle
import android.view.Gravity
import android.widget.*
import okhttp3.*
import java.io.IOException

class MainActivity : Activity() {

    private val client = OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val prefs = getSharedPreferences("config", MODE_PRIVATE)

        val layout = LinearLayout(this)
        layout.orientation = LinearLayout.VERTICAL
        layout.setPadding(40, 40, 40, 40)

        val title = TextView(this)
        title.text = "Kelivo Bridge"
        title.textSize = 30f
        title.gravity = Gravity.CENTER

        val status = TextView(this)
        status.text = "● 服务状态：运行中"

        val serverInput = EditText(this)
        serverInput.hint = "服务器地址"
        serverInput.setText(
            prefs.getString("server", "https://memory5-vuv9.onrender.com")
        )

        val keyInput = EditText(this)
        keyInput.hint = "API Key"
        keyInput.setText(
            prefs.getString("key", "")
        )

        val saveButton = Button(this)
        saveButton.text = "保存配置"

        saveButton.setOnClickListener {

            prefs.edit()
                .putString("server", serverInput.text.toString())
                .putString("key", keyInput.text.toString())
                .apply()

            Toast.makeText(
                this,
                "配置已保存",
                Toast.LENGTH_SHORT
            ).show()
        }

        val testButton = Button(this)
        testButton.text = "测试连接"

        testButton.setOnClickListener {

            val url = serverInput.text.toString()

            val request = Request.Builder()
                .url(url)
                .build()

            client.newCall(request)
                .enqueue(object : Callback {

                    override fun onFailure(
                        call: Call,
                        e: IOException
                    ) {
                        runOnUiThread {
                            Toast.makeText(
                                this@MainActivity,
                                "连接失败: ${e.message}",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }

                    override fun onResponse(
                        call: Call,
                        response: Response
                    ) {
                        runOnUiThread {
                            Toast.makeText(
                                this@MainActivity,
                                "连接成功: ${response.code}",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                })
        }

        layout.addView(title)
        layout.addView(status)
        layout.addView(serverInput)
        layout.addView(keyInput)
        layout.addView(saveButton)
        layout.addView(testButton)

        setContentView(layout)
    }
}
