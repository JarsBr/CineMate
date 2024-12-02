package com.cinemate.app.ui.fragments.login

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.cinemate.app.R
import java.util.*

class CadastroFragment : Fragment() {

    private lateinit var nameInput: EditText
    private lateinit var emailInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var dateInput: EditText
    private lateinit var registerButton: Button
    private lateinit var loginText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_cadastro, container, false)

        nameInput = view.findViewById(R.id.nameInput)
        emailInput = view.findViewById(R.id.emailInput)
        passwordInput = view.findViewById(R.id.passwordInput)
        dateInput = view.findViewById(R.id.dateInput)
        registerButton = view.findViewById(R.id.btnCadastro)
        loginText = view.findViewById(R.id.loginText)

        dateInput.setOnClickListener {
            openDatePicker()
        }

        // Configurar a ação do botão Cadastre-se
        registerButton.setOnClickListener {
            handleRegister()
        }

        loginText.setOnClickListener {
            Toast.makeText(context, "Redirecionar para o Fragmento de Login", Toast.LENGTH_SHORT).show()
        }

        return view
    }

    private fun openDatePicker() {
        // Obtém a data atual
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            R.style.CustomDatePicker,
            { _, selectedYear, selectedMonth, selectedDay ->
                val formattedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                dateInput.setText(formattedDate)
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
        val name = nameInput.text.toString().trim()
        val email = emailInput.text.toString().trim()
        val password = passwordInput.text.toString().trim()
        val dateOfBirth = dateInput.text.toString().trim()

        if (name.isEmpty()) {
            nameInput.error = "Por favor, insira seu nome"
            nameInput.requestFocus()
            return
        }

        if (email.isEmpty()) {
            emailInput.error = "Por favor, insira seu email"
            emailInput.requestFocus()
            return
        }

        if (password.isEmpty()) {
            passwordInput.error = "Por favor, insira sua senha"
            passwordInput.requestFocus()
            return
        }

        if (dateOfBirth.isEmpty()) {
            dateInput.error = "Por favor, selecione sua data de nascimento"
            dateInput.requestFocus()
            return
        }

        // implementar lógica para salvar os dados no firebase e depois enviar usuário até o login
        Toast.makeText(context, "Cadastro realizado com sucesso!", Toast.LENGTH_SHORT).show()

    }
}
