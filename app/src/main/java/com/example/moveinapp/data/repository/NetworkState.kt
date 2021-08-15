package com.example.moveinapp.data.repository


enum class Status{
    Running,
    Success,
    Failed
}
class NetworkState (val status:Status , val message:String) {

    companion object{

        val Loaded:NetworkState
        val Loading:NetworkState
        val Error:NetworkState
        val EndOfList: NetworkState

        init{

            Loaded= NetworkState(Status.Success,"Success")
            Loading= NetworkState(Status.Running,"Running")
            Error= NetworkState(Status.Failed,"There is a problem occured.")
            EndOfList=NetworkState(Status.Failed, "You have reached the end")
        }
    }
}