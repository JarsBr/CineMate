package com.cinemate.app.data.models
import com.google.firebase.Timestamp

data class Review(
    val comentario: String = "",
    val dataCriacao: Timestamp = Timestamp.now(),
    val idFilme: String = "",
    val idUsuario: String = "",
    val nota: Int = 0,
    val respostas: List<Response> = listOf()
)
