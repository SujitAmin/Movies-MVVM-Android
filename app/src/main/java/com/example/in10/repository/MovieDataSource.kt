package com.example.in10.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.example.in10.Model.Constants.FIRST_PAGE
import com.example.in10.Model.TheMovieDBInterface
import com.example.in10.Model.response.Result
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class MovieDataSource (private val apiService : TheMovieDBInterface, private val compositeDisposable: CompositeDisposable)
    : PageKeyedDataSource<Int, Result>(){
    private var page = FIRST_PAGE
    val networkState: MutableLiveData<NetworkState> = MutableLiveData()

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, Result>) {

        networkState.postValue(NetworkState.LOADING)
        compositeDisposable.add(
            apiService.getPopular(page)
                .subscribeOn(Schedulers.io())
                .subscribe(
                    {
                        callback.onResult(it.results, null, page+1)
                        networkState.postValue(NetworkState.LOADED)
                    },
                    {
                        networkState.postValue(NetworkState.ERROR)
                        Log.e("MovieDataSource", it.message)
                    }
                )
        )
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Result>) {
        networkState.postValue(NetworkState.LOADING)
        compositeDisposable.add(
            apiService.getPopular(params.key)
                .subscribeOn(Schedulers.io())
                .subscribe(
                    {
                        if(it.totalPages >= params.key) {
                            callback.onResult(it.results, params.key+1)
                            networkState.postValue(NetworkState.LOADED)
                        }
                        else{
                            networkState.postValue(NetworkState.ENDOFLIST)
                        }
                    },
                    {
                        networkState.postValue(NetworkState.ERROR)
                        Log.e("MovieDataSource", it.message)
                    }
                )
        )
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Result>) {
        //not needed
    }
}