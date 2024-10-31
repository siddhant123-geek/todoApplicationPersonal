package com.example.todoapplicationpersonal.data

import com.example.todoapplicationpersonal.data.models.QuoteItem
import com.example.todoapplicationpersonal.utils.AppConstants.CATEGORY
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class QuotesRepository @Inject constructor(private val networkService: NetworkService) {

    fun fetchQuote(): Flow<QuoteItem> {
        return flow {
            emit(networkService.getQuote(CATEGORY))
            }
            .map {
                it.listOfQuotes[0]
            }
    }
}