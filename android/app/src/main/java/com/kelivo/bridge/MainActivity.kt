package com.kelivo.bridge

import android.app.Activity
import android.os.Bundle
import android.graphics.Color
import android.view.Gravity
import android.widget.*

class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val layout = LinearLayout(this)
        layout.orientation = LinearLayout.VERTICAL
        layout.setPadding(40, 40, 40, 40)

        val title = TextView(this)
        title.text = "Kelivo Bridge"
        title.textSize = 30f
        title.gravity = Gravity.CENTER

        val status = TextView(this)
        status.text = "● 服务状态：运行中"
        status.textSize = 20f

        val serverLabel = TextView(this)
        serverLabel.text = "服务器地址"

        val serverInput = EditText(this)
        serverInput.hint = "例如 http://127.0.0.1:8080"

        val keyLabel = TextView(this)
        keyLabel.text = "API Key"

        val keyInput = EditText(this)
        keyInput.hint = "请输入密钥"

        val button = Button(this)
        button.text = "保存配置"

        button.setOnClickListener {
            Toast.makeText(
                this,
                "配置已保存",
                Toast.LENGTH_SHORT
            ).show()
        }

        layout.addView(title)
        layout.addView(status)
        layout.addView(serverLabel)
        layout.addView(serverInput)
        layout.addView(keyLabel)
        layout.addView(keyInput)
        layout.addView(button)

        setContentView(layout)
    }
}
