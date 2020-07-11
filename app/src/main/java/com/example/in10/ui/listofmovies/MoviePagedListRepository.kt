package com.example.in10.ui.listofmovies

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.in10.Model.Constants.POST_PER_PAGE
import com.example.in10.Model.TheMovieDBInterface
import com.example.in10.Model.response.Result
import com.example.in10.repository.MovieDataSource
import com.example.in10.repository.MovieDataSourceFactory
import com.example.in10.repository.NetworkState
import io.reactivex.disposables.CompositeDisposable

class MoviePagedListRepository (private val apiService : TheMovieDBInterface) {

    private lateinit var moviePagedList: LiveData<PagedList<Result>>
    lateinit var moviesDataSourceFactory: MovieDataSourceFactory

    fun fetchLiveMoviePagedList (compositeDisposable: CompositeDisposable) : LiveData<PagedList<Result>> {
        moviesDataSourceFactory = MovieDataSourceFactory(apiService, compositeDisposable)

        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(POST_PER_PAGE)
            .build()

        moviePagedList = LivePagedListBuilder(moviesDataSourceFactory, config).build()

        return moviePagedList
    }

    fun getNetworkState(): LiveData<NetworkState> {
        return Transformations.switchMap<MovieDataSource, NetworkState>(
            moviesDataSourceFactory.moviesLiveDataSource, MovieDataSource::networkState)
    }
}