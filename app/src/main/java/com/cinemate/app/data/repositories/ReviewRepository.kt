package com.cinemate.app.data.repositories

import com.cinemate.app.data.models.Response
import com.cinemate.app.data.models.Review
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class ReviewRepository(private val db: FirebaseFirestore) {

    private val collection = db.collection("reviews")

    suspend fun getReviewsByMovie(movieId: String): List<Review>? {
        return try {
            collection.whereEqualTo("idFilme", movieId).get().await().toObjects(Review::class.java)
        } catch (e: Exception) {
            null
        }
    }

    suspend fun addReview(review: Review) {
        try {
            collection.add(review).await()
        } catch (e: Exception) {
            // Handle exceptions
        }
    }

    suspend fun addResponse(reviewId: String, response: Response) {
        try {
            db.collection("reviews").document(reviewId)
                .collection("respostas")
                .add(response).await()
        } catch (e: Exception) {
            // Handle exceptions
        }
    }

    suspend fun getResponses(reviewId: String): List<Response>? {
        return try {
            db.collection("reviews").document(reviewId)
                .collection("respostas")
                .get().await().toObjects(Response::class.java)
        } catch (e: Exception) {
            null
        }
    }

}
