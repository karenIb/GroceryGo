package com.example.grocerygo.network

object Endpoints {

    const val ip = "http://192.168.173.204"
    const val host = "$ip:8888/"
    const val login = "profile/login"
    const val register = "profile/register"
    const val offers = "product/getOffers?page=0&pageSize=15"
    const val products = "product/getProducts?page=0&pageSize=20"
    const val product = "product/getProduct"
    const val categories = "category/getCategories"
    const val categoryProducts = "product/getCategoryProducts"
    const val subCategoryProducts = "product/getSubcategoryProducts"

    const val getProfile = "profile/getProfile"

    const val placefromcart = "order/addOrderFromCart"

    const val addReview = "profile/addReview"
    const val removeReview = "profile/removeReview"
    const val removeFavorite = "profile/removeFavorite"
    const val addFavorite = "profile/addFavorite"
    const val addCart = "cart/incrementProduct"
    const val decrementProduct = "cart/decrementProduct"
    const val cartItems = "cart/getCart"
    const val imagePath= "$ip/backend/public"
    const val addOrderFromCart= "order/addOrderFromCart"

}