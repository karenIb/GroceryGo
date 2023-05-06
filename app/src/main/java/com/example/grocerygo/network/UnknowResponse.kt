package com.example.grocerygo.network

data class UnknowResponse(val statusCode: StatusCode):AResponse() {
    override fun getStateObject(): StatusCode = statusCode
}