package com.cinemate.app.ui.fragments.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.cinemate.app.R
import com.cinemate.app.databinding.FragmentRespostasReviewBinding
import com.cinemate.app.ui.adapters.RespostasAdapter
import com.cinemate.app.viewModel.ResponseViewModel

class RespostasReviewFragment : Fragment() {

    private var _binding: FragmentRespostasReviewBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ResponseViewModel by viewModels()
    private lateinit var responsesAdapter: RespostasAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRespostasReviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val reviewId = arguments?.getString("reviewId") ?: return

        setupRecyclerView()
        observeViewModel()

        viewModel.fetchReviewById(reviewId)
        viewModel.fetchRespostas(reviewId)

        binding.buttonAddResposta.setOnClickListener {
            val bundle = Bundle().apply {
                putString("reviewId", reviewId)
            }
            navigateToResponderReviewFragment(bundle)
        }
    }

    private fun setupRecyclerView() {
        responsesAdapter = RespostasAdapter(emptyList())
        binding.recyclerViewRespostas.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = responsesAdapter
        }
    }

    private fun observeViewModel() {
        viewModel.selectedReview.observe(viewLifecycleOwner) { review ->
            if (review != null) {
                binding.tvReviewComentario.text = review.comentario
                binding.tvReviewNota.text = "Nota: ${review.nota}"
                binding.tvReviewUsuario.text = "Por: ${review.nomeUsuario}"
            } else {
                binding.tvReviewComentario.text = "Erro ao carregar a review"
            }
        }

        viewModel.respostas.observe(viewLifecycleOwner) { respostas ->
            if (respostas.isEmpty()) {
                binding.tvRespostasEmpty.visibility = View.VISIBLE
                binding.tvRespostasEmpty.text = "Nenhuma resposta encontrada."
            } else {
                binding.tvRespostasEmpty.visibility = View.GONE
            }
            responsesAdapter.updateRespostas(respostas)
        }
    }


    private fun navigateToResponderReviewFragment(bundle: Bundle) {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, ResponderReviewFragment().apply { arguments = bundle })
            .addToBackStack(null)
            .commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        activity?.findViewById<View>(R.id.bottomNavigation)?.visibility = View.VISIBLE
        _binding = null
    }
}


