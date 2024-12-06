package com.cinemate.app.ui.fragments.admin

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.cinemate.app.R
import com.cinemate.app.data.models.Movie
import com.cinemate.app.data.repositories.MovieRepository
import com.cinemate.app.databinding.FragmentFilmesCadastradosBinding
import com.cinemate.app.ui.adapters.MoviesAdapter
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

class FilmesCadastradosFragment : Fragment() {

    private var _binding: FragmentFilmesCadastradosBinding? = null
    private val binding get() = _binding!!

    private lateinit var movieRepository: MovieRepository
    private lateinit var moviesAdapter: MoviesAdapter.MoviesListAdapter
    private var allMovies: List<Movie> = listOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFilmesCadastradosBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        movieRepository = MovieRepository(FirebaseFirestore.getInstance())

        setupRecyclerView()
        setupSearch()

        binding.btnAdd.setOnClickListener {
            findNavController().navigate(R.id.action_filmesCadastradosFragment_to_adicionarFilmeFragment)
        }

        binding.btnSugestoes.setOnClickListener {
            findNavController().navigate(R.id.action_filmesCadastradosFragment_to_sugestoesFragment)
        }

        fetchMovies()
    }

    private fun setupRecyclerView() {
        moviesAdapter = MoviesAdapter.MoviesListAdapter()
        binding.recyclerViewFilmes.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = moviesAdapter
        }
    }

    private fun setupSearch() {
        binding.searchInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filterMovies(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }


    private fun filterMovies(query: String) {
        val filteredMovies = if (query.isEmpty()) {
            allMovies
        } else {
            allMovies.filter { it.titulo.contains(query, ignoreCase = true) }
        }

        if (filteredMovies.isEmpty()) {
            showNoMoviesCard()
        } else {
            showRecyclerView()
        }

        moviesAdapter.submitList(filteredMovies)
    }



    private fun fetchMovies() {
        lifecycleScope.launch {
            try {
                val movies = movieRepository.getMovies()
                if (!movies.isNullOrEmpty()) {
                    allMovies = movies
                    moviesAdapter.submitList(allMovies)
                    showRecyclerView()
                } else {
                    allMovies = listOf()
                    moviesAdapter.submitList(allMovies)
                    showNoMoviesCard()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                showNoMoviesCard()
            }
        }
    }

    private fun showRecyclerView() {
        binding.recyclerViewFilmes.visibility = View.VISIBLE
        binding.noMoviesCard.visibility = View.INVISIBLE
    }

    private fun showNoMoviesCard() {
        binding.recyclerViewFilmes.visibility = View.INVISIBLE
        binding.noMoviesCard.visibility = View.VISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
