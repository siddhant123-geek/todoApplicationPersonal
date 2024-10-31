package com.example.todoapplicationpersonal.data

import com.google.gson.annotations.SerializedName

data class QuoteItem (
    @SerializedName("quote")
    val quote: String,
    @SerializedName("author")
    val author: String
)