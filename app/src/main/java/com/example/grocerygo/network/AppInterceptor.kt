package com.example.grocerygo.network

import com.example.grocerygo.data.PrefStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

class AppInterceptor constructor(private val prefsStore: PrefStore
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val builder = chain.request()
            .newBuilder()
        runBlocking {
            val accessToken = prefsStore.token().first()
            if (accessToken.isNotEmpty()) {
                builder.header("Authorization", "Bearer $accessToken")
            }
        }
        return chain.proceed(builder.build())
    }
}