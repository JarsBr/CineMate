package com.cinemate.app.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cinemate.app.data.models.Response
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class ResponseViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    private val _respostas = MutableLiveData<List<Response>>()
    val respostas: LiveData<List<Response>> get() = _respostas

    // Busca todas as respostas associadas a uma review específica
    fun fetchRespostas(idReview: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val snapshot = db.collection("respostas")
                    .whereEqualTo("id_review", idReview)
                    .get()
                    .await()

                val respostasList = snapshot.documents.map { doc ->
                    Response(
                        id = doc.id, // Use o ID do documento do Firestore
                        comentario = doc.getString("comentario") ?: "",
                        dataCriacao = doc.getTimestamp("data_criacao") ?: com.google.firebase.Timestamp.now(),
                        idReview = doc.getString("id_review") ?: "",
                        idUsuario = doc.getString("id_usuario") ?: ""
                    )
                }

                _respostas.postValue(respostasList)
            } catch (e: Exception) {
                e.printStackTrace()
                // Adicionar lógica de tratamento de erros se necessário
            }
        }
    }


}

