package com.cinemate.app.ui.fragments.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.cinemate.app.R
import com.cinemate.app.data.models.Movie
import com.cinemate.app.data.repositories.MovieRepository
import com.cinemate.app.databinding.FragmentDetalhesFilmeBinding
import com.cinemate.app.ui.adapters.MoviesAdapter
import com.cinemate.app.viewModel.AuthViewModel
import com.cinemate.app.viewModel.MovieViewModel
import com.cinemate.app.viewModel.MovieViewModelFactory
import com.google.firebase.firestore.FirebaseFirestore

class DetalhesFilmeFragment : Fragment() {

    private var _binding: FragmentDetalhesFilmeBinding? = null
    private val binding get() = _binding!!
    private val authViewModel: AuthViewModel by viewModels()

    private val movieViewModel: MovieViewModel by viewModels {
        MovieViewModelFactory(MovieRepository(FirebaseFirestore.getInstance()))
    }

    private var movieId: String? = null
    private var currentMovie: Movie? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            movieId = it.getString("movieId")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetalhesFilmeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.findViewById<View>(R.id.bottomNavigation)?.visibility = View.GONE

        loadMovieDetails()

        binding.btnFazerReview.setOnClickListener {
            currentMovie?.let { movie -> openPublicarReviewFragment(movie) }
        }

        binding.btnResponderReview.setOnClickListener {
            currentMovie?.let { movie -> openResponderReviewFragment(movie) }
        }
    }

    private fun loadMovieDetails() {
        movieId?.let { id ->
            movieViewModel.fetchMovieDetails(id) { movie ->
                if (movie != null) {
                    currentMovie = movie
                    populateMovieDetails(movie)
                    setupFavoriteButton(movie)
                } else {
                    Toast.makeText(requireContext(), "Erro ao carregar detalhes do filme.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun populateMovieDetails(movie: Movie) {
        val movieImage = binding.imageMovie.movieImage
        val movieFavorite = binding.imageMovie.movieFavorite

        // Configura a imagem do filme
        Glide.with(this)
            .load(movie.imagemUrl)
            .placeholder(R.drawable.placeholder_image)
            .into(movieImage)

        // Configura o botão de favorito
        authViewModel.userDetails.observe(viewLifecycleOwner) { userDetails ->
            val favoriteMovies = userDetails["filmes_favoritos"] as? List<String> ?: emptyList()
            val isFavorite = favoriteMovies.contains(movie.id)

            val favoriteIcon = if (isFavorite) {
                R.drawable.ic_favorite
            } else {
                R.drawable.ic_favorite_lesser
            }
            movieFavorite.setImageResource(favoriteIcon)

            movieFavorite.setOnClickListener {
                updateFavoriteStatus(movie, !isFavorite)
            }
        }

        // Popula os outros detalhes
        binding.tituloLabel.text = "Título: ${movie.titulo}"
        binding.notaLabel.text = "Nota: ${movie.mediaAvaliacao}"
        binding.generosLabel.text = "Gênero: ${movie.genero.joinToString(", ")}"
        binding.anoLabel.text = "Ano: ${movie.ano}"
        binding.duracaoLabel.text = "Duração: ${movie.duracao}"
        binding.faixaEtariaLabel.text = "Faixa etária: ${movie.faixaEtaria}"
        binding.sinopseLabel.text = "Sinopse: ${movie.sinopse}"
        binding.atoresLabel.text = "Atores principais: ${movie.atoresPrincipais}"
    }





    private fun setupFavoriteButton(movie: Movie) {
        val movieFavorite = binding.imageMovie.movieFavorite

        authViewModel.userDetails.observe(viewLifecycleOwner) { userDetails ->
            val favoriteMovies = userDetails["filmes_favoritos"] as? List<String> ?: emptyList()
            val isFavorite = favoriteMovies.contains(movie.id)

            val favoriteIcon = if (isFavorite) {
                R.drawable.ic_favorite
            } else {
                R.drawable.ic_favorite_lesser
            }
            movieFavorite.setImageResource(favoriteIcon)

            movieFavorite.setOnClickListener {
                updateFavoriteStatus(movie, !isFavorite)
            }
        }
    }

    private fun updateFavoriteStatus(movie: Movie, isFavorite: Boolean) {
        authViewModel.userDetails.value?.let { userDetails ->
            val currentFavorites = userDetails["filmes_favoritos"] as? List<String> ?: emptyList()
            val updatedFavorites = if (isFavorite) {
                currentFavorites.toMutableSet().apply { add(movie.id) }.toList()
            } else {
                currentFavorites.toMutableSet().apply { remove(movie.id) }.toList()
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

    private fun openPublicarReviewFragment(movie: Movie) {
        val publicarReviewFragment = PublicarReviewFragment().apply {
            arguments = Bundle().apply {
                putString("movieId", movie.id)
            }
        }

        requireActivity().supportFragmentManager.beginTransaction()
            .add(R.id.fragmentContainer, publicarReviewFragment, "PublicarReview")
            .addToBackStack("PublicarReview")
            .commit()
    }

    private fun openResponderReviewFragment(movie: Movie) {
        val responderReviewFragment = ResponderReviewFragment().apply {
            arguments = Bundle().apply {
                putString("movieId", movie.id)
            }
        }

        requireActivity().supportFragmentManager.beginTransaction()
            .add(R.id.fragmentContainer, responderReviewFragment, "ResponderReview")
            .addToBackStack("ResponderReview")
            .commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        activity?.findViewById<View>(R.id.bottomNavigation)?.visibility = View.VISIBLE
        _binding = null
    }
}
