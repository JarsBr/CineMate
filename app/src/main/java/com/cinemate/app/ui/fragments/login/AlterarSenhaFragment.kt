package com.cinemate.app.ui.fragments.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cinemate.app.R
import com.cinemate.app.databinding.FragmentAlterarSenhaBinding
import com.cinemate.app.databinding.FragmentConfirmaCodigoBinding

class AlterarSenhaFragment : Fragment() {

        private var _binding: FragmentAlterarSenhaBinding? = null
        private val binding get() = _binding!!

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
        }

        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View {
            _binding = FragmentAlterarSenhaBinding.inflate(inflater, container, false)
            return binding.root
        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)

            binding.btnNovaSenha.setOnClickListener {
                handleRegister()
            }
        }


        private fun handleRegister() {
            val senha = binding.senhaInput.text.toString().trim()
            val confirmaSenha = binding.confirmaSenhaInput.text.toString().trim()

            if (senha.isEmpty()) {
                binding.senhaInput.error = "Por favor, insira sua senha"
                binding.senhaInput.requestFocus()
                return
            }

            if (confirmaSenha.isEmpty()) {
                binding.confirmaSenhaInput.error = "Por favor, confirme sua senha"
                binding.confirmaSenhaInput.requestFocus()
                return
            }

        }

        override fun onDestroyView() {
            super.onDestroyView()
            _binding = null
        }

    }