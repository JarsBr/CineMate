package com.cinemate.app.data.repositories

import com.cinemate.app.data.models.Suggestion
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class SuggestionRepository(private val db: FirebaseFirestore) {

    private val collection = db.collection("suggestions")


    suspend fun addSuggestion(suggestion: Suggestion): Result<Unit> {
        return try {
            val documentRef = collection.add(suggestion).await()
            suggestion.id = documentRef.id // Atualiza o ID gerado no objeto
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    suspend fun getSuggestions(): List<Suggestion>? {
        return try {
            val snapshot = collection.get().await()
            snapshot.toObjects(Suggestion::class.java).map { suggestion ->
                suggestion.id = snapshot.documents.find { it.getString("suggestionText") == suggestion.suggestionText }?.id.orEmpty()
                suggestion
            }
        } catch (e: Exception) {
            null
        }
    }


    suspend fun deleteSuggestion(suggestionId: String) {
        if (suggestionId.isNotEmpty()) {
            collection.document(suggestionId).delete().await()
        } else {
            throw Exception("ID da sugestão está vazio. Não é possível excluir.")
        }
    }
}
