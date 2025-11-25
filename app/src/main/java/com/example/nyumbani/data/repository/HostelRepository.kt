package com.example.nyumbani.data.repository

import com.example.nyumbani.model.Hostel
import com.example.nyumbani.utils.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.util.UUID
import javax.inject.Inject

class HostelRepository @Inject constructor(
    private val db: FirebaseFirestore,
    private val auth: FirebaseAuth
) {

    // 1. Add Hostel (Saves text data + Image URL)
    suspend fun addHostel(hostel: Hostel): Resource<Boolean> {
        return try {
            val userId = auth.currentUser?.uid ?: return Resource.Error("Not logged in")

            // Create a unique ID for this hostel
            val newHostelId = UUID.randomUUID().toString()

            val newHostel = hostel.copy(
                hostelId = newHostelId,
                ownerId = userId
            )

            // Save to Firestore "hostels" collection
            db.collection("hostels").document(newHostelId).set(newHostel).await()

            Resource.Success(true)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Failed to add hostel")
        }
    }

    // 2. Get All Hostels (For Students)
    suspend fun getAllHostels(): Resource<List<Hostel>> {
        return try {
            val snapshot = db.collection("hostels").get().await()
            val hostels = snapshot.toObjects(Hostel::class.java)
            Resource.Success(hostels)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Failed to load hostels")
        }
    }

    // 3. Get My Hostels (For Owners)
    suspend fun getOwnerHostels(): Resource<List<Hostel>> {
        return try {
            val userId = auth.currentUser?.uid ?: return Resource.Error("Not logged in")
            val snapshot = db.collection("hostels")
                .whereEqualTo("ownerId", userId)
                .get()
                .await()

            val hostels = snapshot.toObjects(Hostel::class.java)
            Resource.Success(hostels)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Failed to load properties")
        }
    }

    // 4. Delete Hostel
    suspend fun deleteHostel(hostelId: String): Resource<Boolean> {
        return try {
            db.collection("hostels").document(hostelId).delete().await()
            Resource.Success(true)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Failed to delete")
        }
    }
}