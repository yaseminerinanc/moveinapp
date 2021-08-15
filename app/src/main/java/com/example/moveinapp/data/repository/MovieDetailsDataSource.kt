package com.example.moveinapp.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.moveinapp.data.api.IMovieDataBase
import com.example.moveinapp.data.value_object.MovieDetails
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class MovieDetailsDataSource (private val apiService : IMovieDataBase, private val compositeDisposable: CompositeDisposable) {

    private val _networkState = MutableLiveData<NetworkState>()
    val networkState: LiveData<NetworkState>
        get() = _networkState

    private val _movieDetailResponse = MutableLiveData<MovieDetails>()
    val movieDetailResponse: LiveData<MovieDetails>
        get() = _movieDetailResponse

    fun fetchMovieDetails(movieId:Int) {

        _networkState.postValue(NetworkState.Loading)

        try {
            compositeDisposable.add(apiService.getMovieDetails(movieId).subscribeOn(Schedulers.io())
                .subscribe(
                    {
                        _movieDetailResponse.postValue(it)
                        _networkState.postValue(NetworkState.Loaded)
                    },
                    {
                        _networkState.postValue((NetworkState.Error))
                        Log.e("MovieDetailsDataSource", it.message.toString())

                    }
                )

            )
        } catch (e: Exception) {

            Log.e("MovieDetailsDataSource", e.message.toString())
        }
    }
}