package com.example.grocerygo.network

data class AddReviewRequest(
    val comment: String,
    val productId: Int,
    val stars: Int
)