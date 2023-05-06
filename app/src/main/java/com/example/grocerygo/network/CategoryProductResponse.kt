package com.example.grocerygo.network

data class CategoryProductResponse(val statusCode: StatusCode,
                                   val products: List<Product>):AResponse() {
    override fun getStateObject()= statusCode
}