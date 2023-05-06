package com.example.grocerygo.network

data class Category(
    val id: String,
    val imageURL: String,
    val name: String,
    val subcategories: List<Subcategory>
) {

    fun actualImage(): String {
        return Endpoints.imagePath.plus(imageURL.split("8888")[1])
    }
}