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
            emit(networkService.getQuote(CATEGORY)[0])
        }
    }

    suspend fun fetchTodos(): Flow<List<TodoItem>> {
        return database.todosDao().getAllTodos()

    }

    suspend fun addTodo(todo: TodoItem) {
        database.todosDao().insertTodo(todo)
    }

    suspend fun deleteAllTodos() {
        database.todosDao().deleteTodos()
    }

    suspend fun deleteTodo(todoId: Long) {
        database.todosDao().deleteTodos(todoId)
    }

    suspend fun updateTodo(todoId: Long, isChecked: Boolean) {
        database.todosDao().updateTodoStatus(todoId, isChecked)
    }
}