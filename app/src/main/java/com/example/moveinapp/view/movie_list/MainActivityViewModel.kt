package com.example.moveinapp.view.movie_list

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.example.moveinapp.data.repository.NetworkState
import com.example.moveinapp.data.value_object.Result
import io.reactivex.disposables.CompositeDisposable

class MainActivityViewModel(private val movieRepository:MovieListRepository):ViewModel() {

    private val compositeDisposable=CompositeDisposable()

    val movieList :LiveData <PagedList<Result>> by lazy {
        movieRepository.fetchMovieList(compositeDisposable)
    }

    val networkState :LiveData<NetworkState> by lazy {
        movieRepository.getNetworkState()
    }

    fun listIsEmpty(): Boolean {
        return movieList.value?.isEmpty()?:true
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}