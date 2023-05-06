package com.example.grocerygo.ui.placeorder

import androidx.lifecycle.ViewModel
import com.example.grocerygo.data.PrefStore
import com.example.grocerygo.network.*
import kotlinx.coroutines.runBlocking

class PlaceOrderVM : ViewModel() {
    val prefStore = PrefStore.getInstance()


    fun resetCart() {
        runBlocking {
            prefStore.setNumberInCart(0)
        }
    }

    fun getProfile() = ApiFlowWrapper<ProfileResponse>().invoke {
        NetworkInteraction.getInstance().api.getProfile()
    }

    fun placeOrder(firstName:String, lastName:String, email:String, phoneNumber:String, address:String, paymentType:Int = 3) = ApiFlowWrapper<UnknowResponse>().invoke {
        NetworkInteraction.getInstance().api.placeFromCart(PlaceOrderRequest(address, email,firstName, lastName, paymentType, phoneNumber))
    }

}