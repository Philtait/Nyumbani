package com.example.nyumbani.data.repository

import com.example.nyumbani.model.User
import com.example.nyumbani.utils.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val auth: FirebaseAuth,
    private val db: FirebaseFirestore
) {

    // Function to Sign Up a new user
    suspend fun signUp(user: User, password: String): Resource<String> {
        return try {
            // 1. Create Authentication User
            val authResult = auth.createUserWithEmailAndPassword(user.email, password).await()
            val firebaseUser = authResult.user ?: return Resource.Error("User creation failed")

            // --- NEW: Send Verification Email ---
            firebaseUser.sendEmailVerification()
            // ------------------------------------

            // 2. Prepare User Data with the new UID
            val newUser = user.copy(userId = firebaseUser.uid)

            // 3. Save User Details (Name, Phone, Role) to Firestore
            db.collection("users").document(firebaseUser.uid).set(newUser).await()

            // Return the role (Student/Owner) so the app knows where to go
            Resource.Success(newUser.role)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An unknown error occurred")
        }
    }

    // Function to Login
    suspend fun login(email: String, password: String): Resource<String> {
        return try {
            // 1. Authenticate with Firebase
            val authResult = auth.signInWithEmailAndPassword(email, password).await()
            val uid = authResult.user?.uid ?: return Resource.Error("Login failed")

            // 2. Fetch the User's Role from Firestore
            val document = db.collection("users").document(uid).get().await()
            val role = document.getString("role") ?: "Student"

            Resource.Success(role)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Login failed")
        }
    }

    // Add these inside AuthRepository class

    suspend fun reloadUser(): Resource<Boolean> {
        return try {
            auth.currentUser?.reload()?.await()
            val isVerified = auth.currentUser?.isEmailVerified == true
            Resource.Success(isVerified)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Failed to reload")
        }
    }

    fun isEmailVerified(): Boolean {
        return auth.currentUser?.isEmailVerified == true
    }

    suspend fun resendVerificationEmail(): Resource<Boolean> {
        return try {
            auth.currentUser?.sendEmailVerification()?.await()
            Resource.Success(true)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Failed to send email")
        }
    }

    // Check if user is already logged in (for Splash Screen)
    fun isUserLoggedIn(): Boolean = auth.currentUser != null

    // Logout
    fun logout() {
        auth.signOut()
    }
}