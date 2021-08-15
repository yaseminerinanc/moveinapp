package com.example.moveinapp.data.api

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


//https://api.themoviedb.org/3/discover/movie?api_key=3bb3e67969473d0cb4a48a0dd61af747&page=1
//https://image.tmdb.org/t/p/w500//5bFK5d3mVTAvBCXi5NPWH0tYjKl.jpg

const val BASE_URL="https://api.themoviedb.org/3/"
const val API_KEY="3bb3e67969473d0cb4a48a0dd61af747"

object MovieDataBaseClient {

    fun getClient():IMovieDataBase{
        val requestInt = Interceptor{chain ->

            val url=chain.request().url().newBuilder().addQueryParameter("api_key", API_KEY).build()

            val request=chain.request().newBuilder().url(url).build()

            return@Interceptor chain.proceed(request)
        }

        val okHttpClient= OkHttpClient.Builder().addInterceptor(requestInt).connectTimeout(60, TimeUnit.SECONDS).build()

        return Retrofit.Builder().client(okHttpClient).baseUrl(BASE_URL).addCallAdapterFactory(RxJava2CallAdapterFactory.create())
             .addConverterFactory(GsonConverterFactory.create()).build().create(IMovieDataBase::class.java)


    }
}