package com.example.grocerygo.network

data class OfferResponse(
    val statusCode: StatusCode,
    val products: List<Product>
):AResponse() {
    override fun getStateObject(): StatusCode = statusCode
}