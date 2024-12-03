package com.cinemate.app.ui.fragments.admin

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.cinemate.app.R
import com.cinemate.app.data.repositories.AuthRepository
import com.cinemate.app.databinding.FragmentPainelAdminBinding
import com.cinemate.app.ui.activities.MainActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class PainelAdminFragment : Fragment() {

    private var _binding: FragmentPainelAdminBinding? = null
    private val binding get() = _binding!!

    // Repositório de autenticação
    private lateinit var authRepository: AuthRepository

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPainelAdminBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        authRepository = AuthRepository(FirebaseAuth.getInstance(), FirebaseFirestore.getInstance())

        binding.btnFilmes.setOnClickListener {
            findNavController().navigate(R.id.action_painelAdminFragment_to_filmesCadastradosFragment)
        }

        binding.btnUsuarios.setOnClickListener {
            findNavController().navigate(R.id.action_painelAdminFragment_to_usuariosCadastradosFragment)
        }

        binding.btnLogout.setOnClickListener {
            logoutAdmin()
        }
    }

    private fun logoutAdmin() {
        authRepository.logout() // Usando o método logout do repositório
        val intent = Intent(requireContext(), MainActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
