package com.example.todoapplicationpersonal.di.component

import android.app.Application
import android.content.Context
import com.example.todoapplicationpersonal.data.NetworkService
import com.example.todoapplicationpersonal.data.TodosRepository
import com.example.todoapplicationpersonal.di.ApplicationContext
import com.example.todoapplicationpersonal.di.module.ApplicationModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ApplicationModule::class])
interface ApplicationComponent {

    fun inject(application: Application)

    @ApplicationContext
    fun getContext(): Context

    fun getRepo(): TodosRepository

    fun getNetworkService(): NetworkService

}