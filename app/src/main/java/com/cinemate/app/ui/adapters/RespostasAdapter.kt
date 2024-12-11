package com.cinemate.app.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cinemate.app.R
import com.cinemate.app.data.models.Response
import androidx.recyclerview.widget.ItemTouchHelper

class RespostasAdapter(
    private var respostas: List<Response>
) : RecyclerView.Adapter<RespostasAdapter.RespostaViewHolder>() {

    fun updateRespostas(newRespostas: List<Response>) {
        respostas = newRespostas
        notifyDataSetChanged()
    }

    inner class RespostaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvComentario: TextView = itemView.findViewById(R.id.tvRespostaComentario)
        val tvData: TextView = itemView.findViewById(R.id.tvRespostaData)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RespostaViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_resposta, parent, false)
        return RespostaViewHolder(view)
    }

    override fun onBindViewHolder(holder: RespostaViewHolder, position: Int) {
        val resposta = respostas[position]
        holder.tvComentario.text = resposta.comentario
        holder.tvData.text = "Data: ${resposta.dataCriacao.toDate()}"
    }

    fun getRespostaAt(position: Int): Response {
        if (position in 0 until respostas.size) {
            return respostas[position]
        } else {
            throw IndexOutOfBoundsException("Invalid position: $position")
        }
    }



    fun removeAt(position: Int) {
        val newRespostas = respostas.toMutableList()
        newRespostas.removeAt(position)
        respostas = newRespostas
        notifyItemRemoved(position)
    }


    override fun getItemCount(): Int = respostas.size


}
