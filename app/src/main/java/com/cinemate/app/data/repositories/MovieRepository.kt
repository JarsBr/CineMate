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
            snapshot.toObjects(Movie::class.java).map { movie ->
                movie.id = snapshot.documents.find { it.getString("titulo") == movie.titulo }?.id.orEmpty()
                movie
            }
        } catch (e: Exception) {
            null
        }
    }

    suspend fun addMovie(movie: Movie): Result<Unit> {
        return try {
            if (movie.titulo.isEmpty()) {
                throw IllegalArgumentException("O título do filme não pode estar vazio.")
            }

            val documentReference = if (movie.id.isNotEmpty()) {
                collection.document(movie.id).set(movie).await()
                collection.document(movie.id)
            } else {
                val docRef = collection.add(movie).await()
                docRef
            }

            // Atualiza o ID no objeto Movie
            movie.id = documentReference.id
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteMovie(movieId: String) {
        try {
            if (movieId.isNotEmpty()) {
                collection.document(movieId).delete().await()
            } else {
                throw Exception("ID do filme está vazio. Não é possível excluir.")
            }
        } catch (e: Exception) {
            throw Exception("Erro ao excluir o filme: ${e.message}")
        }
    }

    suspend fun updateMovie(movie: Movie) {
        try {
            val movieId = movie.id
            if (movieId.isNotEmpty()) {
                collection.document(movieId).set(movie).await()
            } else {
                throw Exception("ID do filme está vazio. Não é possível atualizar.")
            }
        } catch (e: Exception) {
            throw Exception("Erro ao atualizar o filme: ${e.message}")
        }
    }
}
