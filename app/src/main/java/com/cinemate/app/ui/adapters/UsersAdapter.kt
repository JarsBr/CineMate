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

    // Função para atualizar a lista de usuários
    fun updateUsers(newUsers: List<Map<String, Any>>) {
        users = newUsers
        notifyDataSetChanged()
    }

    // ViewHolder que mantém as referências das views de cada item
    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val userName: TextView = itemView.findViewById(R.id.txtUserName)
        val userEmail: TextView = itemView.findViewById(R.id.txtUserEmail)
    }

    // Criação do ViewHolder para cada item
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
        return UserViewHolder(view)
    }

    // Vincula os dados do usuário à view do item
    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val usuario = users[position]

        // Exibe os dados do usuário
        holder.userName.text = usuario["nome"] as? String ?: "Nome não disponível"
        holder.userEmail.text = usuario["email"] as? String ?: "Email não disponível"

        // Ação ao clicar no item da lista
        holder.itemView.setOnClickListener {
            // Criação do bundle com os dados do usuário
            val bundle = Bundle().apply {
                putString("nome", usuario["nome"] as? String ?: "Nome não disponível")
                putString("email", usuario["email"] as? String ?: "Email não disponível")
                putString("data_nascimento", usuario["data_nascimento"] as? String ?: "Data não disponível")
                putString("tipo_usuario", usuario["tipo_usuario"] as? String ?: "Tipo não disponível")
                putStringArray("filmes_favoritos", (usuario["filmes_favoritos"] as? List<String>)?.toTypedArray() ?: arrayOf())
            }
            // Navega para o GestaoUsuarioFragment passando os dados
            holder.itemView.findNavController().navigate(R.id.action_usuariosCadastradosFragment_to_gestaoUsuarioFragment, bundle)
        }
    }

    // Retorna o número de itens na lista de usuários
    override fun getItemCount(): Int = users.size
}
