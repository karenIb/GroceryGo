package com.example.grocerygo.network

data class Cart(
    val id: Int,
    val totalPrice: Int,
    val products: List<Product>)
