package com.example.todoapplicationpersonal.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoapplicationpersonal.data.QuotesRepository
import com.example.todoapplicationpersonal.data.models.QuoteItem
import com.example.todoapplicationpersonal.data.models.QuoteResponse
import com.example.todoapplicationpersonal.utils.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class TodosViewModel(private val repo: QuotesRepository): ViewModel() {

    private val _uiState: MutableStateFlow<UiState<QuoteItem>> = MutableStateFlow(UiState.Loading)

    val uiState: StateFlow<UiState<QuoteItem>> = _uiState

    init {
        fetchQuotes()
    }

    private fun fetchQuotes() {
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

}