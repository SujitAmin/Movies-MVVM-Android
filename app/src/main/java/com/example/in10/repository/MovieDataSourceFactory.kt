package com.example.in10.repository

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.example.in10.Model.TheMovieDBInterface
import com.example.in10.Model.response.Result
import io.reactivex.disposables.CompositeDisposable

class MovieDataSourceFactory (private val apiService : TheMovieDBInterface, private val compositeDisposable: CompositeDisposable)
    : DataSource.Factory<Int, Result>() {

    val moviesLiveDataSource =  MutableLiveData<MovieDataSource>()
    override fun create(): DataSource<Int, Result> {
        val movieDataSource = MovieDataSource(apiService,compositeDisposable)
        moviesLiveDataSource.postValue(movieDataSource)
        return movieDataSource
    }
}