package com.cinemate.app.data.repositories

import com.cinemate.app.data.models.Review
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class ReviewRepository(private val db: FirebaseFirestore) {
//
//    private val collection = db.collection("reviews")
//
//    suspend fun getReviewsByMovieId(movieId: String): List<Review>? {
//        return try {
//            val snapshot = collection.whereEqualTo("id_filme", movieId).get().await()
//            snapshot.map { document ->
//                val review = document.toObject(Review::class.java)
//                review.id = document.id
//                review
//            }
//        } catch (e: Exception) {
//            null
//        }
//    }
//
//    suspend fun addReview(review: Review): Result<Unit> {
//        return try {
//            if (review.comentario.isEmpty()) {
//                throw IllegalArgumentException("O comentário da review não pode estar vazio.")
//            }
//
//            val documentReference = if (review.id.isNotEmpty()) {
//                collection.document(review.id).set(review).await()
//                collection.document(review.id)
//            } else {
//                val docRef = collection.add(review).await()
//                review.id = docRef.id // Atualiza o ID da review com o ID do documento
//                docRef
//            }
//
//            review.id = documentReference.id
//            Result.success(Unit)
//        } catch (e: Exception) {
//            Result.failure(e)
//        }
//    }
//
//    suspend fun deleteReview(reviewId: String) {
//        try {
//            if (reviewId.isNotEmpty()) {
//                collection.document(reviewId).delete().await()
//            } else {
//                throw Exception("ID da review está vazio. Não é possível excluir.")
//            }
//        } catch (e: Exception) {
//            throw Exception("Erro ao excluir a review: ${e.message}")
//        }
//    }
//
//    suspend fun updateReview(review: Review) {
//        try {
//            val reviewId = review.id
//            if (reviewId.isNotEmpty()) {
//                collection.document(reviewId).set(review).await()
//            } else {
//                throw Exception("ID da review está vazio. Não é possível atualizar.")
//            }
//        } catch (e: Exception) {
//            throw Exception("Erro ao atualizar a review: ${e.message}")
//        }
//    }
}

