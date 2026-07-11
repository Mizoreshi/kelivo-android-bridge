package com.kelivo.bridge.tools

import android.app.usage.UsageStatsManager
import android.content.Context
import java.util.Calendar

class ScreenTimeTool(private val context: Context) {

    fun getTodayUsage(): String {

        val usageStatsManager =
            context.getSystemService(
                Context.USAGE_STATS_SERVICE
            ) as UsageStatsManager

        val calendar = Calendar.getInstance()

        calendar.set(
            Calendar.HOUR_OF_DAY,
            0
        )
        calendar.set(
            Calendar.MINUTE,
            0
        )
        calendar.set(
            Calendar.SECOND,
            0
        )

        val stats =
            usageStatsManager.queryUsageStats(
                UsageStatsManager.INTERVAL_DAILY,
                calendar.timeInMillis,
                System.currentTimeMillis()
            )

        if (stats.isNullOrEmpty()) {
            return "没有获取到使用数据"
        }

        val result = StringBuilder()

        for (item in stats) {

            val minutes =
                item.totalTimeInForeground / 60000

            if (minutes > 0) {

                result.append(
                    "${item.packageName}: ${minutes}分钟\n"
                )
            }
        }

        return result.toString()
    }
}
