package com.example.grocerygo.network

data class ProductDetailsResponse(
    val statusCode: StatusCode,
    val product: ProductDetails
):AResponse() {
    override fun getStateObject(): StatusCode = statusCode
}