package com.kelivo.bridge

import android.app.Activity
import android.os.Bundle
import android.graphics.Color
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.TextView

class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val layout = LinearLayout(this)

        layout.orientation = LinearLayout.VERTICAL
        layout.gravity = Gravity.CENTER
        layout.setPadding(40, 40, 40, 40)

        val title = TextView(this)
        title.text = "Kelivo Bridge"
        title.textSize = 32f
        title.gravity = Gravity.CENTER

        val status = TextView(this)
        status.text = "\n● 服务状态：运行中"
        status.textSize = 20f
        status.gravity = Gravity.CENTER

        val info = TextView(this)
        info.text = "\nAI 外接桥接服务\n等待连接 Kelivo"
        info.textSize = 16f
        info.gravity = Gravity.CENTER

        layout.addView(title)
        layout.addView(status)
        layout.addView(info)

        setContentView(layout)
    }
}
