package com.example.cinemate

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Referenciando os botões
        val buttonLogin = findViewById<Button>(R.id.buttonLogin)
        val buttonCadastro = findViewById<Button>(R.id.buttonCadastro)

        // Configurando clique no botão Login
        buttonLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java) // Substitua pelo nome correto da Activity de login
            startActivity(intent)
        }

        // Configurando clique no botão Cadastro
        buttonCadastro.setOnClickListener {
            val intent = Intent(this, CadastroActivity::class.java) // Substitua pelo nome correto da Activity de cadastro
            startActivity(intent)
        }

    }
}