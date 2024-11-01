package com.example.todoapplicationpersonal.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoapplicationpersonal.data.TodosRepository
import com.example.todoapplicationpersonal.data.models.QuoteItem
import com.example.todoapplicationpersonal.utils.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class TodosViewModel(private val repo: TodosRepository): ViewModel() {

    private val _uiState: MutableStateFlow<UiState<QuoteItem>> = MutableStateFlow(UiState.Loading)

    val uiState: StateFlow<UiState<QuoteItem>> = _uiState

    init {
        fetchTodoItems()
        fetchQuote()
    }

    private fun fetchQuote() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            repo.fetchQuote()
                .catch {
                    _uiState.value = UiState.Error(it.message.toString())
                }
                .collect {
                    _uiState.value = UiState.Success(it)
                }
        }
    }

    private fun fetchTodoItems() {

    }

}