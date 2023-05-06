package com.example.grocerygo.network

sealed class Resource<T:AResponse>(val data: T? = null, val message:String?= null) {
    class Success<T:AResponse>(data: T): Resource<T>(data)
    class NotStarted<T:AResponse>() : Resource<T>()
    class Error<T:AResponse>(eMessage:String) : Resource<T>(message = eMessage)
    class Loading<T:AResponse> : Resource<T>()
}