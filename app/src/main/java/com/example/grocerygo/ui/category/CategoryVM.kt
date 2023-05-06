package com.example.grocerygo.ui.category

import androidx.lifecycle.ViewModel
import com.example.grocerygo.data.PrefStore
import com.example.grocerygo.network.*
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class CategoryVM:ViewModel() {

    val prefStore = PrefStore.getInstance()

    fun getCategoryProducts(id:Int) = ApiFlowWrapper<CategoryProductResponse>().invoke {
        NetworkInteraction.getInstance().api.getCategoryProducts(id)
    }

    fun isLoggedIn() = runBlocking {
        prefStore.isLoggedIn().first()
    }

     fun addFavorite(productId:String) = ApiFlowWrapper<UnknowResponse>().invoke {
        NetworkInteraction.getInstance().api.addFavorite(FavoriteRequest(productId))
    }
    fun unFavorite(productId:String) = ApiFlowWrapper<UnknowResponse>().invoke {
        NetworkInteraction.getInstance().api.removeFavorite(FavoriteRequest(productId))
    }

    suspend fun addToCart(productId:String) = ApiFlowWrapper<UnknowResponse>().invoke {
        NetworkInteraction.getInstance().api.addToCartRequest(AddToCartRequest( productId.toInt()))
    }
    suspend fun incrementCard() = prefStore.incrementCart()

}