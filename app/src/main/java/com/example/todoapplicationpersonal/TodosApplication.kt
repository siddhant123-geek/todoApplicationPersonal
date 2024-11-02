package com.example.todoapplicationpersonal

import android.app.Application
import com.example.todoapplicationpersonal.di.component.ApplicationComponent
import com.example.todoapplicationpersonal.di.component.DaggerApplicationComponent
import com.example.todoapplicationpersonal.di.module.ApplicationModule
import javax.inject.Inject

class TodosApplication: Application() {

    @Inject
    lateinit var applicationComponent: ApplicationComponent

    override fun onCreate() {
        injectDependencies()
        super.onCreate()
    }

    private fun injectDependencies() {
        applicationComponent = DaggerApplicationComponent.builder()
            .applicationModule(ApplicationModule(this))
            .build()
        applicationComponent.inject(this)
    }
}
