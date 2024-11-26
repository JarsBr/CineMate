package com.example.cinemate

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class CadastroActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_cadastro)

        // Configurações de janela
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.cadastro)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Inicializando FirebaseAuth e Firestore
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        // Referências dos campos
        val nomeField = findViewById<EditText>(R.id.etNome)
        val emailField = findViewById<EditText>(R.id.etEmail)
        val senhaField = findViewById<EditText>(R.id.etSenha)
        val dataNascimentoField = findViewById<EditText>(R.id.etDataNascimento)
        val btnCadastro = findViewById<Button>(R.id.btnCadastro)

        btnCadastro.setOnClickListener {
            val nome = nomeField.text.toString().trim()
            val email = emailField.text.toString().trim()
            val senha = senhaField.text.toString().trim()
            val dataNascimento = dataNascimentoField.text.toString().trim()

            if (nome.isEmpty() || email.isEmpty() || senha.isEmpty() || dataNascimento.isEmpty()) {
                Toast.makeText(this, "Por favor, preencha todos os campos!", Toast.LENGTH_SHORT).show()
            } else {
                cadastrarUsuario(email, senha, nome, dataNascimento)
            }
        }
    }

    private fun cadastrarUsuario(email: String, senha: String, nome: String, dataNascimento: String) {
        auth.createUserWithEmailAndPassword(email, senha)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val userId = auth.currentUser?.uid
                    if (userId != null) {
                        salvarDadosNoFirestore(userId, nome, email, dataNascimento)
                    }
                } else {
                    Toast.makeText(this, "Erro ao cadastrar: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun salvarDadosNoFirestore(userId: String, nome: String, email: String, dataNascimento: String) {
        val userMap = hashMapOf(
            "nome" to nome,
            "email" to email,
            "dataNascimento" to dataNascimento,
            "role" to "user" // Padrão como usuário comum
        )

        firestore.collection("usuarios").document(userId)
            .set(userMap)
            .addOnSuccessListener {
                Toast.makeText(this, "Cadastro realizado com sucesso!", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Erro ao salvar dados: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
