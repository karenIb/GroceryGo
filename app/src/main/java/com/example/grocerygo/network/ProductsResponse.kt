package com.example.grocerygo.network

data class ProductsResponse(
    val statusCode: StatusCode,
    val products: List<Product> = ArrayList()
):AResponse() {
    override fun getStateObject(): StatusCode = statusCode
}