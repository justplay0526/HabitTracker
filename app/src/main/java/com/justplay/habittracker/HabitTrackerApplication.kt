package com.justplay.habittracker

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class HabitTrackerApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        // 設定 Debug 模式
        setDebugMode()
    }

    /**
     * 設定 Debug 模式
     */
    private fun setDebugMode() {
        if (BuildConfig.DEBUG) {
            // 設定 Timber
            Timber.plant(tagTree)
        }
    }

    companion object {
        /**
         * Timber 的 TagTree
         */
        private val tagTree: Timber.Tree = object : Timber.DebugTree() {
            override fun createStackElementTag(element: StackTraceElement): String =
                "[${element.fileName}:${element.lineNumber}:${element.methodName}]"

            override fun log(priority: Int, message: String?, vararg args: Any?) {
                var maxLengthMessage = message
                message?.let { msg ->
                    val maxLength = 500

                    // 超過最大長度，就截斷其訊息
                    if (msg.length > maxLength) {
                        maxLengthMessage = msg.take(maxLength) + "..."
                    }
                }
                super.log(priority, maxLengthMessage, *args)
            }
        }
    }
}