package com.example.grocerygo.ui.product

import androidx.lifecycle.ViewModel
import com.example.grocerygo.data.PrefStore
import com.example.grocerygo.network.*
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class ProductViewModel:ViewModel() {

    private val prefStore = PrefStore.getInstance()
    suspend fun  getProductDetails(id:String) = ApiFlowWrapper<ProductDetailsResponse>().invoke {
        NetworkInteraction.getInstance().api.getProductDetails(id)
    }

    suspend fun addReview(productId:String, stars:Int, comment:String) = ApiFlowWrapper<UnknowResponse>().invoke {
        NetworkInteraction.getInstance().api.addReview(AddReviewRequest(comment, productId.toInt(), stars))
    }

    suspend fun removeReview(productId:String) = ApiFlowWrapper<UnknowResponse>().invoke {
        NetworkInteraction.getInstance().api.removeReview(RemoveReviewRequest( productId.toInt()))
    }

    suspend fun addToCart(productId:String) = ApiFlowWrapper<UnknowResponse>().invoke {
        NetworkInteraction.getInstance().api.addToCartRequest(AddToCartRequest( productId.toInt()))
    }
    suspend fun incrementCard() = prefStore.incrementCart()

    suspend fun addFavorite(productId:String) = ApiFlowWrapper<UnknowResponse>().invoke {
        NetworkInteraction.getInstance().api.addFavorite(FavoriteRequest(productId))
    }

     suspend fun incrementFavorite() = prefStore.incrementFavorite()


    suspend fun decrementFavorite() = prefStore.decrementFavorite()

    suspend fun removeFavorite(productId:String) = ApiFlowWrapper<UnknowResponse>().invoke {
        NetworkInteraction.getInstance().api.removeFavorite(FavoriteRequest(productId))
    }

    fun isLoggedIn() = runBlocking {
        prefStore.isLoggedIn().first()
    }

}