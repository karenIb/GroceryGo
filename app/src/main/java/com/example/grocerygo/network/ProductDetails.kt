package com.example.grocerygo.network

data class ProductDetails(
    val currentCartQuantity: Int,
    val discountedPrice: Int,
    val id: String,
    val imageURL: String,
    val isFavorite: Boolean,
    val name: String,
    val price: Int,
    val quantity: Int,
    val reviews: List<Review>,
    val subcategory: String,
    val subcategoryId: String
) {
    fun actualImage(): String {
        return Endpoints.imagePath.plus(imageURL.split("8888")[1])
    }
}