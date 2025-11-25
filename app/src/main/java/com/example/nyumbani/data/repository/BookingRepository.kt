package com.example.nyumbani.data.repository

import com.example.nyumbani.model.Booking
import com.example.nyumbani.utils.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.util.UUID
import javax.inject.Inject

class BookingRepository @Inject constructor(
    private val db: FirebaseFirestore,
    private val auth: FirebaseAuth
) {

    // Create a new booking
    suspend fun createBooking(booking: Booking): Resource<Boolean> {
        return try {
            val bookingId = UUID.randomUUID().toString()
            val finalBooking = booking.copy(bookingId = bookingId)
            db.collection("bookings").document(bookingId).set(finalBooking).await()
            Resource.Success(true)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Booking failed")
        }
    }

    // Get bookings for the current student
    suspend fun getStudentBookings(): Resource<List<Booking>> {
        return try {
            val userId = auth.currentUser?.uid ?: return Resource.Error("Not logged in")
            val snapshot = db.collection("bookings")
                .whereEqualTo("studentId", userId)
                .get().await()
            val bookings = snapshot.toObjects(Booking::class.java)
            Resource.Success(bookings)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Failed to load bookings")
        }
    }

    // Get bookings for the OWNER
    suspend fun getOwnerBookings(): Resource<List<Booking>> {
        return try {
            val userId = auth.currentUser?.uid ?: return Resource.Error("Not logged in")
            val snapshot = db.collection("bookings")
                .whereEqualTo("ownerId", userId)
                .get().await()
            val bookings = snapshot.toObjects(Booking::class.java)
            Resource.Success(bookings)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Failed to load bookings")
        }
    }

    // Cancel/Delete Booking
    suspend fun deleteBooking(bookingId: String): Resource<Boolean> {
        return try {
            db.collection("bookings").document(bookingId).delete().await()
            Resource.Success(true)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Failed to cancel booking")
        }
    }

    // Update Status (Confirm/Reject)
    suspend fun updateBookingStatus(bookingId: String, status: String): Resource<Boolean> {
        return try {
            db.collection("bookings").document(bookingId).update("status", status).await()
            Resource.Success(true)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Update failed")
        }
    }
}