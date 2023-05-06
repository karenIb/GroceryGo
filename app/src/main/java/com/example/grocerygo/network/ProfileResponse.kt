package com.example.grocerygo.network

class ProfileResponse (
    val statusCode: StatusCode,
    val profile: Profile
):AResponse() {
    override fun getStateObject(): StatusCode = statusCode
}