package com.example.grocerygo.ui.splash

import androidx.lifecycle.ViewModel
import com.example.grocerygo.data.PrefStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.runBlocking

class SplashViewModel: ViewModel() {

    private val prefStore = PrefStore.getInstance()


    suspend fun isFirst() = prefStore.isFirst().first()

    suspend fun setIsFirst() = prefStore.setIsFirst()
}