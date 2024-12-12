package com.cinemate.app.ui.fragments.main

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.cinemate.app.R
import com.cinemate.app.data.models.Movie
import com.cinemate.app.data.models.Review
import com.cinemate.app.databinding.FragmentAdicionarReviewBinding
import com.cinemate.app.viewModel.AuthViewModel
import com.cinemate.app.viewModel.ReviewViewModel
import com.google.firebase.Timestamp


class AdicionarReviewFragment : Fragment() {
    private var _binding: FragmentAdicionarReviewBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ReviewViewModel by viewModels()
    private val authViewModel: AuthViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAdicionarReviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        binding.buttonSubmitReview.setOnClickListener {
            criarReview()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        activity?.findViewById<View>(R.id.bottomNavigation)?.visibility = View.VISIBLE
        _binding = null
    }

    private fun criarReview() {
        val movieId = arguments?.getString("movieId") ?: return
        val comentario = binding.editTextReview.text.toString()
        val nota = binding.ratingBarReview.rating.toInt()

        if (comentario.isNotEmpty() && nota > 0) {
            authViewModel.userDetails.observe(viewLifecycleOwner) { userDetails ->
                val userId = authViewModel.getCurrentUserId() ?: return@observe
                val userName = userDetails["nome"] as? String ?: "Usuário desconhecido"

                val review = Review(
                    comentario,
                    dataCriacao = Timestamp.now(),
                    idFilme = movieId,
                    idUsuario = userId,
                    nota,
                    nomeUsuario = userName
                )

                viewModel.addReview(review) { success ->
                    if (success) {
                        Toast.makeText(requireContext(), "Review feita com sucesso", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(requireContext(), "Falha ao fazer a review", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        } else {
            Toast.makeText(requireContext(), "Por favor, preencha todos os campos", Toast.LENGTH_SHORT).show()
        }
    }
}






