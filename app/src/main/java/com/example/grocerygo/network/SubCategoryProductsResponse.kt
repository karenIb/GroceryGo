package com.example.grocerygo.network

class SubCategoryProductsResponse (
    val products: List<Product>,
    val totalCount:Int,
    val statusCode: StatusCode,

    ):AResponse() {
    override fun getStateObject(): StatusCode = statusCode
}