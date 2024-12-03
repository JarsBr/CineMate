package com.cinemate.app.data.repositories

import com.cinemate.app.data.models.Suggestion
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class SuggestionRepository(private val db: FirebaseFirestore) {

    private val collection = db.collection("sugestoes")

    suspend fun addSuggestion(suggestion: Suggestion) {
        try {
            collection.add(suggestion).await()
        } catch (e: Exception) {
            // Handle exceptions
        }
    }

    suspend fun getSuggestions(): List<Suggestion>? {
        return try {
            collection.get().await().toObjects(Suggestion::class.java)
        } catch (e: Exception) {
            null
        }
    }
}
