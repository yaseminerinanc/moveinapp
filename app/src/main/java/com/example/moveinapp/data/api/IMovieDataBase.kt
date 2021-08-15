package com.example.moveinapp.data.api

import com.example.moveinapp.data.value_object.MovieDetails
import com.example.moveinapp.data.value_object.MovieResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface IMovieDataBase {

        @GET("movie/{movie_id}")
        fun getMovieDetails(@Path("movie_id") id:Int): Single<MovieDetails>
        @GET("discover/movie")
        fun getPopularMovie(@Query("page") page: Int): Single<MovieResponse>

}