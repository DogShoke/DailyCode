package com.example.dailycode

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.Data

class CouponReminderWorker(context: Context, workerParams: WorkerParameters) :
    Worker(context, workerParams) {

    override fun doWork(): Result {
        val type = inputData.getString("type") ?: "morning"

        val (title, message) = when (type) {
            "morning" -> "Новый купон доступен!" to "Зайди в приложение и посмотри купон на сегодня."
            "evening" -> "Не забудь забрать купон!" to "Успей воспользоваться сегодняшним купоном."
            else -> "Уведомление" to "Проверь приложение."
        }

        NotificationHelper(applicationContext).showNotification(title, message)
        return Result.success()
    }
}