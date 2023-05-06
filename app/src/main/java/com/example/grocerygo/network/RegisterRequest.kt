package com.example.grocerygo.network

data class RegisterRequest(
    val firstName: String,
    val lastName: String,
    val username: String,
    val password: String,
    val phoneNumber: String,
    val address: String,
    val dob: String,
    val email: String,
    val gender: Int
)
