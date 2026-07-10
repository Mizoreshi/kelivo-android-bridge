package com.kelivo.bridge

import android.app.Activity
import android.os.Bundle
import android.widget.TextView

class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = TextView(this)
        view.text = "Kelivo Bridge\n启动成功"
        view.textSize = 30f

        setContentView(view)
    }
}
