package com.cinemate.app.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore

class AuthViewModel : ViewModel() {

    val user = MutableLiveData<FirebaseUser?>()
    val userDetails = MutableLiveData<Map<String, Any?>>()

    init {
        user.value = FirebaseAuth.getInstance().currentUser
        fetchUserDetails()
    }

    fun updateUserFavorites(updatedFavorites: List<String>, callback: (Boolean) -> Unit) {
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser == null) {
            callback(false)
            return
        }

        val userId = currentUser.uid
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

    fun updateProfile(newName: String, newDataNascimento: String, callback: (Boolean) -> Unit) {
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser == null) {
            callback(false)
            return
        }

        val userId = currentUser.uid
        val userDocument = FirebaseFirestore.getInstance().collection("usuarios").document(userId)

        val updatedData = mapOf(
            "nome" to newName,
            "data_nascimento" to newDataNascimento
        )

        userDocument.update(updatedData).addOnCompleteListener { firestoreTask ->
            if (firestoreTask.isSuccessful) {
                fetchUserDetails() // Atualiza os dados depois de salvar
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

        // Reautentica o usuário com a senha fornecida
        val credential = EmailAuthProvider.getCredential(currentUser.email!!, password)
        currentUser.reauthenticate(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // Se a reautenticação for bem-sucedida, atualiza o perfil
                updateProfile(newName, newDataNascimento, callback)
            } else {
                callback(false) // Reautenticação falhou
            }
        }
    }

    fun refreshUserData() {
        val currentUser = FirebaseAuth.getInstance().currentUser
        user.value = currentUser // Atualiza o LiveData com o usuário atual
        fetchUserDetails() // Sempre recarrega os detalhes do usuário
    }
}
