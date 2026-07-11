package com.kelivo.bridge.tools

import android.app.usage.UsageStatsManager
import android.content.Context
import java.util.Calendar

class ScreenTimeTool(private val context: Context) {

    private fun formatTime(minutes: Long): String {

        val hour = minutes / 60
        val minute = minutes % 60

        return if (hour > 0) {
            "${hour}小时${minute}分钟"
        } else {
            "${minute}分钟"
        }
    }


    private fun getAppName(packageName: String): String {

        return when(packageName) {

            "com.ss.android.ugc.aweme" -> "抖音"

            "com.tencent.mm" -> "微信"

            "com.tencent.mobileqq" -> "QQ"

            "com.android.chrome" -> "Chrome"

            else -> packageName
        }
    }


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
                "${getAppName(app.first)}  ${formatTime(app.second)}\n"
            )
        }


        return result.toString()
    }
}
