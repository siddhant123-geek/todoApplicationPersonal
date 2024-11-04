package com.example.todoapplicationpersonal.utils

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.todoapplicationpersonal.utils.AppConstants.SHARED_PREFS_NAME
import com.example.todoapplicationpersonal.utils.AppConstants.TASK_PENDING
import javax.inject.Inject


class NotifyWorker @Inject constructor(
    val context: Context,
    private val workerParams: WorkerParameters,
) : Worker(context, workerParams) {

    companion object {
        const val todosWork = "todosWork"
    }

    @SuppressLint("RestrictedApi")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun doWork(): Result {
        Log.d("###", "doWork: flow coming to this")
        try {
            val sharedPrefs = context.getSharedPreferences(
                SHARED_PREFS_NAME,
                Context.MODE_PRIVATE
            )
            val pendingTasks = sharedPrefs.getBoolean(
                TASK_PENDING,
                false
            )
            if (pendingTasks) {
                Log.d("###", "doWork: comimng to pending task in workManager")
                NotificationHelper.createNotificationChannel(applicationContext)
                val notification = NotificationHelper.createNotification(applicationContext)
                val notificationManager = NotificationManagerCompat.from(applicationContext)
                if (ActivityCompat.checkSelfPermission(
                        applicationContext,
                        Manifest.permission.POST_NOTIFICATIONS
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    return Result.failure()
                }
                notificationManager.notify(1, notification)
            }
            return Result.Success()
        } catch (e: Error) {
            return Result.Success()
        }
    }
}