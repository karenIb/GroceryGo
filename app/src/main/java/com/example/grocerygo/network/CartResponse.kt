package com.example.grocerygo.network

data class CartResponse(
    val statusCode: StatusCode,
    val cart: Cart?
) : AResponse() {
    override fun getStateObject() = statusCode

}