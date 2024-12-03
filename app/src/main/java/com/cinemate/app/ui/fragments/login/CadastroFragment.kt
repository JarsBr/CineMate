package com.cinemate.app.ui.fragments.login

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.cinemate.app.R
import com.cinemate.app.data.repositories.AuthRepository
import com.cinemate.app.databinding.FragmentCadastroBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.*

class CadastroFragment : Fragment() {

    private var _binding: FragmentCadastroBinding? = null
    private val binding get() = _binding!!

    // Repositórios
    private lateinit var authRepository: AuthRepository

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCadastroBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inicializando o AuthRepository
        authRepository = AuthRepository(FirebaseAuth.getInstance(), FirebaseFirestore.getInstance())

        // Configurações para o campo de data
        binding.dateInput.setOnClickListener {
            openDatePicker()
        }

        // Configurar a ação do botão "Cadastre-se"
        binding.btnCadastro.setOnClickListener {
            handleRegister()
        }

        // Configurar a ação do texto "Login"
        binding.loginText.setOnClickListener {
            val navController = requireActivity().findNavController(R.id.nav_host_fragment_main)
            navController.navigate(R.id.action_cadastroFragment_to_loginFragment)
        }
    }

    private fun openDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            R.style.CustomDatePicker,
            { _, selectedYear, selectedMonth, selectedDay ->
                val formattedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                binding.dateInput.setText(formattedDate)
            },
            year,
            month,
            day
        )

        datePickerDialog.window?.apply {
            setDimAmount(0.95f)
        }

        datePickerDialog.show()
    }

    private fun handleRegister() {
        val name = binding.nameInput.text.toString().trim()
        val email = binding.emailInput.text.toString().trim()
        val password = binding.passwordInput.text.toString().trim()
        val dateOfBirth = binding.dateInput.text.toString().trim()

        if (name.isEmpty()) {
            binding.nameInput.error = "Por favor, insira seu nome"
            binding.nameInput.requestFocus()
            return
        }

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

        if (dateOfBirth.isEmpty()) {
            binding.dateInput.error = "Por favor, selecione sua data de nascimento"
            binding.dateInput.requestFocus()
            return
        }

        // Mostrando loading
        showLoading(true)

        // Registrando usuário usando AuthRepository
        lifecycleScope.launch {
            val result = authRepository.register(email, password)
            if (result != null) {
                val userId = result.user?.uid
                if (userId != null) {
                    saveUserDataToFirestore(userId, name, email, dateOfBirth)
                } else {
                    showLoading(false)
                    Toast.makeText(context, "Erro ao obter o ID do usuário.", Toast.LENGTH_SHORT).show()
                }
            } else {
                showLoading(false)
                Toast.makeText(context, "Erro ao registrar. Tente novamente.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private suspend fun saveUserDataToFirestore(userId: String, name: String, email: String, dateOfBirth: String) {
        val userData = mapOf(
            "nome" to name,
            "email" to email,
            "data_nascimento" to dateOfBirth,
            "tipo_usuario" to "user",
            "filmes_favoritos" to emptyList<String>()
        )

        try {
            val db = FirebaseFirestore.getInstance()
            db.collection("usuarios").document(userId).set(userData).await()
            Toast.makeText(context, "Cadastro realizado com sucesso!", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_cadastroFragment_to_loginFragment)
        } catch (e: Exception) {
            Toast.makeText(context, "Erro ao salvar dados: ${e.message}", Toast.LENGTH_SHORT).show()
        } finally {
            showLoading(false)
        }
    }

    private fun showLoading(show: Boolean) {
        binding.loadingSpinner.visibility = if (show) View.VISIBLE else View.GONE
        binding.btnCadastro.isEnabled = !show
        binding.loginText.isEnabled = !show
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
