package com.example.todoapplicationpersonal.di.module

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.todoapplicationpersonal.data.TodosRepository
import com.example.todoapplicationpersonal.di.ActivityContext
import com.example.todoapplicationpersonal.di.ActivityScope
import com.example.todoapplicationpersonal.ui.TodosAdapter
import com.example.todoapplicationpersonal.ui.viewModel.TodosViewModel
import com.example.todoapplicationpersonal.utils.ViewModelProviderFactory
import dagger.Module
import dagger.Provides

@Module
class ActivityModule(private val activity: AppCompatActivity) {

    @Provides
    @ActivityContext
    fun provideContext(): Context {
        return activity
    }

    @Provides
    @ActivityScope
    fun provideTodosViewModel(
        repo: TodosRepository
    ): TodosViewModel {
        return ViewModelProvider(
            activity,
            ViewModelProviderFactory(
                TodosViewModel::class
            ) { TodosViewModel(repo) }
        )[TodosViewModel::class.java]
    }

    @Provides
    @ActivityScope
    fun provideTodosAdapter(): TodosAdapter {
        return TodosAdapter(emptyList())
    }
}