package com.cinemate.app.ui.fragments.login

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

class LoginFragment : Fragment() {

    private lateinit var emailInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var loginButton: Button
    private lateinit var forgotPasswordText: TextView
    private lateinit var registerText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_login, container, false)

        emailInput = view.findViewById(R.id.emailInput)
        passwordInput = view.findViewById(R.id.passwordInput)
        loginButton = view.findViewById(R.id.btnLogin)
        forgotPasswordText = view.findViewById(R.id.forgotPassword)
        registerText = view.findViewById(R.id.registerText)

        // ação de login
        loginButton.setOnClickListener {
            handleLogin()
        }

        // enviar ao EnviarCodigoVerificacaoFragment
        forgotPasswordText.setOnClickListener {
            Toast.makeText(context, "Redirecionar para a recuperação de senha", Toast.LENGTH_SHORT).show()
        }

        // enviar ao CadastroFragment
        registerText.setOnClickListener {
            Toast.makeText(context, "Redirecionar para o cadastro", Toast.LENGTH_SHORT).show()
        }

        return view
    }

    private fun handleLogin() {
        val email = emailInput.text.toString().trim()
        val password = passwordInput.text.toString().trim()

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

        //  substituir pelo authentication do firebase
        if (email == "admin@example.com" && password == "password") {
            Toast.makeText(context, "Login bem-sucedido!", Toast.LENGTH_SHORT).show()
            // fazer redirecionar para outra activity (UserDashboardActivity ou AdminDashboardActivity)
        } else {
            Toast.makeText(context, "Email ou senha inválidos", Toast.LENGTH_SHORT).show()
        }
    }
}
