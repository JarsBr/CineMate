package com.cinemate.app.ui.fragments.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.cinemate.app.R
import com.cinemate.app.databinding.FragmentAlterarDadosBinding
import com.cinemate.app.databinding.FragmentDetalhesFilmeBinding


class DetalhesFilmeFragment : Fragment() {

    private var _binding: FragmentDetalhesFilmeBinding? = null
    private val binding get() = _binding!!

    private var movieId: String? = null // Armazena o ID do filme selecionado

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            movieId = it.getString("movieId") // Recupera o ID do filme
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

        binding.textViewMovieId.text = movieId
    }

    override fun onDestroyView() {
        super.onDestroyView()
        activity?.findViewById<View>(R.id.bottomNavigation)?.visibility = View.VISIBLE
        _binding = null
    }
}

