package com.cinemate.app.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cinemate.app.data.models.Response
import com.cinemate.app.data.models.Review
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class ResponseViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()

    private val _respostas = MutableLiveData<List<Response>>()
    val respostas: LiveData<List<Response>> get() = _respostas

    private val _selectedReview = MutableLiveData<Review>()
    val selectedReview: LiveData<Review> get() = _selectedReview

    // Busca os detalhes de uma review específica
    fun fetchReviewById(idReview: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val document = db.collection("reviews").document(idReview).get().await()
                val review = document.toObject(Review::class.java)?.copy(id = document.id)
                review?.let { _selectedReview.postValue(it) }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // Busca todas as respostas associadas a uma review específica
    fun fetchRespostas(idReview: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val snapshot = db.collection("respostas")
                    .whereEqualTo("idReview", idReview) // Garantir que o campo seja o correto
                    .get()
                    .await()

                val responsesList = snapshot.documents.map { doc ->
                    Response(
                        id = doc.id,
                        comentario = doc.getString("comentario") ?: "",
                        dataCriacao = doc.getTimestamp("dataCriacao") ?: com.google.firebase.Timestamp.now(),
                        idReview = doc.getString("idReview") ?: ""
                    )
                }

                _respostas.postValue(responsesList)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun submitResponse(resposta: Response) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val respostaRef = db.collection("respostas").add(resposta).await()
                println("Resposta salva com sucesso: ${respostaRef.id}")
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}


