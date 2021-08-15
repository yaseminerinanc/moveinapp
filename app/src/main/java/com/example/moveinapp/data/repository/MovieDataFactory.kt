package com.example.moveinapp.data.repository

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.example.moveinapp.data.api.IMovieDataBase
import com.example.moveinapp.data.value_object.Result
import io.reactivex.disposables.CompositeDisposable

class MovieDataFactory (private val apiService : IMovieDataBase, private val compositeDisposable: CompositeDisposable)
    : DataSource.Factory<Int, Result>() {

    val moviesLiveDataSource =  MutableLiveData<MovieListDataSource>()

    override fun create(): DataSource<Int, Result> {
        val movieDataSource = MovieListDataSource(apiService,compositeDisposable)

        moviesLiveDataSource.postValue(movieDataSource)
        return movieDataSource
    }
}