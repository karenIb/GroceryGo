package com.example.grocerygo.network

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface API {

    @POST(Endpoints.login)
    suspend fun login(@Body loginRequest: LoginRequest): LoginResponse

    @POST(Endpoints.register)
    suspend fun register(@Body registerRequest: RegisterRequest): LoginResponse

    @POST(Endpoints.addReview)
    suspend fun addReview(@Body reviewRequest: AddReviewRequest): UnknowResponse

    @POST(Endpoints.removeReview)
    suspend fun removeReview(@Body reviewRequest: RemoveReviewRequest): UnknowResponse

    @POST(Endpoints.addCart)
    suspend fun addToCartRequest(@Body addToCartRequest: AddToCartRequest): UnknowResponse

    @POST(Endpoints.decrementProduct)
    suspend fun decrementProduct(@Body addToCartRequest: AddToCartRequest): UnknowResponse


    @POST(Endpoints.removeFavorite)
    suspend fun removeFavorite(@Body removeFavoriteRequest: FavoriteRequest): UnknowResponse

    @POST(Endpoints.addFavorite)
    suspend fun addFavorite(@Body addFavoriteRequest: FavoriteRequest): UnknowResponse

    @GET(Endpoints.cartItems)
    suspend fun cartItems(): CartResponse

    @GET(Endpoints.offers)
    suspend fun getOffers(): OfferResponse

    @POST(Endpoints.placefromcart)
    suspend fun placeFromCart(@Body placeOrderRequest: PlaceOrderRequest): UnknowResponse


    @GET(Endpoints.product)
    suspend fun getProductDetails(@Query("id") id:String): ProductDetailsResponse

    @GET(Endpoints.getProfile)
    suspend fun getProfile(): ProfileResponse

    @GET(Endpoints.products)
    suspend fun searchProducts(@Query("searchQuery") searchQuery:String): ProductsResponse

    @GET(Endpoints.categories)
    suspend fun getCategories(): CategoryResponse


    @GET(Endpoints.subCategoryProducts)
    suspend fun getSubCategoryProduct(@Query("subcategoryId") subcategoryId:Int): SubCategoryProductsResponse

    @GET(Endpoints.categoryProducts)
    suspend fun getCategoryProducts(@Query("categoryId") categoryId:Int): CategoryProductResponse

}