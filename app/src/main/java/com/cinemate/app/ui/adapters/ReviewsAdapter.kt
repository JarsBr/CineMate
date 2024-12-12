package com.cinemate.app.ui.adapters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cinemate.app.R
import com.cinemate.app.data.models.Review
import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.*

class ReviewsAdapter(
    private var reviews: List<Review>,
    private val onReviewClick: (Review) -> Unit
) : RecyclerView.Adapter<ReviewsAdapter.ReviewViewHolder>() {

    fun updateReviews(newReviews: List<Review>) {
        reviews = newReviews
        notifyDataSetChanged()
    }

    inner class ReviewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvComentario: TextView = itemView.findViewById(R.id.tvComentario)
        val tvNota: TextView = itemView.findViewById(R.id.tvNota)
        val tvDataCriacao: TextView = itemView.findViewById(R.id.tvDataCriacao)
        val tvNomeUsuario: TextView = itemView.findViewById(R.id.tvNomeUsuario)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_review, parent, false)
        return ReviewViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        val review = reviews[position]
        holder.tvComentario.text = review.comentario
        holder.tvNota.text = "Nota: ${review.nota}"
        holder.tvNomeUsuario.text = "Por: ${review.nomeUsuario}"

        val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        val date = review.dataCriacao.toDate()
        holder.tvDataCriacao.text = "Data: ${dateFormat.format(date)}"

        holder.itemView.setOnClickListener { onReviewClick(review) }
    }

    override fun getItemCount(): Int = reviews.size
}
