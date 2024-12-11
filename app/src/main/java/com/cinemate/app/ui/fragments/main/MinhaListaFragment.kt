package com.cinemate.app.ui.fragments.main

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.cinemate.app.R
import com.cinemate.app.data.models.Movie
import com.cinemate.app.data.repositories.MovieRepository
import com.cinemate.app.databinding.FragmentFeedBinding
import com.cinemate.app.databinding.FragmentMinhaListaBinding
import com.cinemate.app.ui.adapters.MoviesAdapter
import com.cinemate.app.viewModel.AuthViewModel
import com.cinemate.app.viewModel.MovieViewModel
import com.cinemate.app.viewModel.MovieViewModelFactory
import com.google.firebase.firestore.FirebaseFirestore

class MinhaListaFragment : Fragment() {

    private var _binding: FragmentMinhaListaBinding? = null
    private val binding get() = _binding!!
    private val authViewModel: AuthViewModel by viewModels()

    private val viewModel: MovieViewModel by viewModels {
        MovieViewModelFactory(MovieRepository(FirebaseFirestore.getInstance()))
    }

    private lateinit var moviesAdapter: MoviesAdapter.MoviesListAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMinhaListaBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        if (!::moviesAdapter.isInitialized) {
            moviesAdapter = MoviesAdapter.MoviesListAdapter(
                onItemClick = { selectedMovie ->
                    openDetalhesFilmeFragment(selectedMovie)
                },
                onFavoriteClick = { movieId, isFavorite ->
                    updateFavoriteStatus(movieId, isFavorite)
                },
                layoutId = R.layout.item_movie_card_feed,
                favoriteMovies = emptyList()
            )

            binding.recyclerViewFavoritos.apply {
                layoutManager = GridLayoutManager(requireContext(), 2)
                adapter = moviesAdapter
            }
        }

        authViewModel.userDetails.observe(viewLifecycleOwner) { userDetails ->
            val favoriteMovieIds = userDetails["filmes_favoritos"] as? List<String> ?: emptyList()
            moviesAdapter.favoriteMovies = favoriteMovieIds

            viewModel.fetchFavorites(favoriteMovieIds)
        }

        viewModel.favoriteMovies.observe(viewLifecycleOwner) { favoriteMovies ->
            moviesAdapter.submitList(favoriteMovies)
        }
    }


    private fun updateFavoriteStatus(movieId: String, isFavorite: Boolean) {
        authViewModel.userDetails.value?.let { userDetails ->
            val currentFavorites = userDetails["filmes_favoritos"] as? List<String> ?: emptyList()
            val updatedFavorites = if (isFavorite) {
                currentFavorites.toMutableSet().apply { add(movieId) }.toList()
            } else {
                currentFavorites.toMutableSet().apply { remove(movieId) }.toList()
            }

            authViewModel.updateUserFavorites(updatedFavorites) { success ->
                if (success) {
                    authViewModel.userDetails.value = userDetails.toMutableMap().apply {
                        put("filmes_favoritos", updatedFavorites)
                    }

                    Toast.makeText(
                        requireContext(),
                        if (isFavorite) "Filme adicionado aos favoritos!" else "Filme removido dos favoritos!",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(requireContext(), "Erro ao atualizar favoritos.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


    private fun openDetalhesFilmeFragment(selectedMovie: Movie) {
        val detalhesFilmeFragment = DetalhesFilmeFragment().apply {
            arguments = Bundle().apply {
                putParcelable("selectedMovie", selectedMovie)
            }
        }

        requireActivity().supportFragmentManager.beginTransaction()
            .add(R.id.fragmentContainer, detalhesFilmeFragment, "DetalhesFilme")
            .addToBackStack("DetalhesFilme")
            .commit()
    }

    private fun observeViewModel() {
        viewModel.filteredMovies.observe(viewLifecycleOwner) { movies ->
            moviesAdapter.submitList(movies)
        }
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}