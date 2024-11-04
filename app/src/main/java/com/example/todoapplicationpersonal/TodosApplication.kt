package com.example.todoapplicationpersonal

import android.app.Application
import android.util.Log
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.todoapplicationpersonal.di.component.ApplicationComponent
import com.example.todoapplicationpersonal.di.component.DaggerApplicationComponent
import com.example.todoapplicationpersonal.di.module.ApplicationModule
import com.example.todoapplicationpersonal.utils.NotifyWorker
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class TodosApplication : Application() {

    @Inject
    lateinit var applicationComponent: ApplicationComponent

    override fun onCreate() {
        injectDependencies()
        initializeTheWorkManager()
        super.onCreate()
    }

    private fun initializeTheWorkManager() {
        Log.d("###", "initializeTheWorkManager: workManager initialized")

        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
            .build()

        val myWorkRequest = PeriodicWorkRequestBuilder<NotifyWorker>(
            repeatInterval = 6,
            repeatIntervalTimeUnit = TimeUnit.HOURS
        )
            .setConstraints(constraints)
            .setBackoffCriteria(
                BackoffPolicy.EXPONENTIAL,
                15,
                TimeUnit.MINUTES
            )
            .setInitialDelay(15, TimeUnit.SECONDS)
            .build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            NotifyWorker.todosWork,
            ExistingPeriodicWorkPolicy.KEEP,
            myWorkRequest
        )
    }

    private fun injectDependencies() {
        applicationComponent = DaggerApplicationComponent.builder()
            .applicationModule(ApplicationModule(this))
            .build()
        applicationComponent.inject(this)
    }
}
