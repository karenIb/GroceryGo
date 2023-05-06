package com.example.grocerygo.network

data class CategoryResponse(
    val statusCode: StatusCode,
    val categories: List<Category>
):AResponse() {
    override fun getStateObject(): StatusCode = statusCode
}
