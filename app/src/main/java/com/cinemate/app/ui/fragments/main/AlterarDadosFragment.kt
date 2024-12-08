package com.cinemate.app.ui.fragments.main

import android.os.Bundle
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.cinemate.app.R
import com.cinemate.app.databinding.FragmentAlterarDadosBinding
import com.cinemate.app.viewModel.AuthViewModel
import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.util.*

class AlterarDadosFragment : Fragment() {

    private var _binding: FragmentAlterarDadosBinding? = null
    private val binding get() = _binding!!
    private val authViewModel: AuthViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAlterarDadosBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Atualiza os dados do usuário antes de exibir
        authViewModel.refreshUserData()

        // Observa as alterações nos dados do usuário
        authViewModel.userDetails.observe(viewLifecycleOwner) { userDetails ->
            binding.nomeInput.setText(userDetails["nome"]?.toString() ?: "")

            // Exibir a data de nascimento formatada
            val dataNascimento = userDetails["data_nascimento"]?.toString()
            binding.dataNascimentoInput.setText(dataNascimento ?: "")
        }

        binding.btnAlterarDados.setOnClickListener {
            val newName = binding.nomeInput.text.toString()
            val newDataNascimento = binding.dataNascimentoInput.text.toString()
            val password = binding.senhaInput.text.toString()

            if (newName.isEmpty() || newDataNascimento.isEmpty() || password.isEmpty()) {
                Toast.makeText(context, "Por favor, preencha todos os campos.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Reautentica e atualiza os dados
            authViewModel.reauthenticateAndUpdateProfile(password, newName, newDataNascimento) { success ->
                if (success) {
                    Toast.makeText(context, "Dados atualizados com sucesso!", Toast.LENGTH_SHORT).show()
                    authViewModel.refreshUserData() // Atualiza os dados do usuário após a alteração
                    requireActivity().supportFragmentManager.popBackStack()
                } else {
                    Toast.makeText(context, "Erro ao atualizar os dados ou senha incorreta.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
