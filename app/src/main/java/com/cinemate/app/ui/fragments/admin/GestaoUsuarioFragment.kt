package com.cinemate.app.ui.fragments.admin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.cinemate.app.R

class GestaoUsuarioFragment : Fragment() {

    private lateinit var txtUserName: TextView
    private lateinit var txtUserEmail: TextView
    private lateinit var txtUserBirthDate: TextView
    private lateinit var txtUserType: TextView
    private lateinit var txtUserFavorites: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_gestao_usuario, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inicializando as views
        txtUserName = view.findViewById(R.id.txtUserName)
        txtUserEmail = view.findViewById(R.id.txtUserEmail)
        txtUserBirthDate = view.findViewById(R.id.txtUserBirthDate)
        txtUserType = view.findViewById(R.id.txtUserType)
        txtUserFavorites = view.findViewById(R.id.txtUserFavorites)

        // Recupera os parâmetros passados pelo Bundle
        val nome = arguments?.getString("nome") ?: "Nome não disponível"
        val email = arguments?.getString("email") ?: "Email não disponível"
        val dataNascimento = arguments?.getString("data_nascimento") ?: "Data não disponível"
        val tipoUsuario = arguments?.getString("tipo_usuario") ?: "Tipo não disponível"
        val filmesFavoritos = arguments?.getStringArray("filmes_favoritos")?.joinToString("\n") ?: "Nenhum filme favorito"



        // Exibe os dados no layout
        txtUserName.text = "Nome: $nome"
        txtUserEmail.text = "Email: $email"
        txtUserBirthDate.text = "Data de Nascimento: $dataNascimento"
        txtUserType.text = "Tipo de Usuário: $tipoUsuario"
        txtUserFavorites.text = "Filmes Favoritos:\n$filmesFavoritos"
    }
}
