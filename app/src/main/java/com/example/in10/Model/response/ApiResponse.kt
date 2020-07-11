package com.example.in10.Model.response


import com.google.gson.annotations.SerializedName

data class ApiResponse(
    val dates: Dates,
    val page: Int,
    val results: List<Result>,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("total_results")
    val totalResults: Int
)