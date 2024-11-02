package com.example.todoapplicationpersonal.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.lang.IllegalArgumentException
import kotlin.reflect.KClass

class ViewModelProviderFactory<T: ViewModel>(
    private val kClass: KClass<T>,
    private val creator: () -> T
): ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    @Throws(IllegalArgumentException::class)
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(kClass.java)) return creator.invoke() as T
        else throw IllegalArgumentException("Unknown class name")
    }
}