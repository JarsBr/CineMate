package com.cinemate.app.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import com.google.firebase.Timestamp

@Parcelize
data class Review(
    val comentario: String,
    val dataCriacao: Timestamp,
    val idFilme: String,
    val idUsuario: String,
    val nota: Int,
    val nomeUsuario: String,
    val id: String? = null
) : Parcelable {
    fun toMap(): Map<String, Any> {
        return mapOf(
            "comentario" to comentario,
            "data_criacao" to dataCriacao,
            "id_filme" to idFilme,
            "id_usuario" to idUsuario,
            "nota" to nota,
            "nome_usuario" to nomeUsuario
        )
    }
}








