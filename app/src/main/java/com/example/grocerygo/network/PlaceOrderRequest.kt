package com.example.grocerygo.network

data class PlaceOrderRequest(
    val address: String,
    val email: String,
    val firstName: String,
    val lastName: String,
    val paymentTypeId: Int,
    val phoneNumber: String
)