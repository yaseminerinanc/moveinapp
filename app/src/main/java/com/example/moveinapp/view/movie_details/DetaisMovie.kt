package com.example.moveinapp.view.movie_details

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.example.moveinapp.R
import com.example.moveinapp.data.api.IMovieDataBase
import com.example.moveinapp.data.api.MovieDataBaseClient
import com.example.moveinapp.data.repository.NetworkState
import com.example.moveinapp.data.value_object.MovieDetails
import kotlinx.android.synthetic.main.activity_detais_movie.*



class DetaisMovie : AppCompatActivity() {

    private lateinit var viewModel: MovieViewModel
    private lateinit var movieRepo: MovieDetailsRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detais_movie)

        val movieId: Int = intent.getIntExtra("id", 1)
        val apiService: IMovieDataBase = MovieDataBaseClient.getClient()

        movieRepo = MovieDetailsRepository(apiService)
        viewModel = getViewModel(movieId)
        viewModel.movieDetails.observe(this, Observer { bindUI(it) })

        viewModel.networkState.observe(this, Observer {
            pb.visibility = if (it == NetworkState.Loading) View.VISIBLE else View.GONE
            text_error.visibility = if (it == NetworkState.Error) View.VISIBLE else View.GONE
        })
    }

         fun bindUI(it: MovieDetails) {

            mv_dt_title.text = it.title
            mv_dt_subtitle.text = it.tagline
            mv_dt_releasedate.text = it.releaseDate
            mv_dt_overview.text = it.overview

            val moviePosterURL = "https://image.tmdb.org/t/p/w500" + it.posterPath
            Glide.with(this).load(moviePosterURL).into(mv_dt_foto);

    }

    private fun getViewModel(movieId: Int): MovieViewModel {

        return ViewModelProviders.of(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return MovieViewModel(movieRepo,movieId) as T
            }
        })[MovieViewModel::class.java]
    }

}