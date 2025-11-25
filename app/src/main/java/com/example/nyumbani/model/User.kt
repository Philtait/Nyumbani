package com.example.nyumbani.model

data class User(
    val userId: String = "",
    val name: String = "",
    val email: String = "",
    val phone: String = "",
    val role: String = "Student" // Values: "Student" or "Owner"
)