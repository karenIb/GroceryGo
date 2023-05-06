package com.example.grocerygo.ui.register

import androidx.lifecycle.ViewModel
import com.example.grocerygo.data.PrefStore
import com.example.grocerygo.network.*
import kotlinx.coroutines.runBlocking

class RegisterViewModel : ViewModel() {
    val prefStore = PrefStore.getInstance()

    fun register(
        firstName: String,
        lastName: String,
        username: String,
        password: String,
        phoneNumber: String,
        email: String,
        address: String,
        dob: String,
        gender: Int
    ) = ApiFlowWrapper<LoginResponse>().invoke {
        NetworkInteraction.getInstance().api.register(RegisterRequest(firstName,
        lastName, username,
        password,
        phoneNumber,
        address,
        dob,
        email,
        gender))
    }

    fun onRegisterSuccess(loginResponse: LoginResponse) {
        runBlocking {
            prefStore.saveToken(loginResponse.token?:"")
            prefStore.setIsLoggedIn(true)
        }
    }

}