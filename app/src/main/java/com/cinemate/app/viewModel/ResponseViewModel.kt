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

    // Função para buscar respostas associadas a uma review específica
    fun fetchRespostas(reviewId: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Acessando a subcoleção 'respostas' dentro de um documento de review
                val snapshot = db.collection("reviews")
                    .document(reviewId) // Acessando o documento da review
                    .collection("respostas") // Acessando a subcoleção 'respostas'
                    .get()
                    .await()

                val respostasList = snapshot.documents.map { doc ->
                    Response(
                        comentario = doc.getString("comentario") ?: "",
                        dataCriacao = doc.getTimestamp("data_criacao") ?: com.google.firebase.Timestamp.now(),
                        idUsuario = doc.getString("id_usuario") ?: ""
                    )
                }

                _respostas.postValue(respostasList)
            } catch (e: Exception) {
                e.printStackTrace()
                // Adicionar tratamento de erro aqui, se necessário
            }
        }
    }
}
