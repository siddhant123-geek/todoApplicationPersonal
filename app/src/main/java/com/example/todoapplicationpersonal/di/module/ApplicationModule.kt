package com.example.todoapplicationpersonal.di.module

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModelProvider
import com.example.todoapplicationpersonal.data.NetworkService
import com.example.todoapplicationpersonal.di.ApplicationContext
import com.example.todoapplicationpersonal.di.BaseUrl
import com.example.todoapplicationpersonal.utils.AppConstants.BASE_URL
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class ApplicationModule(private val application: Application) {

    @Provides
    @ApplicationContext
    fun provideContext(): Context {
        return application
    }

    @Provides
    @BaseUrl
    fun provideBaseUrl(): String {
        return BASE_URL
    }

    @Provides
    @Singleton
    fun provideGsonConvertor(): GsonConverterFactory {
        return GsonConverterFactory.create()
    }

    @Provides
    @Singleton
    fun provideNetworkService(
        @BaseUrl baseUrl: String,
        gsonFactory: GsonConverterFactory
    ): NetworkService {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(gsonFactory)
            .build()
            .create(NetworkService::class.java)
    }
}