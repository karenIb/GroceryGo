package com.example.grocerygo.network

data class Subcategory(
    val id: String,
    val imageURL: String,
    val name: String,
    val parentId: String,
    val promotion: Promotion
)