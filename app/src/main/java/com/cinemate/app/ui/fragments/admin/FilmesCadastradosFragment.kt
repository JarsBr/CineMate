package com.cinemate.app.ui.fragments.admin

import android.os.Bundle
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

        binding.btnAdd.setOnClickListener {
            findNavController().navigate(R.id.action_filmesCadastradosFragment_to_gestaoFilmeFragment)
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

    private fun fetchMovies() {
        lifecycleScope.launch {
            try {
                val movies = movieRepository.getMovies()
                if (movies != null && movies.isNotEmpty()) {
                    moviesAdapter.submitList(movies)
                } else {
                    Toast.makeText(requireContext(), "Nenhum filme encontrado.", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(requireContext(), "Erro ao buscar filmes: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
