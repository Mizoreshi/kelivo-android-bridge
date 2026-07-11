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

        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)

        val stats =
            usageStatsManager.queryUsageStats(
                UsageStatsManager.INTERVAL_DAILY,
                calendar.timeInMillis,
                System.currentTimeMillis()
            )

        if (stats.isNullOrEmpty()) {
            return "没有获取到使用数据"
        }

        val apps = stats.map {
            Pair(
                it.packageName,
                it.totalTimeInForeground / 60000
            )
        }
            .filter {
                it.second > 0
            }
            .sortedByDescending {
                it.second
            }
            .take(10)


        if (apps.isEmpty()) {
            return "今天没有使用记录"
        }


        val result = StringBuilder()

        result.append("今日屏幕使用时间\n\n")


        for (app in apps) {

            result.append(
                "${app.first}  ${app.second}分钟\n"
            )
        }


        return result.toString()
    }
}
