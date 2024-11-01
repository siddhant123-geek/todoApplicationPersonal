package com.example.todoapplicationpersonal.di.component

import com.example.todoapplicationpersonal.di.ActivityScope
import com.example.todoapplicationpersonal.di.module.ActivityModule
import com.example.todoapplicationpersonal.ui.TodosActivity
import dagger.Component

@ActivityScope
@Component(modules = [ActivityModule::class], dependencies = [ApplicationComponent::class])
interface ActivityComponent {

    fun inject(activity: TodosActivity)
}