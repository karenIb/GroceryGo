package com.example.grocerygo.network

data class Review(
    val comment: String,
    val customerId: Int,
    val customerName: String,
    val date: String,
    val id: Int,
    val isMine: Boolean,
    val stars: Int
)