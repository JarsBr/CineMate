package com.cinemate.app.ui.adapters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.cinemate.app.R

class UsersAdapter(
    private var users: List<Map<String, Any>>
) : RecyclerView.Adapter<UsersAdapter.UserViewHolder>() {

    fun updateUsers(newUsers: List<Map<String, Any>>) {
        users = newUsers
        notifyDataSetChanged()
    }

    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val userName: TextView = itemView.findViewById(R.id.txtUserName)
        val userEmail: TextView = itemView.findViewById(R.id.txtUserEmail)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val usuario = users[position]

        holder.userName.text = usuario["nome"] as? String ?: "Nome não disponível"
        holder.userEmail.text = usuario["email"] as? String ?: "Email não disponível"

        holder.itemView.setOnClickListener {
            val bundle = Bundle().apply {
                putString("nome", usuario["nome"] as? String ?: "Nome não disponível")
                putString("email", usuario["email"] as? String ?: "Email não disponível")
                putString("data_nascimento", usuario["data_nascimento"] as? String ?: "Data não disponível")
                putString("tipo_usuario", usuario["tipo_usuario"] as? String ?: "Tipo não disponível")
                putStringArray("filmes_favoritos", (usuario["filmes_favoritos"] as? List<String>)?.toTypedArray() ?: arrayOf())
            }
            holder.itemView.findNavController().navigate(R.id.action_usuariosCadastradosFragment_to_gestaoUsuarioFragment, bundle)
        }
    }

    override fun getItemCount(): Int = users.size
}
