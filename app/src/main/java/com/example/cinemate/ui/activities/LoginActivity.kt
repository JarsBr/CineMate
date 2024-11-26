package com.example.cinemate

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        val emailField = findViewById<EditText>(R.id.etEmailLogin)
        val senhaField = findViewById<EditText>(R.id.etSenhaLogin)
        val btnLogin = findViewById<Button>(R.id.btnLogin)

        btnLogin.setOnClickListener {
            val email = emailField.text.toString().trim()
            val senha = senhaField.text.toString().trim()

            if (email.isEmpty() || senha.isEmpty()) {
                Toast.makeText(this, "Por favor, preencha todos os campos!", Toast.LENGTH_SHORT).show()
            } else {
                autenticarUsuario(email, senha)
            }
        }
    }

    private fun autenticarUsuario(email: String, senha: String) {
        auth.signInWithEmailAndPassword(email, senha)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val userId = auth.currentUser?.uid
                    if (userId != null) {
                        verificarRole(userId)
                    }
                } else {
                    Toast.makeText(this, "Erro ao autenticar: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun verificarRole(userId: String) {
        firestore.collection("usuarios").document(userId).get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val role = document.getString("role")
                    if (role == "admin") {
                        // Direciona para a tela de admin
                        startActivity(Intent(this, PainelAdminActivity::class.java))
                    } else {
                        // Direciona para a tela de usuário comum
                        startActivity(Intent(this, FeedActivity::class.java))
                    }
                    finish()
                } else {
                    Toast.makeText(this, "Usuário não encontrado no banco de dados!", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Erro ao verificar dados: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
