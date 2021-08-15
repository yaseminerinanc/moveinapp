package com.example.moveinapp.view.movie_list

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.moveinapp.data.api.IMovieDataBase
import com.example.moveinapp.data.repository.MovieDataFactory
import com.example.moveinapp.data.repository.MovieListDataSource
import com.example.moveinapp.data.repository.NetworkState
import com.example.moveinapp.data.value_object.Result
import io.reactivex.disposables.CompositeDisposable

@Suppress("DEPRECATION")
class MovieListRepository  (private val apiService : IMovieDataBase) {

    lateinit var movieList: LiveData<PagedList<Result>>
    lateinit var movieListDataFactory: MovieDataFactory

    fun fetchMovieList (compositeDisposable: CompositeDisposable) : LiveData <PagedList<Result>>{

        movieListDataFactory=MovieDataFactory(apiService, compositeDisposable)

        val config = PagedList.Config.Builder().setEnablePlaceholders(false).setPageSize(10).build()

        movieList = LivePagedListBuilder(movieListDataFactory, config).build()

        return movieList
    }

    fun getNetworkState(): LiveData<NetworkState> {
        return Transformations.switchMap<MovieListDataSource, NetworkState>(
            movieListDataFactory.moviesLiveDataSource, MovieListDataSource::networkState)


    }
}