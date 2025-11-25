package com.example.nyumbani.model

data class Hostel(
    val hostelId: String = "",
    val ownerId: String = "", // Links to the Owner who created it
    val name: String = "",
    val description: String = "",
    val address: String = "", // Text address (e.g. "Madaraka, Nairobi")
    val price: Double = 0.0,
    val imageUrls: List<String> = emptyList(), // List of image URLs from Firebase Storage
    val features: List<String> = emptyList(), // e.g. ["WiFi", "Water", "Security"]
    val latitude: Double = -1.2921, // Default to Nairobi
    val longitude: Double = 36.8219
)