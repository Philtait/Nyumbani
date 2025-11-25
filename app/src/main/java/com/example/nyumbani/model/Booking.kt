package com.example.nyumbani.model

data class Booking(
    val bookingId: String = "",
    val hostelId: String = "",
    val hostelName: String = "",
    val ownerId: String = "",
    val studentId: String = "",
    val studentName: String = "",
    val totalPrice: Double = 0.0,
    val checkInDate: String = "",
    val checkOutDate: String = "",
    val status: String = "Pending",
    val timestamp: Long = System.currentTimeMillis()
)