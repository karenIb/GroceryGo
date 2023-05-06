package com.example.grocerygo.network

import com.example.grocerygo.data.PrefStore
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit

class NetworkInterface {


    private val client =  OkHttpClient.Builder()
    .callTimeout(30, TimeUnit.SECONDS)
    .connectTimeout(30, TimeUnit.SECONDS)
    .readTimeout(30, TimeUnit.SECONDS)
    .writeTimeout(30, TimeUnit.SECONDS)
    .addInterceptor(AppInterceptor(PrefStore.getInstance()))
    .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
    .build()

    val retrofit = Retrofit.Builder()
    .addConverterFactory(GsonConverterFactory.create(Gson()))
    .addConverterFactory(ScalarsConverterFactory.create())
    .client(client)
    .baseUrl(Endpoints.host)
    .build()


}