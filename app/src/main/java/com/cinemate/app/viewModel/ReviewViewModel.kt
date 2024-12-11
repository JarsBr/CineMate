package com.cinemate.app.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cinemate.app.data.models.Review // Certifique-se de usar o Review correto
import com.cinemate.app.data.models.Response
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class ReviewViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    private val _reviews = MutableLiveData<List<Review>>()
    val reviews: LiveData<List<Review>> get() = _reviews

    fun fetchReviews(filmId: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val documents = db.collection("reviews")
                    .whereEqualTo("id_filme", filmId)
                    .get()
                    .await()

                val reviewsList = documents.map { doc ->
                    val userId = doc.getString("id_usuario") ?: ""
                    val userName = if (userId.isNotEmpty()) {
                        db.collection("usuarios").document(userId).get().await().getString("nome") ?: "Usuário desconhecido"
                    } else {
                        "Usuário desconhecido"
                    }

                    val respostas = db.collection("reviews")
                        .document(doc.id)
                        .collection("respostas")
                        .get()
                        .await()
                        .map { resp ->
                            Response(
                                comentario = resp.getString("comentario") ?: "",
                                dataCriacao = resp.getTimestamp("data_criacao") ?: com.google.firebase.Timestamp.now(),
                                idUsuario = resp.getString("id_usuario") ?: ""
                            )
                        }

                    Review(
                        comentario = doc.getString("comentario") ?: "",
                        dataCriacao = doc.getTimestamp("data_criacao")?.toDate()?.time ?: System.currentTimeMillis(),
                        idFilme = doc.getString("id_filme") ?: "",
                        idUsuario = userId,
                        nota = doc.getLong("nota")?.toInt() ?: 0,
                        nomeUsuario = userName,
                        id = doc.id
                    )
                }

                _reviews.postValue(reviewsList)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}
