package com.cinemate.app.data.models

data class User(
    val nome: String = "",
    val email: String = "",
    val dataNascimento: String = "",
    val tipoUsuario: String = "",
    val filmesFavoritos: List<String> = emptyList()
)
