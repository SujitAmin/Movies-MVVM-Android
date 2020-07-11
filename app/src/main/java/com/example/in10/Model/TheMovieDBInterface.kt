package com.example.in10.Model

import com.example.in10.Model.response.ApiResponse
import com.example.in10.Model.response.Result
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TheMovieDBInterface {
    @GET("movie/popular")
    fun getPopular(@Query("page") page: Int): Single<ApiResponse>

    @GET("movie/{movie_id}")
    fun getMovieDetails(@Path("movie_id") id: Int): Single<Result>
}