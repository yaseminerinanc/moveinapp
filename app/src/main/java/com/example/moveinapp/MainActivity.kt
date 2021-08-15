package com.example.moveinapp

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.example.moveinapp.data.api.IMovieDataBase
import com.example.moveinapp.data.api.MovieDataBaseClient
import com.example.moveinapp.data.repository.NetworkState
import com.example.moveinapp.view.movie_list.MainActivityViewModel
import com.example.moveinapp.view.movie_list.MovieListAdapter
import com.example.moveinapp.view.movie_list.MovieListRepository
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel:MainActivityViewModel
    lateinit var movieRepository:MovieListRepository

            override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

                val apiService :IMovieDataBase =MovieDataBaseClient.getClient()
                movieRepository= MovieListRepository(apiService)

                viewModel=getViewModel()

                val movieAdapter = MovieListAdapter(this)

                val gridLayoutManager = GridLayoutManager(this, 3)

                gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                    override fun getSpanSize(position: Int): Int {
                        val viewType = movieAdapter.getItemViewType(position)
                        if (viewType == 2) return  1
                        else return 3
                    }
                };


                rv_movie_list.layoutManager = gridLayoutManager
                rv_movie_list.setHasFixedSize(true)
                rv_movie_list.adapter = movieAdapter

                viewModel.movieList.observe(this, Observer {
                    movieAdapter.submitList(it)
                })

                viewModel.networkState.observe(this, Observer {
                    pb_disvover.visibility = if (viewModel.listIsEmpty() && it == NetworkState.Loading) View.VISIBLE else View.GONE
                    txt_error_popular.visibility = if (viewModel.listIsEmpty() && it == NetworkState.Error) View.VISIBLE else View.GONE

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
}