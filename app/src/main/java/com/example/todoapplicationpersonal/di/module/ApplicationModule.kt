package com.example.todoapplicationpersonal.di.module

import android.app.Application
import android.content.Context
import com.example.todoapplicationpersonal.di.ApplicationContext
import dagger.Module
import dagger.Provides

@Module
class ApplicationModule(private val application: Application) {

    @Provides
    @ApplicationContext
    fun provideContext(): Context {
        return application
    }
}