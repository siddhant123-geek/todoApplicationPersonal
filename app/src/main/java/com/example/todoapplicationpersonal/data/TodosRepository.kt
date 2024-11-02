package com.example.todoapplicationpersonal.data

import com.example.todoapplicationpersonal.data.models.QuoteItem
import com.example.todoapplicationpersonal.data.models.TodoItem
import com.example.todoapplicationpersonal.utils.AppConstants.CATEGORY
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TodosRepository @Inject constructor(private val networkService: NetworkService,
                                          private val database: TodosDatabase) {

    fun fetchQuote(): Flow<QuoteItem> {
        return flow {
            emit(networkService.getQuote(CATEGORY))
            }
            .map {
                it.listOfQuotes[0]
            }
    }

    fun fetchTodos(): Flow<List<TodoItem>> {
        return flow {
            emit(database.todosDao().getAllTodos())
        }
    }
}