package com.example.grocerygo.ui.search

import androidx.lifecycle.ViewModel
import com.example.grocerygo.network.ApiFlowWrapper
import com.example.grocerygo.network.NetworkInteraction
import com.example.grocerygo.network.ProductsResponse

class SearchViewModel :ViewModel() {

    fun searchProduct(query:String) = ApiFlowWrapper<ProductsResponse>().invoke {
        NetworkInteraction.getInstance().api.searchProducts(query)
    }
}