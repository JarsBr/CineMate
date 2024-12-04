package com.cinemateapp.utils

object Constants {

    val generosList = listOf(
        "Ação",
        "Aventura",
        "Comédia",
        "Drama",
        "Fantasia",
        "Ficção Científica",
        "Terror",
        "Horror",
        "Romance",
        "Suspense",
        "Mistério",
        "Animação"
    )

    data class Plataforma(
        val nome: String,
        val iconeUrl: String,
        val link: String
    )

    val ondeAssistirList = listOf(
        Plataforma(
            nome = "Netflix",
            iconeUrl = "https://res.cloudinary.com/cinemateapp/image/upload/v1733281093/icons/netflix_icon.png",
            link = "https://www.netflix.com"
        ),
        Plataforma(
            nome = "Prime Video",
            iconeUrl = "https://res.cloudinary.com/cinemateapp/image/upload/v1733281093/icons/primevideo_icon.png",
            link = "https://www.primevideo.com"
        ),
        Plataforma(
            nome = "Disney+",
            iconeUrl = "https://res.cloudinary.com/cinemateapp/image/upload/v1733281093/icons/disneyplus_icon.png",
            link = "https://www.disneyplus.com"
        ),
        Plataforma(
            nome = "Max",
            iconeUrl = "https://res.cloudinary.com/cinemateapp/image/upload/v1733281093/icons/max_icon.png",
            link = "https://www.max.com"
        ),
        Plataforma(
            nome = "Apple TV+",
            iconeUrl = "https://res.cloudinary.com/cinemateapp/image/upload/v1733281093/icons/appletv_icon.png",
            link = "https://tv.apple.com"
        ),
        Plataforma(
            nome = "Paramount+",
            iconeUrl = "https://res.cloudinary.com/cinemateapp/image/upload/v1733281093/icons/paramountplus_icon.png",
            link = "https://www.paramountplus.com"
        ),
        Plataforma(
            nome = "Globoplay",
            iconeUrl = "https://res.cloudinary.com/cinemateapp/image/upload/v1733281093/icons/globoplay_icon.png",
            link = "https://globoplay.globo.com"
        ),
        Plataforma(
            nome = "Em cartaz",
            iconeUrl = "https://res.cloudinary.com/cinemateapp/image/upload/v1733281093/icons/cinema_icon.png",
            link = "https://www.ingresso.com"
        ),
        Plataforma(
            nome = "Nenhum",
            iconeUrl = "https://res.cloudinary.com/cinemateapp/image/upload/v1733281610/icons/none_icon.png",
            link = ""
        )
    )
}
