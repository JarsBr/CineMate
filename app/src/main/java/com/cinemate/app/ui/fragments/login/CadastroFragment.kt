package com.cinemate.app.ui.fragments.login

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.cinemate.app.R
import com.cinemate.app.databinding.FragmentCadastroBinding
import java.util.*

class CadastroFragment : Fragment() {

    private var _binding: FragmentCadastroBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCadastroBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
            val navController = requireActivity().findNavController(R.id.nav_host_fragment)
            navController.navigate(R.id.action_cadastroFragment_to_loginFragment)
        }

    }

    private fun openDatePicker() {
        // Obtém a data atual
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        // Configura o DatePickerDialog
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

        // Exibe o DatePicker
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

        // Implementar lógica para salvar os dados no Firebase e redirecionar para o Login
        Toast.makeText(context, "Cadastro realizado com sucesso!", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
