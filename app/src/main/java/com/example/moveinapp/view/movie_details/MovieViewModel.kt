package com.example.moveinapp.view.movie_details

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.moveinapp.data.repository.NetworkState
import com.example.moveinapp.data.value_object.MovieDetails
import io.reactivex.disposables.CompositeDisposable

class MovieViewModel (private val movieRepo :MovieDetailsRepository,movieId:Int): ViewModel() {

    private val compDisposible = CompositeDisposable()

    val movieDetails : LiveData<MovieDetails> by lazy {

        movieRepo.fetchAMovieDetail(compDisposible,movieId)
    }
    val networkState : LiveData<NetworkState> by lazy {

        movieRepo.getMovieDetailsNetworkState()
    }

    override fun onCleared(){
        super.onCleared()
        compDisposible.dispose()
    }
}