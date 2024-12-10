package com.cinemate.app.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.functions.FirebaseFunctions

class UserViewModel : ViewModel() {

    private val _users = MutableLiveData<List<Map<String, Any>>>()
    val users: LiveData<List<Map<String, Any>>> get() = _users

    fun fetchUsers() {
        val db = FirebaseFirestore.getInstance()
        db.collection("usuarios") // Supondo que a coleção se chame 'usuarios'
            .get()
            .addOnSuccessListener { result ->
                val usersList = mutableListOf<Map<String, Any>>()
                for (document in result) {
                    val user = document.data // Obtém os dados do documento
                    usersList.add(user)
                }
                _users.value = usersList
            }
            .addOnFailureListener { exception ->
            }
    }
}

