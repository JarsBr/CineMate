package com.cinemate.app.ui.fragments.admin

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.cinemate.app.R
import com.cinemate.app.data.repositories.MovieRepository
import com.cinemate.app.databinding.FragmentFilmesCadastradosBinding
import com.cinemate.app.ui.adapters.MoviesAdapter
import com.cinemate.app.ui.viewmodels.MovieViewModel
import com.cinemate.app.ui.viewmodels.MovieViewModelFactory
import com.google.firebase.firestore.FirebaseFirestore

class FilmesCadastradosFragment : Fragment() {

    private var _binding: FragmentFilmesCadastradosBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MovieViewModel by viewModels {
        MovieViewModelFactory(MovieRepository(FirebaseFirestore.getInstance()))
    }

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

        setupRecyclerView()
        setupSearch()
        observeViewModel()

        binding.btnAdd.setOnClickListener {
            findNavController().navigate(R.id.action_filmesCadastradosFragment_to_adicionarFilmeFragment)
        }

        binding.btnSugestoes.setOnClickListener {
            findNavController().navigate(R.id.action_filmesCadastradosFragment_to_sugestoesFragment)
        }
    }

    private fun setupRecyclerView() {
        moviesAdapter = MoviesAdapter.MoviesListAdapter { selectedMovie ->
            val action = FilmesCadastradosFragmentDirections
                .actionFilmesCadastradosFragmentToAtualizarFilmeFragment(selectedMovie)
            findNavController().navigate(action)
        }
        binding.recyclerViewFilmes.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = moviesAdapter
        }
    }


    private fun setupSearch() {
        binding.searchInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.filterMovies(s.toString())
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun observeViewModel() {
        viewModel.filteredMovies.observe(viewLifecycleOwner) { movies ->
            moviesAdapter.submitList(movies)
        }

        viewModel.showNoMoviesMessage.observe(viewLifecycleOwner) { show ->
            if (show) {
                binding.noMoviesCard.visibility = View.VISIBLE
                binding.recyclerViewFilmes.visibility = View.INVISIBLE
            } else {
                binding.noMoviesCard.visibility = View.INVISIBLE
                binding.recyclerViewFilmes.visibility = View.VISIBLE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
