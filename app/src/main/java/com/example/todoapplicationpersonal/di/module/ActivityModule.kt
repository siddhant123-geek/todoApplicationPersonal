package com.example.todoapplicationpersonal.di.module

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.example.todoapplicationpersonal.di.ActivityContext
import dagger.Module
import dagger.Provides

@Module
class ActivityModule(private val activity: AppCompatActivity) {

    @Provides
    @ActivityContext
    fun provideContext(): Context {
        return activity
    }

}