package com.example.grocerygo.network

data class LoginResponse(
    val statusCode: StatusCode,
    val name: String?,
    val token: String?
):AResponse() {
    override fun getStateObject(): StatusCode = statusCode
}