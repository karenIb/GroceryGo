package com.example.grocerygo.ui.signin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.grocerygo.data.PrefStore
import com.example.grocerygo.network.*
import kotlinx.coroutines.launch

class SignInViewModel : ViewModel() {


    private val prefStore = PrefStore.getInstance()

    fun login(username: String, password: String) = ApiFlowWrapper<LoginResponse>().invoke {
        NetworkInteraction.getInstance().api.login(LoginRequest(username, password))
    }

    fun getCart() = ApiFlowWrapper<CartResponse>().invoke {
        NetworkInteraction.getInstance().api.cartItems()
    }

   suspend fun setNumberInCarts(itemCart:Int) {
        prefStore.setNumberInCart(itemCart)
    }

    fun loginSuccess(token: String, onFinish: () -> Unit) {
        viewModelScope.launch {
            prefStore.saveToken(token)
            prefStore.setIsLoggedIn(true)
            onFinish()
        }
    }

}