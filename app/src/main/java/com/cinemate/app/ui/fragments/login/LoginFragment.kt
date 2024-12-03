package com.cinemate.app.ui.fragments.login

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.cinemate.app.R
import com.cinemate.app.databinding.FragmentLoginBinding
import com.cinemate.app.ui.activities.AdminDashboardActivity
import com.cinemate.app.ui.activities.UserDashboardActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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

        val auth = FirebaseAuth.getInstance()
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val userId = auth.currentUser?.uid
                    if (userId != null) {
                        checkUserType(userId)
                    } else {
                        showLoading(false)
                        Toast.makeText(context, "Erro ao obter dados do usuário.", Toast.LENGTH_SHORT).show()
                    }
                } else {

                    val errorMessage = task.exception?.message ?: "Erro desconhecido"
                    Toast.makeText(context, "Falha no login: $errorMessage", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun checkUserType(userId: String) {
        val db = FirebaseFirestore.getInstance()
        val userRef = db.collection("usuarios").document(userId)

        userRef.get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val userType = document.getString("tipo_usuario")
                    when (userType) {
                        "admin" -> {
                            Toast.makeText(context, "Bem-vindo, Admin!", Toast.LENGTH_SHORT).show()
                            val intent = Intent(requireContext(), AdminDashboardActivity::class.java)
                            startActivity(intent)
                            requireActivity().finish()
                        }
                        "user" -> {
                            Toast.makeText(context, "Bem-vindo, Usuário!", Toast.LENGTH_SHORT).show()
                            val intent = Intent(requireContext(), UserDashboardActivity::class.java)
                            startActivity(intent)
                            requireActivity().finish()
                        }
                        else -> {
                            Toast.makeText(context, "Tipo de usuário desconhecido.", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Toast.makeText(context, "Usuário não encontrado no banco de dados.", Toast.LENGTH_SHORT).show()
                }
                showLoading(false)
            }
            .addOnFailureListener { exception ->
                showLoading(false)
                Toast.makeText(context, "Erro ao acessar o Firestore: ${exception.message}", Toast.LENGTH_SHORT).show()
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
}


