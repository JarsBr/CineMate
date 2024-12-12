package com.cinemate.app.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore

class AuthViewModel : ViewModel() {
    val user = MutableLiveData<FirebaseUser?>()
    val userDetails = MutableLiveData<Map<String, Any?>>()
    val userDocumentId = MutableLiveData<String>()

    init {
        user.value = FirebaseAuth.getInstance().currentUser
        fetchUserDetails()
    }

    fun updateUserFavorites(updatedFavorites: List<String>, callback: (Boolean) -> Unit) {
        val userId = userDocumentId.value ?: return callback(false)
        val userDocument = FirebaseFirestore.getInstance().collection("usuarios").document(userId)

        userDocument.update("filmes_favoritos", updatedFavorites)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    fetchUserDetails()
                }
                callback(task.isSuccessful)
            }
    }

    fun fetchUserDetails() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        FirebaseFirestore.getInstance().collection("usuarios").document(userId)
            .addSnapshotListener { document, _ ->
                if (document != null && document.exists()) {
                    userDetails.value = document.data
                }
            }
    }

    fun getCurrentUserId(): String? {
        return FirebaseAuth.getInstance().currentUser?.uid
    }

    fun updateProfile(newName: String, newDataNascimento: String, callback: (Boolean) -> Unit) {
        val userId = userDocumentId.value ?: return callback(false)
        val userDocument = FirebaseFirestore.getInstance().collection("usuarios").document(userId)

        val updatedData = mapOf(
            "nome" to newName,
            "data_nascimento" to newDataNascimento
        )

        userDocument.update(updatedData).addOnCompleteListener { firestoreTask ->
            if (firestoreTask.isSuccessful) {
                fetchUserDetails()
            }
            callback(firestoreTask.isSuccessful)
        }
    }

    fun reauthenticateAndUpdateProfile(password: String, newName: String, newDataNascimento: String, callback: (Boolean) -> Unit) {
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser == null) {
            callback(false)
            return
        }

        val credential = EmailAuthProvider.getCredential(currentUser.email!!, password)
        currentUser.reauthenticate(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                updateProfile(newName, newDataNascimento, callback)
            } else {
                callback(false)
            }
        }
    }

    fun refreshUserData() {
        user.value = FirebaseAuth.getInstance().currentUser
        fetchUserDetails()
    }
}

