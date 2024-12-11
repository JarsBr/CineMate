package com.cinemate.app.data.models

import com.google.firebase.Timestamp

data class Response(
    val id: String = "", // Adiciona o campo id
    val comentario: String = "",
    val dataCriacao: Timestamp = Timestamp.now(),
    val idReview: String = "",
    val idUsuario: String = ""
)


