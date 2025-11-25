package com.example.nyumbani.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nyumbani.data.repository.BookingRepository
import com.example.nyumbani.model.Booking
import com.example.nyumbani.utils.Resource
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookingViewModel @Inject constructor(
    private val repository: BookingRepository,
    private val auth: FirebaseAuth
) : ViewModel() {

    private val _bookingState = MutableStateFlow<Resource<Boolean>>(Resource.Success(false))
    val bookingState: StateFlow<Resource<Boolean>> = _bookingState

    private val _myBookings = MutableStateFlow<Resource<List<Booking>>>(Resource.Loading())
    val myBookings: StateFlow<Resource<List<Booking>>> = _myBookings

    // UPDATED: Now accepts Check-in and Check-out dates
    fun createBooking(
        hostelId: String,
        hostelName: String,
        price: Double,
        ownerId: String,
        checkIn: String,
        checkOut: String
    ) {
        viewModelScope.launch {
            _bookingState.value = Resource.Loading()
            val userId = auth.currentUser?.uid ?: ""
            val userEmail = auth.currentUser?.email ?: "Student"

            val booking = Booking(
                hostelId = hostelId,
                hostelName = hostelName,
                ownerId = ownerId,
                studentId = userId,
                studentName = userEmail,
                totalPrice = price,
                checkInDate = checkIn, // <--- Saving Date
                checkOutDate = checkOut, // <--- Saving Date
                status = "Pending"
            )
            _bookingState.value = repository.createBooking(booking)
        }
    }

    fun loadMyBookings() {
        viewModelScope.launch {
            _myBookings.value = Resource.Loading()
            _myBookings.value = repository.getStudentBookings()
        }
    }

    fun cancelBooking(bookingId: String) {
        viewModelScope.launch {
            repository.deleteBooking(bookingId)
            loadMyBookings()
        }
    }

    // --- OWNER FUNCTIONS ---
    fun loadOwnerBookings() {
        viewModelScope.launch {
            _myBookings.value = Resource.Loading()
            _myBookings.value = repository.getOwnerBookings()
        }
    }

    fun updateStatus(bookingId: String, status: String) {
        viewModelScope.launch {
            repository.updateBookingStatus(bookingId, status)
            loadOwnerBookings()
        }
    }

    fun resetState() {
        _bookingState.value = Resource.Success(false)
    }
}