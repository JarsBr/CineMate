package com.cinemate.app.ui.fragments.admin

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.cinemate.app.data.models.Suggestion
import com.cinemate.app.ui.adapters.SugestoesAdapter
import com.cinemate.app.databinding.FragmentSugestoesBinding
import com.google.firebase.firestore.FirebaseFirestore

class SugestoesFragment : Fragment() {

    private var _binding: FragmentSugestoesBinding? = null
    private val binding get() = _binding!!

    private lateinit var suggestionAdapter: SugestoesAdapter
    private val firestore = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSugestoesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())


        firestore.collection("suggestions")
            .get()
            .addOnSuccessListener { documents ->
                val suggestions = documents.map { document ->
                    Suggestion(suggestionText = document.getString("suggestionText") ?: "")
                }
                suggestionAdapter = SugestoesAdapter(suggestions)
                binding.recyclerView.adapter = suggestionAdapter
            }
            .addOnFailureListener { exception ->
                Toast.makeText(
                    requireContext(),
                    "Erro ao carregar sugestões: ${exception.message}",
                    Toast.LENGTH_SHORT
                ).show()


                Log.e("SugestoesFragment", "Erro ao buscar sugestões", exception)
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
