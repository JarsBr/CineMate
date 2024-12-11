package com.cinemate.app.data.models

import com.google.firebase.Timestamp

data class Response(
    val comentario: String = "",
    val dataCriacao: Timestamp = Timestamp.now(),
    val idUsuario: String = ""
)
