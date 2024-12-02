package com.cinemate.app.ui.fragments.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cinemate.app.databinding.FragmentEsqueceuSenhaBinding

class EsqueceuSenhaFragment : Fragment() {

    private var _binding: FragmentEsqueceuSenhaBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEsqueceuSenhaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Configurar a ação do botão "Cadastre-se"
        binding.btnEnviarCod.setOnClickListener {
            handleRegister()
        }
    }


    private fun handleRegister() {
        val email = binding.emailInput.text.toString().trim()

        if (email.isEmpty()) {
            binding.emailInput.error = "Por favor, insira seu email"
            binding.emailInput.requestFocus()
            return
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}