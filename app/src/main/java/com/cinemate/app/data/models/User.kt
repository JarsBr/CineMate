package com.cinemate.app.data.models

data class User(
    val id: String = "",
    val dataNascimento: String = "",
    val email: String = "",
    val filmesFavoritos: List<String> = emptyList(),
    val nome: String = "",
    val tipoUsuario: String = ""
)
