package com.cinemate.app.ui.fragments.login

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.cinemate.app.R
import com.cinemate.app.data.repositories.AuthRepository
import com.cinemate.app.databinding.FragmentLoginBinding
import com.cinemate.app.ui.activities.AdminDashboardActivity
import com.cinemate.app.ui.activities.UserDashboardActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private lateinit var authRepository: AuthRepository

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        authRepository = AuthRepository(FirebaseAuth.getInstance(), FirebaseFirestore.getInstance())

        binding.forgotPassword.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_esqueceuSenhaFragment)
        }

        binding.registerText.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_cadastroFragment)
        }

        binding.btnLogin.setOnClickListener {
            handleLogin()
        }
    }

    private fun handleLogin() {
        val email = binding.emailInput.text.toString().trim()
        val password = binding.passwordInput.text.toString().trim()

        if (email.isEmpty()) {
            binding.emailInput.error = "Por favor, insira seu email"
            binding.emailInput.requestFocus()
            return
        }

        if (password.isEmpty()) {
            binding.passwordInput.error = "Por favor, insira sua senha"
            binding.passwordInput.requestFocus()
            return
        }

        showLoading(true)

        lifecycleScope.launch {
            val result = authRepository.loginAndCheckUserType(email, password)
            val authResult = result.first
            val userType = result.second

            showLoading(false)

            if (authResult != null && userType != null) {
                handleUserNavigation(userType)
            } else {
                Toast.makeText(context, "Falha no login. Verifique suas credenciais.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun handleUserNavigation(userType: String) {
        when (userType) {
            "admin" -> {
                Toast.makeText(context, "Bem-vindo, Admin!", Toast.LENGTH_SHORT).show()
                startActivity(Intent(requireContext(), AdminDashboardActivity::class.java))
                requireActivity().finish()
            }
            "user" -> {
                Toast.makeText(context, "Bem-vindo, Usuário!", Toast.LENGTH_SHORT).show()
                startActivity(Intent(requireContext(), UserDashboardActivity::class.java))
                requireActivity().finish()
            }
            else -> {
                Toast.makeText(context, "Tipo de usuário desconhecido.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showLoading(show: Boolean) {
        binding.loadingSpinner.visibility = if (show) View.VISIBLE else View.GONE
        binding.btnLogin.isEnabled = !show
        binding.registerText.isEnabled = !show
        binding.forgotPassword.isEnabled = !show
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
