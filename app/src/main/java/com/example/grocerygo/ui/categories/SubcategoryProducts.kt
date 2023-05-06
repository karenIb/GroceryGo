package com.example.grocerygo.ui.categories

import com.example.grocerygo.network.Product
import com.example.grocerygo.network.Subcategory

data class SubcategoryProducts(
    val subcategory: Subcategory,
    var products:List<Product>
)