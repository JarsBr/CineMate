package com.cinemate.app.data.models

import com.google.firebase.Timestamp

data class Review(
    val id: String = "",
    val comentario: String = "",
    val dataCriacao: Timestamp = Timestamp.now(),
    val idFilme: String = "",
    val idUsuario: String = "",
    val nota: Float = 0f,
    val respostas: List<Response> = emptyList()
)
