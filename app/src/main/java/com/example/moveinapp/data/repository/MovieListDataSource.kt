package com.example.moveinapp.data.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.example.moveinapp.data.api.IMovieDataBase
import com.example.moveinapp.data.value_object.Result
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class MovieListDataSource (private val apiService : IMovieDataBase, private val compositeDisposable: CompositeDisposable)
    : PageKeyedDataSource<Int, Result>(){

    private var page = 1

    val networkState: MutableLiveData<NetworkState> = MutableLiveData()

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, Result>) {

        networkState.postValue(NetworkState.Loading)

        compositeDisposable.add(apiService.getPopularMovie(page).subscribeOn(Schedulers.io()).subscribe(
                    {
                        callback.onResult(it.results, null, page+1)
                        networkState.postValue(NetworkState.Loaded)
                    },
                    {
                        networkState.postValue(NetworkState.Error)
                        Log.e("MovieDataSource", it.message.toString())
                    }
                )
        )
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Result>) {
        networkState.postValue(NetworkState.Loading)

        compositeDisposable.add(apiService.getPopularMovie(params.key).subscribeOn(Schedulers.io()).subscribe(
                    {
                        if(it.totalPages >= params.key) {
                            callback.onResult(it.results, params.key+1)
                            networkState.postValue(NetworkState.Loaded)
                        }
                        else{
                            networkState.postValue(NetworkState.EndOfList)
                        }
                    },
                    {
                        networkState.postValue(NetworkState.Error)
                        Log.e("MovieDataSource", it.message.toString())
                    }
                )
        )
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Result>) {

    }
}