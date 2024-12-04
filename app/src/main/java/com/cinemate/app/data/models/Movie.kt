package com.cinemate.app.data.models

data class Movie(
    val id: String = "",
    val ano: Int = 0,
    val atoresPrincipais: String = "",
    val duracao: String = "",
    val faixaEtaria: Int = 0,
    val titulo: String = "",
    val genero: List<String> = emptyList(),
    val imagemUrl: String = "",
    val mediaAvaliacao: Float = 0f,
    val ondeAssistir: List<String> = emptyList(),
    val qtdReviews: Int = 0,
    val sinopse: String = ""
)
