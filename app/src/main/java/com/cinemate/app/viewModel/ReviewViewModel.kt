package com.cinemate.app.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cinemate.app.data.models.Review
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore


class ReviewViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    private val _reviews = MutableLiveData<List<Review>>()
    val reviews: LiveData<List<Review>> get() = _reviews

    fun addReview(review: Review, callback: (Boolean) -> Unit) {
        db.collection("reviews").add(review.toMap())
            .addOnSuccessListener {
                callback(true)
            }
            .addOnFailureListener { exception ->
                Log.e("ReviewViewModel", "Error adding review", exception)
                callback(false)
            }
    }

    fun updateMovieRating(filmId: String) {
        db.collection("reviews")
            .whereEqualTo("id_filme", filmId)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val reviews = task.result
                    val totalReviews = reviews.size()
                    if (totalReviews > 0) {
                        val totalRating = reviews.sumOf { it.getLong("nota")?.toDouble() ?: 0.0 }
                        val averageRating = totalRating / totalReviews

                        db.collection("filmes").document(filmId)
                            .update("mediaAvaliacao", averageRating.toFloat())
                            .addOnSuccessListener {
                                Log.d("ReviewViewModel", "mediaAvaliacao atualizada com sucesso!")
                            }
                            .addOnFailureListener { exception ->
                                Log.e("ReviewViewModel", "Erro ao atualizar mediaAvaliacao", exception)
                            }
                    }
                } else {
                    Log.e("ReviewViewModel", "Erro ao buscar reviews para calcular a média", task.exception)
                }
            }
    }


    fun fetchReviews(filmId: String) {
        db.collection("reviews")
            .whereEqualTo("id_filme", filmId)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val documents = task.result
                    val reviewsList = documents.map { doc ->
                        val userId = doc.getString("id_usuario") ?: ""
                        val userName = doc.getString("nome_usuario") ?: "Usuário desconhecido"

                        Review(
                            comentario = doc.getString("comentario") ?: "",
                            dataCriacao = doc.getTimestamp("data_criacao") ?: Timestamp.now(),
                            idFilme = doc.getString("id_filme") ?: "",
                            idUsuario = userId,
                            nota = doc.getLong("nota")?.toInt() ?: 0,
                            nomeUsuario = userName,
                            id = doc.id
                        )
                    }
                    _reviews.postValue(reviewsList)
                } else {
                    Log.e("ReviewViewModel", "Error fetching reviews", task.exception)
                }
            }
    }

}

