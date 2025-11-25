package com.example.nyumbani.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nyumbani.data.repository.AuthRepository
import com.example.nyumbani.model.User
import com.example.nyumbani.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {

    // Authentication State
    private val _authState = MutableStateFlow<Resource<String>>(Resource.Error("")) // Idle
    val authState: StateFlow<Resource<String>> = _authState

    // Verification State
    private val _verificationState = MutableStateFlow<Resource<Boolean>>(Resource.Loading())
    val verificationState: StateFlow<Resource<Boolean>> = _verificationState

    fun login(email: String, pass: String) {
        viewModelScope.launch {
            _authState.value = Resource.Loading()
            _authState.value = repository.login(email, pass)
        }
    }

    fun signUp(name: String, email: String, pass: String, phone: String, role: String) {
        viewModelScope.launch {
            _authState.value = Resource.Loading()
            val newUser = User(name = name, email = email, phone = phone, role = role)
            _authState.value = repository.signUp(newUser, pass)
        }
    }

    fun checkEmailVerification() {
        viewModelScope.launch {
            _verificationState.value = Resource.Loading()
            _verificationState.value = repository.reloadUser()
        }
    }

    fun resendEmail() {
        viewModelScope.launch {
            repository.resendVerificationEmail()
        }
    }

    fun isEmailVerified(): Boolean {
        return repository.isEmailVerified()
    }

    // --- THIS WAS MISSING ---
    fun logout() {
        repository.logout()
        resetState()
    }
    // ------------------------

    fun resetState() {
        _authState.value = Resource.Error("")
    }
}