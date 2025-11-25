package com.example.nyumbani.data.repository

import com.example.nyumbani.model.User
import com.example.nyumbani.utils.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ProfileRepository @Inject constructor(
    private val db: FirebaseFirestore,
    private val auth: FirebaseAuth
) {
    suspend fun getUserProfile(): Resource<User> {
        return try {
            val userId = auth.currentUser?.uid ?: return Resource.Error("Not logged in")
            val snapshot = db.collection("users").document(userId).get().await()
            val user = snapshot.toObject(User::class.java)

            if (user != null) {
                Resource.Success(user)
            } else {
                Resource.Error("User not found")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Failed to load profile")
        }
    }

    suspend fun updateUserProfile(name: String, phone: String): Resource<Boolean> {
        return try {
            val userId = auth.currentUser?.uid ?: return Resource.Error("Not logged in")
            val updates = mapOf(
                "name" to name,
                "phone" to phone
            )
            db.collection("users").document(userId).update(updates).await()
            Resource.Success(true)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Update failed")
        }
    }

    fun logout() {
        auth.signOut()
    }
}