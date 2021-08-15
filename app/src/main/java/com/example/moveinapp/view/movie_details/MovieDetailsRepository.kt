package com.example.moveinapp.view.movie_details

import androidx.lifecycle.LiveData
import com.example.moveinapp.data.api.IMovieDataBase
import com.example.moveinapp.data.repository.MovieDetailsDataSource
import com.example.moveinapp.data.repository.NetworkState
import com.example.moveinapp.data.value_object.MovieDetails
import io.reactivex.disposables.CompositeDisposable

class MovieDetailsRepository (private val apiService: IMovieDataBase){

    lateinit var movDetDataSrc : MovieDetailsDataSource

    fun fetchAMovieDetail(compositeDisposable: CompositeDisposable, movieId:Int): LiveData<MovieDetails> {

        movDetDataSrc= MovieDetailsDataSource(apiService,compositeDisposable)
        movDetDataSrc.fetchMovieDetails(movieId)

        return movDetDataSrc.movieDetailResponse
    }

    fun getMovieDetailsNetworkState(): LiveData<NetworkState> {

        return movDetDataSrc.networkState
    }
}