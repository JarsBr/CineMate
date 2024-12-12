package com.cinemate.app.ui.fragments.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.cinemate.app.R
import com.cinemate.app.data.models.Response
import com.cinemate.app.databinding.FragmentResponderReviewBinding
import com.cinemate.app.viewModel.ResponseViewModel
import com.google.firebase.Timestamp


class ResponderReviewFragment : Fragment() {
    private var _binding: FragmentResponderReviewBinding? = null
    private val binding get() = _binding!!
    private val responseViewModel: ResponseViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentResponderReviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.findViewById<View>(R.id.bottomNavigation)?.visibility = View.GONE

        // Pega o ID da review passado via argumentos
        val idReview = arguments?.getString("reviewId") ?: return

        binding.btnSubmeterResposta.setOnClickListener {
            val comentario = binding.etRespostaComentario.text.toString().trim()
            if (comentario.isNotEmpty()) {
                // Criando a resposta
                val resposta = Response(
                    comentario = comentario,
                    idReview = idReview,
                    dataCriacao = Timestamp.now()
                )
                // Submetendo a resposta através do ViewModel
                responseViewModel.submitResponse(resposta)
                // Limpar o campo de comentário
                binding.etRespostaComentario.text.clear()
            } else {
                Toast.makeText(requireContext(), "Por favor, digite uma resposta.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        activity?.findViewById<View>(R.id.bottomNavigation)?.visibility = View.VISIBLE
        _binding = null
    }
}

