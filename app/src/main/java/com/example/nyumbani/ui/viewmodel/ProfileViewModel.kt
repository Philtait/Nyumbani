package com.example.nyumbani.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nyumbani.data.repository.ProfileRepository
import com.example.nyumbani.model.User
import com.example.nyumbani.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: ProfileRepository
) : ViewModel() {

    private val _profileState = MutableStateFlow<Resource<User>>(Resource.Loading())
    val profileState: StateFlow<Resource<User>> = _profileState

    private val _updateState = MutableStateFlow<Resource<Boolean>>(Resource.Success(false))
    val updateState: StateFlow<Resource<Boolean>> = _updateState

    fun loadProfile() {
        viewModelScope.launch {
            _profileState.value = Resource.Loading()
            _profileState.value = repository.getUserProfile()
        }
    }

    fun updateProfile(name: String, phone: String) {
        viewModelScope.launch {
            _updateState.value = Resource.Loading()
            val result = repository.updateUserProfile(name, phone)
            _updateState.value = result
            if (result is Resource.Success) {
                loadProfile() // Refresh data
            }
        }
    }

    fun logout() {
        repository.logout()
    }
}