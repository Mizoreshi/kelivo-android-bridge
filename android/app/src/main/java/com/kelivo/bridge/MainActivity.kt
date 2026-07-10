package com.kelivo.bridge

import android.app.Activity
import android.os.Bundle
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.widget.TextView

class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val text = TextView(this)
        text.text = "Kelivo Bridge\n\n正在运行"
        text.textSize = 24f
        text.setPadding(40, 80, 40, 40)
        setContentView(text)

        val manager = getSystemService(Context.USAGE_STATS_SERVICE)

        if (manager is UsageStatsManager) {

            val stats = manager.queryUsageStats(
                UsageStatsManager.INTERVAL_DAILY,
                0,
                System.currentTimeMillis()
            )

            if (stats == null || stats.isEmpty()) {
                startActivity(
                    Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
                )
            }
        }
    }
}
