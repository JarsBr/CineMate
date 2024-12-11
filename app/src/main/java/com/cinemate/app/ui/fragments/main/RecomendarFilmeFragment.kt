package com.cinemate.app.ui.fragments.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.cinemate.app.R
import com.cinemate.app.data.models.Suggestion
import com.cinemate.app.data.repositories.SuggestionRepository
import com.cinemate.app.databinding.FragmentRecomendarFilmeBinding
import com.cinemate.app.viewModel.SuggestionViewModel
import com.cinemate.app.viewModel.SuggestionViewModelFactory
import com.google.firebase.firestore.FirebaseFirestore

class RecomendarFilmeFragment : Fragment() {

    private var _binding: FragmentRecomendarFilmeBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: SuggestionViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecomendarFilmeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.findViewById<View>(R.id.bottomNavigation)?.visibility = View.GONE


        val repository = SuggestionRepository(FirebaseFirestore.getInstance())
        viewModel = ViewModelProvider(this, SuggestionViewModelFactory(repository))[SuggestionViewModel::class.java]

        binding.btnSugerirFilme.setOnClickListener {
            val suggestionText = binding.sugestaoFilmeInput.editText?.text.toString()
            if (suggestionText.isNotBlank()) {
                val suggestion = Suggestion(suggestionText = suggestionText)
                viewModel.saveSuggestion(
                    suggestion,
                    onSuccess = {

                        Toast.makeText(requireContext(), "Sugestão enviada!", Toast.LENGTH_SHORT).show()
                        binding.sugestaoFilmeInput.editText?.text?.clear()


                        requireActivity().supportFragmentManager.popBackStack()
                    },
                    onFailure = { exception ->

                        Toast.makeText(requireContext(), "Erro: ${exception.message}", Toast.LENGTH_SHORT).show()
                    }
                )
            } else {

                Toast.makeText(requireContext(), "Por favor, insira uma sugestão.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        activity?.findViewById<View>(R.id.bottomNavigation)?.visibility = View.VISIBLE
        _binding = null
    }
}
