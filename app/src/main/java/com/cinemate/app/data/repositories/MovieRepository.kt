package com.cinemate.app.data.repositories

import com.cinemate.app.data.models.Movie
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.tasks.await

class MovieRepository(private val db: FirebaseFirestore) {

    private val collection = db.collection("filmes")

    suspend fun getMovies(): List<Movie>? {
        return try {
            val snapshot = collection.get().await()
            snapshot.toObjects(Movie::class.java)
        } catch (e: Exception) {
            null
        }
    }

    suspend fun addMovie(movie: Movie) {
        try {
            collection.add(movie).await()
        } catch (e: Exception) {
            // Handle exceptions
        }
    }

    suspend fun deleteMovie(movieId: String) {
        try {
            collection.document(movieId).delete().await()
        } catch (e: Exception) {
            // Handle exceptions
        }
    }
}
