package com.example.todoapplicationpersonal.utils

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.todoapplicationpersonal.R
import com.example.todoapplicationpersonal.utils.AppConstants.NOTIFICATION_CHANNEL_ID
import com.example.todoapplicationpersonal.utils.AppConstants.NOTIFICATION_CHANNEL_NAME

object NotificationHelper {

    @RequiresApi(Build.VERSION_CODES.O)
    fun createNotificationChannel(context: Context) {
        val notificationChannel = NotificationChannel(NOTIFICATION_CHANNEL_ID,
                                                      NOTIFICATION_CHANNEL_NAME,
                                                      NotificationManager.IMPORTANCE_DEFAULT)
        val notificationManager = context.getSystemService(NotificationManager::class.java)
        notificationManager.createNotificationChannel(notificationChannel)
    }

    fun createNotification(context: Context): Notification {
        val builder = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("Pending Tasks")
            .setContentText("Hi, you have some pending tasks. Let us complete them")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        return builder.build()
    }
}