package com.cinemate.app.ui.fragments.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.cinemate.app.R
import com.cinemate.app.databinding.FragmentEsqueceuSenhaBinding
import com.google.firebase.auth.FirebaseAuth

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

        binding.btnEnviar.setOnClickListener {
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

        val auth = FirebaseAuth.getInstance()
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(requireContext(), "Email de redefinição enviado!", Toast.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.action_esqueceuSenhaFragment_to_loginFragment)
                } else {
                    Toast.makeText(requireContext(), "Erro: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}