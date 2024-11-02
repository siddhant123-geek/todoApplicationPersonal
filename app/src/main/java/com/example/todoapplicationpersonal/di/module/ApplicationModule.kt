package com.example.todoapplicationpersonal.di.module

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.todoapplicationpersonal.data.NetworkService
import com.example.todoapplicationpersonal.data.TodosDatabase
import com.example.todoapplicationpersonal.di.ActivityContext
import com.example.todoapplicationpersonal.di.ActivityScope
import com.example.todoapplicationpersonal.di.ApplicationContext
import com.example.todoapplicationpersonal.di.BaseUrl
import com.example.todoapplicationpersonal.di.DbName
import com.example.todoapplicationpersonal.utils.AppConstants.BASE_URL
import com.example.todoapplicationpersonal.utils.AppConstants.DB_NAME
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
    @DbName
    fun provideDbName(): String {
        return DB_NAME
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

    @Provides
    @Singleton
    fun provideDatabaseService(
        @ApplicationContext context: Context,
        @DbName dbName: String
    ): TodosDatabase {
        return Room.databaseBuilder(
            context,
            TodosDatabase::class.java,
            dbName
        ).build()
    }
}