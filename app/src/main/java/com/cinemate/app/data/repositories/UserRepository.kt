package com.cinemate.app.data.repositories

import com.cinemate.app.data.models.User
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class UserRepository(private val db: FirebaseFirestore) {

    private val collection = db.collection("usuarios")

    suspend fun getUserById(userId: String): User? {
        return try {
            collection.document(userId).get().await().toObject(User::class.java)
        } catch (e: Exception) {
            null
        }
    }

    suspend fun addUser(user: User) {
        try {
            collection.document(user.id).set(user).await()
        } catch (e: Exception) {
            // Handle exceptions
        }
    }

    suspend fun updateUser(userId: String, updatedData: Map<String, Any>) {
        try {
            collection.document(userId).update(updatedData).await()
        } catch (e: Exception) {
            // Handle exceptions
        }
    }

    suspend fun deleteUser(userId: String) {
        try {
            collection.document(userId).delete().await()
        } catch (e: Exception) {
            // Handle exceptions
        }
    }
}
