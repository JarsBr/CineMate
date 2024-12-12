package com.cinemate.app.data.models

import com.google.firebase.Timestamp

data class Response(
    val id: String = "",
    val comentario: String = "",
    val dataCriacao: Timestamp = Timestamp.now(),
    val idReview: String = "",
    val idUsuario: String = ""
)


