package com.example.in10.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.example.in10.Model.response.Result
import com.example.in10.repository.NetworkState
import com.example.in10.ui.listofmovies.MoviePagedListRepository
import io.reactivex.disposables.CompositeDisposable

class MainActivityViewModel(private val movieRepository : MoviePagedListRepository) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    val  moviePagedList : LiveData<PagedList<Result>> by lazy {
        movieRepository.fetchLiveMoviePagedList(compositeDisposable)
    }

    val  networkState : LiveData<NetworkState> by lazy {
        movieRepository.getNetworkState()
    }

    fun listIsEmpty(): Boolean {
        return moviePagedList.value?.isEmpty() ?: true
    }


    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

}