package com.cinemate.app.data.models

import com.google.firebase.Timestamp

data class Suggestion(
    val id: String = "",
    val idUsuario: String = "",
    val sugestao: String = "",
    val dataEnvio: Timestamp = Timestamp.now()
)
