package com.cinemate.app.ui.fragments.admin

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.cinemate.app.R
import com.cinemate.app.data.models.Response
import com.cinemate.app.data.models.Review
import com.cinemate.app.databinding.FragmentGestaoRepostasBinding
import com.cinemate.app.ui.adapters.RespostasAdapter
import com.cinemate.app.viewModel.ResponseViewModel

class GestaoRepostasFragment : Fragment() {

    private var _binding: FragmentGestaoRepostasBinding? = null
    private val binding get() = _binding!!
    private val responseViewModel: ResponseViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGestaoRepostasBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Obtendo o objeto 'review' passado como argumento
        val review = arguments?.getParcelable<Review>("review") ?: return

        val reviewId = arguments?.getString("reviewId") ?: return

        // Passa o ID para o ViewModel buscar as respostas associadas
        responseViewModel.fetchRespostas(reviewId)


        // Observa as respostas carregadas
        responseViewModel.respostas.observe(viewLifecycleOwner) { respostas ->
            if (respostas.isEmpty()) {
                Log.d("GestaoRepostasFragment", "Não há respostas para essa review.")
            } else {
                Log.d("GestaoRepostasFragment", "Respostas carregadas: ${respostas.size}")
            }

            binding.recyclerViewRespostas.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = RespostasAdapter(respostas)
            }
        }

        // Busca as respostas associadas ao reviewId
        responseViewModel.fetchRespostas(reviewId)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
