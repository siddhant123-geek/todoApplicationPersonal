package com.example.todoapplicationpersonal.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoapplicationpersonal.data.TodosRepository
import com.example.todoapplicationpersonal.data.models.QuoteItem
import com.example.todoapplicationpersonal.data.models.TodoItem
import com.example.todoapplicationpersonal.utils.UiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TodosViewModel(private val repo: TodosRepository): ViewModel() {

    private val _uiStateQuote: MutableStateFlow<UiState<QuoteItem>> = MutableStateFlow(UiState.Loading)

    val uiStateQuote: StateFlow<UiState<QuoteItem>> = _uiStateQuote

    private val _uiStateTodos: MutableStateFlow<UiState<List<TodoItem>>> = MutableStateFlow(UiState.Loading)

    val uiStateTodos: StateFlow<UiState<List<TodoItem>>> = _uiStateTodos

    init {
        fetchTodoItems()
        fetchQuote()
    }

    private fun fetchQuote() {
        viewModelScope.launch {
            _uiStateQuote.value = UiState.Loading
            repo.fetchQuote()
                .catch {
                    _uiStateQuote.value = UiState.Error(it.message.toString())
                }
                .collect {
                    _uiStateQuote.value = UiState.Success(it)
                }
        }
    }

    private fun fetchTodoItems() {
        viewModelScope.launch {
            _uiStateQuote.value = UiState.Loading
            withContext(Dispatchers.IO) {
                repo.fetchTodos()
                    .catch {
                        _uiStateTodos.value = UiState.Error(it.message.toString())
                    }
                    .collect {
                        _uiStateTodos.value = UiState.Success(it)
                    }
            }
        }
    }

    suspend fun addTodoItem(todo: TodoItem) {
        repo.addTodo(todo)
    }

    suspend fun deleteAllTodos() {
        repo.deleteAllTodos()
    }

    suspend fun deleteTodo(id: Long) {
        repo.deleteTodo(id)
    }

    suspend fun updateTodo(todoId: Long, isChecked: Boolean) {
        repo.updateTodo(todoId, isChecked)
    }
}