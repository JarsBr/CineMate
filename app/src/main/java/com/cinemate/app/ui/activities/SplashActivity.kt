package com.cinemate.app.ui.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.cinemate.app.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        window.statusBarColor = android.graphics.Color.TRANSPARENT
        setContentView(R.layout.activity_splash)

        checkUserSession()
    }

    private fun checkUserSession() {
        val auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser

        if (currentUser != null) {
            val userId = currentUser.uid
            val db = FirebaseFirestore.getInstance()
            val userRef = db.collection("usuarios").document(userId)

            userRef.get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        val userType = document.getString("tipo_usuario")
                        when (userType) {
                            "admin" -> {
                                val intent = Intent(this, AdminDashboardActivity::class.java)
                                startActivity(intent)
                                finish()
                            }
                            "user" -> {
                                val intent = Intent(this, UserDashboardActivity::class.java)
                                startActivity(intent)
                                finish()
                            }
                            else -> {
                                auth.signOut()
                                goToLogin()
                            }
                        }
                    } else {
                        auth.signOut()
                        goToLogin()
                    }
                }
                .addOnFailureListener {
                    auth.signOut()
                    goToLogin()
                }
        } else {
            goToLogin()
        }
    }

    private fun goToLogin() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}
