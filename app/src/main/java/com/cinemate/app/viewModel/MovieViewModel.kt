package com.cinemate.app.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cinemate.app.data.models.Movie
import com.cinemate.app.data.repositories.MovieRepository
import kotlinx.coroutines.launch

class MovieViewModel(private val movieRepository: MovieRepository) : ViewModel() {

    private val _movies = MutableLiveData<List<Movie>>()
    val movies: LiveData<List<Movie>> get() = _movies

    private val _filteredMovies = MutableLiveData<List<Movie>>()
    val filteredMovies: LiveData<List<Movie>> get() = _filteredMovies

    private val _showNoMoviesMessage = MutableLiveData<Boolean>()
    val showNoMoviesMessage: LiveData<Boolean> get() = _showNoMoviesMessage

    init {
        fetchMovies()
    }

    private fun fetchMovies() {
        viewModelScope.launch {
            try {
                val movies = movieRepository.getMovies() ?: emptyList()
                _movies.value = movies
                _filteredMovies.value = movies
                _showNoMoviesMessage.value = movies.isEmpty()
            } catch (e: Exception) {
                _movies.value = emptyList()
                _filteredMovies.value = emptyList()
                _showNoMoviesMessage.value = true
            }
        }
    }

    fun filterMovies(query: String) {
        val allMovies = _movies.value ?: emptyList()
        val filtered = if (query.isBlank()) {
            allMovies
        } else {
            allMovies.filter { it.titulo.contains(query, ignoreCase = true) }
        }
        _filteredMovies.value = filtered
        _showNoMoviesMessage.value = filtered.isEmpty()
    }
}
