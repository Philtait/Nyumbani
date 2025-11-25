package com.example.nyumbani.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nyumbani.data.repository.HostelRepository
import com.example.nyumbani.model.Hostel
import com.example.nyumbani.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HostelViewModel @Inject constructor(
    private val repository: HostelRepository
) : ViewModel() {

    private val _hostelListState = MutableStateFlow<Resource<List<Hostel>>>(Resource.Loading())
    val hostelListState: StateFlow<Resource<List<Hostel>>> = _hostelListState

    private val _operationState = MutableStateFlow<Resource<Boolean>>(Resource.Success(false))
    val operationState: StateFlow<Resource<Boolean>> = _operationState

    fun loadAllHostels() {
        viewModelScope.launch {
            _hostelListState.value = Resource.Loading()
            _hostelListState.value = repository.getAllHostels()
        }
    }

    fun loadOwnerHostels() {
        viewModelScope.launch {
            _hostelListState.value = Resource.Loading()
            _hostelListState.value = repository.getOwnerHostels()
        }
    }

    // UPDATED: Now accepts 'features' list
    fun addHostel(
        name: String,
        address: String,
        price: String,
        desc: String,
        imageUrl: String,
        features: List<String>
    ) {
        viewModelScope.launch {
            _operationState.value = Resource.Loading()
            val priceDouble = price.toDoubleOrNull() ?: 0.0
            val finalImage = if (imageUrl.isNotEmpty()) imageUrl else "https://images.unsplash.com/photo-1555854877-bab0e564b8d5"

            val newHostel = Hostel(
                name = name,
                address = address,
                price = priceDouble,
                description = desc,
                imageUrls = listOf(finalImage),
                features = features // Saved here
            )

            val result = repository.addHostel(newHostel)
            _operationState.value = result
            if (result is Resource.Success) {
                loadOwnerHostels()
            }
        }
    }

    fun deleteHostel(hostelId: String) {
        viewModelScope.launch {
            repository.deleteHostel(hostelId)
            loadOwnerHostels()
        }
    }

    fun resetOperationState() {
        _operationState.value = Resource.Success(false)
    }
}