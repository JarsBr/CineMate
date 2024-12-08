package com.cinemate.app.ui.fragments.main

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.cinemate.app.R
import com.cinemate.app.databinding.FragmentPerfilBinding
import com.cinemate.app.ui.activities.MainActivity
import com.cinemate.app.viewModel.AuthViewModel
import com.google.firebase.auth.FirebaseAuth

class PerfilFragment : Fragment() {

    private var _binding: FragmentPerfilBinding? = null
    private val binding get() = _binding!!
    private val authViewModel: AuthViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPerfilBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        authViewModel.userDetails.observe(viewLifecycleOwner) { userDetails ->
            binding.txtNome.text = userDetails["nome"]?.toString() ?: "Nome não disponível"
            binding.txtDataNascimento.text = userDetails["data_nascimento"]?.toString() ?: "Data de nascimento não disponível"
        }

        binding.btnAlterarDados.setOnClickListener {
            openAlterarDadosFragment()
        }

        binding.btnLogout.setOnClickListener {
            logout()
        }
    }

    private fun openAlterarDadosFragment() {
        val alterarDadosFragment = AlterarDadosFragment()
        requireActivity().supportFragmentManager.beginTransaction()
            .add(R.id.fragmentContainer, alterarDadosFragment, "AlterarDados")
            .addToBackStack("AlterarDados")
            .commit()
    }

    private fun logout() {
        FirebaseAuth.getInstance().signOut()
        val intent = Intent(requireContext(), MainActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

