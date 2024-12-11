package com.cinemate.app.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import com.google.firebase.Timestamp

@Parcelize
data class Review(
    val comentario: String = "",
    val dataCriacao: Long = System.currentTimeMillis(),
    val idFilme: String = "",
    val idUsuario: String = "",
    val nota: Int = 0,
    val nomeUsuario: String = "",
    val id: String = ""
) : Parcelable {
    fun toTimestamp(): Timestamp = Timestamp(dataCriacao / 1000, (dataCriacao % 1000).toInt())
}



