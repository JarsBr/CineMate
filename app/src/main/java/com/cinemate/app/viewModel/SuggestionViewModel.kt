package com.cinemate.app.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cinemate.app.data.models.Suggestion
import com.cinemate.app.data.repositories.SuggestionRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

class SuggestionViewModel(private val repository: SuggestionRepository) : ViewModel() {

    fun saveSuggestion(suggestion: Suggestion, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        viewModelScope.launch {
            repository.addSuggestion(suggestion)
                .onSuccess { onSuccess() }
                .onFailure {
                    onFailure(it as? Exception ?: Exception(it))
                }
        }
    }

    fun deleteSuggestion(
        suggestionId: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        viewModelScope.launch {
            try {
                repository.deleteSuggestion(suggestionId)
                onSuccess()
            } catch (e: Exception) {
                onFailure(e)
            }
        }
    }
}

