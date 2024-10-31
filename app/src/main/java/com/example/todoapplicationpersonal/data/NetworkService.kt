package com.example.todoapplicationpersonal.data

import com.example.todoapplicationpersonal.data.models.QuoteItem
import com.example.todoapplicationpersonal.data.models.QuoteResponse
import com.example.todoapplicationpersonal.utils.AppConstants.API_KEY
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface NetworkService {

    @Headers("X-Api-Key: $API_KEY")
    @GET
    fun getQuote(@Query("category") category: String): QuoteResponse
}