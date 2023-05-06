package com.example.grocerygo.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.grocerygo.data.PrefStore
import com.example.grocerygo.network.*
import com.example.grocerygo.ui.categories.SubcategoryProducts
import com.example.grocerygo.ui.home.HomeModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class MainViewModel : ViewModel() {

    private val prefStore = PrefStore.getInstance()

    val pageModelState = MutableStateFlow<Resource>(Resource.NotStarted)
    val categoriesState = MutableStateFlow<List<Category>>(emptyList())
    val subCateState = MutableStateFlow<List<SubcategoryProducts>>(emptyList())
    val cardBadgeNum = MutableStateFlow<Int>(0)

    fun isLoggedIn() = runBlocking {
        prefStore.isLoggedIn().first()
    }

    fun logout() = runBlocking {
        prefStore.setIsLoggedIn(false)
        prefStore.saveToken("")

    }

    fun getCart() = ApiFlowWrapper<CartResponse>().invoke {
        NetworkInteraction.getInstance().api.cartItems()
    }

    suspend fun decrement() = prefStore.decremenCart()

    suspend fun removeFromCart(productId:String) = ApiFlowWrapper<UnknowResponse>().invoke {
        NetworkInteraction.getInstance().api.decrementProduct(AddToCartRequest( productId.toInt()))
    }

    fun getCardBadgeNumber() = runBlocking {
        cardBadgeNum.value = prefStore.numberInCart().first()
    }

    fun getCardItems() = ApiFlowWrapper<CartResponse>().invoke {
        NetworkInteraction.getInstance().api.cartItems()
    }

    fun getCategories() = ApiFlowWrapper<CategoryResponse>().invoke {
        NetworkInteraction.getInstance().api.getCategories()
    }

    fun getSubCategryProduct(subcategoryId: Int) =
        ApiFlowWrapper<SubCategoryProductsResponse>().invoke {
            NetworkInteraction.getInstance().api.getSubCategoryProduct(subcategoryId)
        }

    fun getOffers() = ApiFlowWrapper<OfferResponse>().invoke {
        NetworkInteraction.getInstance().api.getOffers()
    }

    fun addFavorite(productId: String) = ApiFlowWrapper<UnknowResponse>().invoke {
        NetworkInteraction.getInstance().api.addFavorite(FavoriteRequest(productId))
    }

    fun unFavorite(productId: String) = ApiFlowWrapper<UnknowResponse>().invoke {
        NetworkInteraction.getInstance().api.removeFavorite(FavoriteRequest(productId))
    }

    suspend fun addToCart(productId: String) = ApiFlowWrapper<UnknowResponse>().invoke {
        NetworkInteraction.getInstance().api.addToCartRequest(AddToCartRequest(productId.toInt()))
    }


    suspend fun decrementCard(productId: String) = ApiFlowWrapper<UnknowResponse>().invoke {
        NetworkInteraction.getInstance().api.decrementProduct(AddToCartRequest(productId.toInt()))
    }

    suspend fun incrementCard() {
        viewModelScope.launch {
            prefStore.incrementCart()
            getCardBadgeNumber()
        }

    }


    sealed class Resource(val data: List<HomeModel<*>>? = null, val message: String? = null) {
        class Success(data: List<HomeModel<*>>) : Resource(data)
        object NotStarted : Resource()
        class Error(eMessage: String) : Resource(message = eMessage)
        object Loading : Resource()
    }

}