package com.example.grocerygo.network

data class Product(
    val currentCartQuantity: Int,
    val discountedPrice: Int,
    val id: String,
    val imageURL: String,
    var isFavorite: Boolean,
    val name: String,
    val price: Int,
    val quantity: Int,
    val pickedQuantity: Int,
    val subcategory: String,
    val subcategoryId: String
) {
    fun actualImage(): String {
        return Endpoints.imagePath.plus(imageURL.split("8888")[1])
    }
}