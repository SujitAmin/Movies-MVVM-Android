@file:Suppress("DEPRECATION")

package com.example.in10.ui.listofmovies

import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.example.in10.R
import kotlinx.android.synthetic.main.activity_main.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.in10.Model.TheMovieDBClient
import com.example.in10.Model.TheMovieDBInterface
import com.example.in10.repository.NetworkState
import com.example.in10.ui.listofmovies.adapter.PopularMoviePagedListAdapter
import com.example.in10.viewModel.MainActivityViewModel

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: MainActivityViewModel

    lateinit var movieRepository: MoviePagedListRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val movieAdapter = PopularMoviePagedListAdapter(this)
        val linearLayoutManager = LinearLayoutManager(this)

        setApiServiceRepositoryAndViewModel()
        initializeAndSetRecyclerView(movieAdapter,linearLayoutManager)
        addObservers(movieAdapter)


    }

    private fun setApiServiceRepositoryAndViewModel() {
        val apiService : TheMovieDBInterface = TheMovieDBClient.getClient()
        movieRepository = MoviePagedListRepository(apiService)
        viewModel = getViewModel()
    }

    private fun initializeAndSetRecyclerView(movieAdapter: PopularMoviePagedListAdapter, linearLayoutManager: LinearLayoutManager) {
        rv_movie_list.layoutManager = linearLayoutManager
        rv_movie_list.setHasFixedSize(true)
        rv_movie_list.adapter = movieAdapter
        rv_movie_list.addItemDecoration(DividerItemDecoration(this@MainActivity, LinearLayoutManager.VERTICAL))
    }

    private fun addObservers(movieAdapter: PopularMoviePagedListAdapter) {
        viewModel.moviePagedList.observe(this, Observer {
            movieAdapter.submitList(it)
        })

        viewModel.networkState.observe(this, Observer {
            progress_bar_popular.visibility = if (viewModel.listIsEmpty() && it == NetworkState.LOADING) View.VISIBLE else View.GONE
            txt_error_popular.visibility = if (viewModel.listIsEmpty() && it == NetworkState.ERROR) View.VISIBLE else View.GONE

            if (!viewModel.listIsEmpty()) {
                movieAdapter.setNetworkState(it)
            }
        })
    }

    private fun getViewModel(): MainActivityViewModel {
        return ViewModelProviders.of(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return MainActivityViewModel(movieRepository) as T
            }
        })[MainActivityViewModel::class.java]
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        if(menu != null) {
            for (i in 0 until menu.size()) {
                val menuItem = menu.getItem(i)
                val icon = menuItem.icon
                if (icon != null) {
                    DrawableCompat.setTint(icon, ContextCompat.getColor(this, R.color.yellowChrome))
                }
            }
        }
        return super.onCreateOptionsMenu(menu)
    }
}